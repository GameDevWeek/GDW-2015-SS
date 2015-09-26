package de.hochschuletrier.gdw.ss15.network.gdwNetwork;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

public class Clientsocket extends BaseClient implements Closeable
{
	private ConnectStatus m_Connectstatus = ConnectStatus.ErrorNeverConnected;
	private AtomicBoolean m_CalledConnectListeners = new AtomicBoolean(true);
	
	private UdpSocket m_Clocksocket;
	private String m_Ip;
	private int m_Ports[] = new int[3];
	private Timeouthandler m_Timeouthandlers[] = new Timeouthandler[2];
	
	private HashMap<Integer, HolderSocketConnectListener<Object>> m_ConnectListeners = new HashMap<>();
	
	private static final short m_LoginKey1 = 42;
	private static final long m_LoginKey2 = 1342797446;
	private static final short m_LoginKeyReceived1 = 1337;
	private static final short m_LoginKeyReceived2 = 10815;
	
	private int m_TimeToResendHandshake = 500;
	private int m_MaxTrysResendHandshake = 5;
	
	private MyTimer m_ConnectTimer = new MyTimer();
	private boolean m_HandshakePart1;
	private int m_AcctualLoginTry;
	private long m_Logintoken = -1;
	
	private ClockSyncHandler m_ClockSyncHandler;
	private boolean m_BySyncClock;
	private MyTimer m_ServerTimer = new MyTimer();
	
	public Clientsocket(String ip,int port,boolean localNetwork)
	{//erstellt client socket auf der ip dem port und den naechsten drei ports, verbindet nicht zum server
		this(ip,port,port+1,port+2, localNetwork);
	}
	
	public Clientsocket(String ip,int portsave,int portunsave,int portclock,boolean localNetwork)
	{//erstellt client socket auf der ip und den drei ports, verbindet nicht zum server
		super(localNetwork);
		m_Ports[Sockettypes.SaveSocket.getValue()] = portsave;
		m_Ports[Sockettypes.UnsaveSocket.getValue()] = portunsave;
		m_Ports[Sockettypes.ClockSocket.getValue()] = portclock;
		
		for(int i=0;i<2;i++)
		{
			m_Sockets[i]=null;
		}
		m_Clocksocket=null;
		m_Ip=ip;
		
		m_ThreadPool = new ThreadPoolExecutor(1, 2, 3, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
	}
	
	public boolean connect()
	{/*versucht connect zum server zu starten, gibt false zurueck falls socket berits verbunden oder sockets nicht erstellt werden k�nnen,
	 wenn true zurueck gegeben wird bedeutet das nicht das die verbindung geklaptt hat sondern das er versucht eine verbindung aufzubauten
	 -> siehe Funktionenen getConnectStatus(), registerConnectListnerLambda(), registerConnectListner()*/
		if(m_Connected.get())
		{
			return false;
		}
		
		if(!ClearSocketsAndThreads())
		{
			return false;
		}
		
		m_ReceivedPacketsSave.clear();
		m_ReceivedPacketsUnsave.clear();
		
		try
		{
			m_Sockets[Sockettypes.SaveSocket.getValue()]=new UdpSocket(m_Ip, m_Ports[Sockettypes.SaveSocket.getValue()]);
			m_Sockets[Sockettypes.SaveSocket.getValue()].setTimeout(1);
			m_Sockets[Sockettypes.UnsaveSocket.getValue()]=new UdpSocket(m_Ip, m_Ports[Sockettypes.UnsaveSocket.getValue()]);
			m_Sockets[Sockettypes.UnsaveSocket.getValue()].setTimeout(1);
			m_Clocksocket = new UdpSocket(m_Ip, m_Ports[Sockettypes.ClockSocket.getValue()]);
			m_Clocksocket.setTimeout(1);
		}
		catch (SocketException ex)
		{
			ex.printStackTrace();
			return false;
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
		
		m_ClockSyncHandler = new ClockSyncHandler(m_Clocksocket);
		m_BySyncClock=true;
		if(!m_ClockSyncHandler.StartNewSynconisation(m_ServerTimer))
		{
			return false;
		}
		
		m_AcctualLoginTry=1;
		m_HandshakePart1=true;
		m_Connectstatus=ConnectStatus.ByConnect;
		m_ConnectTimer.StartCounterMS(m_TimeToResendHandshake);
		
		m_ThreadPool.execute(()->RunConnect());
		
		return true;
	}
	
	public boolean disconnect()//doto do a real disconnect with answer from other side
	{//Disconnected den clienten vom Server blockeiert bis alle threads ausgelaufen sind, gibt fall zurueck fall es nicht gelapt hat (socket errors ohter threads)sollte nicht passieren
		if(m_Connected.get() && !m_ByDisconned.get())
		{
			m_ByDisconned.set(true);
			m_Connected.set(false);
			m_DisconnectHandler = new DisconnectHandler(m_Sockets[Sockettypes.UnsaveSocket.getValue()], false);
			m_ThreadPool.execute(()->RunReceiveDisconnect());
			return true;
		}
		return false;
	}
	
	public void registerConnectListnerLambda(SocketConnectListener linstner,Object bindto)
	{//regstirert callback funktion mit lambda
		m_ConnectListeners.put(linstner.hashCode(), new HolderSocketConnectListener<Object>(linstner,bindto));
	}
	
	public void registerConnectListner(SocketConnectListener linstner)
	{//registriert callback funktion mit objekt
		m_ConnectListeners.put(linstner.hashCode(), new HolderSocketConnectListener<Object>(linstner,linstner));
	}
	
	@Override
	public void callListeners()
	{//ruft alle registrieren connect und packet callback funktionen auf (extra funktion damit nicht threadet)
		//Tools.Sleep(1000);
		if(m_CalledConnectListeners.get() == false)
		{
			m_CalledConnectListeners.set(true);
			Iterator<Entry<Integer, HolderSocketConnectListener<Object>>> it = m_ConnectListeners.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<Integer, HolderSocketConnectListener<Object>> pair = (Map.Entry<Integer, HolderSocketConnectListener<Object>>)it.next();
				if(pair.getValue().get() == null || pair.getValue().isEnqueued())
				{
					it.remove();
				}
				else
				{
					pair.getValue().LoginFinished(m_Connectstatus);
				}
			}
		}
		super.callListeners();
	}
	
	public ConnectStatus getConnectStatus()
	{//gibt den connect status zurueck
		return m_Connectstatus;
	}
	
	@Override
	public boolean clientInUse()
	{
		return super.clientInUse() || m_Connectstatus == ConnectStatus.ByConnect;
	}
	
	//-----------------------------------private functions--------------------------------------------
	
	private void RunConnect()
	{
		if(m_Connectstatus == ConnectStatus.ByConnect)
		{
			try
			{
				if(m_BySyncClock)
				{
					int rc = m_ClockSyncHandler.Update();
					if(rc == 1)
					{
						m_BySyncClock=false;
					}
					else if(rc == -1)
					{
						FinishConnectTry(ConnectStatus.ErrorClockSync);
						return;
					}
				}
				else
				{
					UdpData from = new UdpData();
					ByteArrayInputStream input = m_Sockets[Sockettypes.UnsaveSocket.getValue()].ReceiveByte(from);
									
					if(input.available()==Packet.getMainheadersize()+Byte.SIZE/8 &&
							m_Sockets[Sockettypes.UnsaveSocket.getValue()].get_InetAdress().equals(from.get_InetAdress()))
					{
						DataInputStream datainput = new DataInputStream(input);
						byte flag = datainput.readByte();
						short sh = datainput.readShort();
						long lh = datainput.readLong();
						
						if(flag == UnsaveSocketFlag.Login1.getValue() && sh == m_LoginKeyReceived1)
						{
							m_Logintoken = lh;
							m_HandshakePart1 = false;
							m_ConnectTimer.StartCounterMS(m_TimeToResendHandshake);
							m_AcctualLoginTry=0;
						}
						else if(flag == UnsaveSocketFlag.Login2.getValue() && sh == m_LoginKeyReceived2 && lh == m_Logintoken + m_LoginKey1 + m_LoginKeyReceived1)
						{
					
							m_Timeouthandlers[Sockettypes.SaveSocket.getValue()] = new Timeouthandler(m_Sockets[Sockettypes.SaveSocket.getValue()], (byte)SaveSocketFlag.IsAlive.getValue());
							m_Timeouthandlers[Sockettypes.UnsaveSocket.getValue()] = new Timeouthandler(m_Sockets[Sockettypes.UnsaveSocket.getValue()], (byte)UnsaveSocketFlag.IsAlive.getValue());
							
							m_ReliablePackethandler = new ReliablePackethandler(10*m_LatenzValue, m_Sockets[Sockettypes.SaveSocket.getValue()]);
							
							m_Connected.set(true);
							m_ThreadPool.execute(()->RunReceiveSave());
							m_ThreadPool.execute(()->RunReceiveUnsave());
							
							FinishConnectTry(ConnectStatus.Succes);
						}
						
						
					}
				
					m_ConnectTimer.Update();
					if(m_ConnectTimer.get_CounterMilliseconds()>m_TimeToResendHandshake)
					{
						if(m_AcctualLoginTry > m_MaxTrysResendHandshake)
						{
							FinishConnectTry(ConnectStatus.ErrorNoConnection);
						}
						else
						{
							m_ConnectTimer.StartCounter();
							m_AcctualLoginTry++;
							sendLoginHandshake();
						}
					}
				}
				m_ThreadPool.execute(()->RunConnect());
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				FinishConnectTry(ConnectStatus.ErrorInternal);
				return;
			}
		}
	}
	
	private void FinishConnectTry(ConnectStatus status)
	{
		m_Connectstatus = status;
		m_CalledConnectListeners.set(false);
	}
	
	private void RunReceiveSave()
	{
		if(m_Connected.get())
		{
			try
			{
				UdpData from = new UdpData();
				ByteArrayInputStream input = m_Sockets[Sockettypes.SaveSocket.getValue()].ReceiveByte(from);
				if(input.available() > 0 && from.get_InetAdress().equals(m_Sockets[Sockettypes.SaveSocket.getValue()].get_InetAdress()))
				{//data available and come from right socket
					int flag = input.read();
					if(flag == SaveSocketFlag.Ack.getValue() && input.available() == Long.SIZE/8)
					{
						DataInputStream datainput = new DataInputStream(input);
						m_ReliablePackethandler.ReceivedAck(datainput.readLong());
					}
					else if(flag == SaveSocketFlag.Packet.getValue())
					{
						receivePacketSave(input);
					}
					else if(flag == SaveSocketFlag.IsAlive.getValue())
					{
						m_Timeouthandlers[Sockettypes.SaveSocket.getValue()].ReceivedIsAliveAswer(input);
					}
				}
				
				if(!m_Timeouthandlers[Sockettypes.SaveSocket.getValue()].Update())
				{
					finishDisconnect();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			m_ThreadPool.execute(()->RunReceiveSave());
		}
	}
	
	private void RunReceiveUnsave()
	{
		if(m_Connected.get())
		{
			try
			{
				UdpData from = new UdpData();
				ByteArrayInputStream input = m_Sockets[Sockettypes.UnsaveSocket.getValue()].ReceiveByte(from);
				if(input.available()>0)
				{
				}
				if(input.available() > 0 && from.get_InetAdress().equals(m_Sockets[Sockettypes.UnsaveSocket.getValue()].get_InetAdress()))
				{//data available and come from right socket
					int flag = input.read();
					if(flag == UnsaveSocketFlag.IsAlive.getValue())
					{
						m_Timeouthandlers[Sockettypes.UnsaveSocket.getValue()].ReceivedIsAliveAswer(input);
					}
					else if(flag == UnsaveSocketFlag.Packet.getValue() && input.available()>=Packet.getMainheadersize())
					{
						this.receivePacketUnsave(input);//from base client class
					}
					else if(flag == UnsaveSocketFlag.Disconnect.getValue() && input.available() == 1 && input.read() == 0)
					{
						m_ByDisconned.set(true);
						m_Connected.set(false);
						m_DisconnectHandler = new DisconnectHandler(m_Sockets[Sockettypes.UnsaveSocket.getValue()],true);
						m_ThreadPool.execute(() -> RunReceiveDisconnect());
					}
				}
				
				if(!m_Timeouthandlers[Sockettypes.UnsaveSocket.getValue()].Update())
				{
					finishDisconnect();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				finishDisconnect();
			}
			m_ThreadPool.execute(()->RunReceiveUnsave());
		}
	}

	public boolean isByConnect()
	{
		return m_Connectstatus == ConnectStatus.ByConnect;
	}

	private boolean ClearSocketsAndThreads()
	{
		try
		{
			while(m_ThreadPool.getActiveCount() != 0)
			{
				m_ThreadPool.awaitTermination(100, TimeUnit.MICROSECONDS);
			}
		}
		catch(InterruptedException ex)
		{
		}
		
		try
		{
			UdpSocket.ClearSocket(m_Sockets[Sockettypes.SaveSocket.getValue()]);
			UdpSocket.ClearSocket(m_Sockets[Sockettypes.UnsaveSocket.getValue()]);
			UdpSocket.ClearSocket(m_Clocksocket);
		}
		catch(SocketException ex)
		{
			return false;
		}
		return true;
	}
	
	private void sendLoginHandshake() throws IOException
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream outputdata = new DataOutputStream(output);
		if(m_HandshakePart1)
		{
			outputdata.writeByte(UnsaveSocketFlag.Login1.getValue());
			outputdata.writeShort(m_LoginKey1);
			outputdata.writeLong(m_LoginKey2);
			m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output);
		}
		else
		{
			outputdata.writeByte(UnsaveSocketFlag.Login2.getValue());
			outputdata.writeShort(m_LoginKey1 + m_LoginKeyReceived1);
			outputdata.writeLong(m_Logintoken);
			m_Sockets[Sockettypes.SaveSocket.getValue()].Send(output,m_Sockets[Sockettypes.UnsaveSocket.getValue()].get_UdpData());
			//sends from save to unsave socket
		}
	}

	@Override
	protected long getTimestamp()
	{
		m_ServerTimer.Update();
		return m_ServerTimer.get_CounterNanoseconds();
	}
}
