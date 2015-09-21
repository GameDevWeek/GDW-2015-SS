package gdwNetwork;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;

import gdwNetwork.basic.BaseClient;
import gdwNetwork.basic.UdpData;
import gdwNetwork.basic.UdpSocket;
import gdwNetwork.data.Packet;
import gdwNetwork.enums.SaveSocketFlag;
import gdwNetwork.enums.Sockettypes;
import gdwNetwork.enums.UnsaveSocketFlag;
import gdwNetwork.tools.DisconnectHandler;
import gdwNetwork.tools.MyTimer;
import gdwNetwork.tools.ReliablePackethandler;
import gdwNetwork.tools.Tools;

public class Serverclientsocket extends BaseClient
{
	private Serversocket m_Serversocket;
	private LinkedList<ByteArrayInputStream> m_BuffersSave = new LinkedList<ByteArrayInputStream>();
	private LinkedList<ByteArrayInputStream> m_BuffersUnsave = new LinkedList<ByteArrayInputStream>();
	
	private UdpData m_UdpData[] = new UdpData[2];
	
	private MyTimer m_TimeoutTimers[] = new MyTimer[2];
	private int m_Timouttime = 10000; //10 seconds
	
	static final int m_PacketTimeout = 100;
	
	private static final int m_SizeOfIsAliveMessage = Long.SIZE/8;
	
	public Serverclientsocket(Serversocket serversocket,UdpSocket savesocket,UdpData savedata,UdpSocket unsavesocket,UdpData unsavedata,boolean localnetwork)
	{//intern fuer Serversocket
		super(localnetwork);
		m_Connected.set(true);
		m_Serversocket=serversocket;
		m_ThreadPool = m_Serversocket.getThreadPool();
		m_Sockets[Sockettypes.SaveSocket.getValue()]=savesocket;
		m_UdpData[Sockettypes.SaveSocket.getValue()]=savedata;
		m_Sockets[Sockettypes.UnsaveSocket.getValue()]=unsavesocket;
		m_UdpData[Sockettypes.UnsaveSocket.getValue()]=unsavedata;
		
		m_TimeoutTimers[Sockettypes.SaveSocket.getValue()] = new MyTimer(true);
		m_TimeoutTimers[Sockettypes.UnsaveSocket.getValue()] = new MyTimer(true);
		
		m_ReliablePackethandler = new ReliablePackethandler(m_PacketTimeout, m_Sockets[Sockettypes.SaveSocket.getValue()],m_UdpData[Sockettypes.SaveSocket.getValue()]);
		
		m_Serversocket.RegisterClient(this);
		
		m_ThreadPool.execute(()->RunReceiveSave());
		m_ThreadPool.execute(()->RunReceiveUnsave());
		
		
	}
	
	public void close()
	{//schlie�t client connection
		m_Serversocket.UnregisterClient(this);
		disconnect();
		while(m_ByDisconned.get())
		{
			Tools.Sleep(1);
		}
	}
	
	@Override
	public boolean sendPacketUnsave(Packet pack,boolean rebuildPacket)
	{//versendet ein packet �ber den unsave socket, rebuildPacket -> packet wird neu gebaut, sollte nur gemacht werden wenn sich daten ge�ndert haben (performance)
		if(m_Connected.get())
		{
			if(rebuildPacket && m_InsertTimestampAutomaticly)
			{
				pack.setTimestamp(getTimestamp());
			}
			try
			{
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				output.write(UnsaveSocketFlag.Packet.getValue());
				pack.getOutputstream(rebuildPacket).writeTo(output);
				//return m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output);
				
				return m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output,m_UdpData[Sockettypes.UnsaveSocket.getValue()]);
			}
			catch (IOException ex)
			{
				System.out.println("Stream fehler bie senden eines packets");
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public UdpData get_UdpDataSave()
	{//intern fuer serversocket
		return m_UdpData[Sockettypes.SaveSocket.getValue()];
	}
	
	public UdpData get_UdpDataUnsave()
	{//intern fuer serversocket
		return m_UdpData[Sockettypes.UnsaveSocket.getValue()];
	}
	
	public void AddNewDataSave(ByteArrayInputStream input)
	{//intern fuer serversocket
		synchronized (m_BuffersSave)
		{
			m_BuffersSave.add(input);
		}
	}
	
	public void AddNewDataUnsave(ByteArrayInputStream input)
	{//intern fuer serversocket
		synchronized (m_BuffersUnsave)
		{
			m_BuffersUnsave.add(input);
		}
	}
	
	@Override
	public boolean disconnect()//doto do a real disconnect with answer from other side
	{//Disconnected den clienten vom Server blockeiert bis alle threads ausgelaufen sind, gibt fall zurueck fall es nicht gelapt hat (socket errors ohter threads)sollte nicht passieren
		if(m_Connected.get() && !m_ByDisconned.get())
		{
			m_Connected.set(false);
			m_ByDisconned.set(true);
			m_DisconnectHandler = new DisconnectHandler(m_Sockets[Sockettypes.UnsaveSocket.getValue()],m_UdpData[Sockettypes.UnsaveSocket.getValue()], false);
			m_ThreadPool.execute(()->RunReceiveDisconnect());
			return true;
		}
		return false;
	}
	
	//----------------------------------------private functions--------------------------------
	
	private void RunReceiveSave()
	{
		if(m_Connected.get())
		{
			while(!m_BuffersSave.isEmpty())
			{
				//System.out.println("Client received data save");
				ByteArrayInputStream stream;
				synchronized (m_BuffersSave)
				{
					stream = m_BuffersSave.poll();
				}
				HandleReceivedDataSave(stream);
			}
			
			m_TimeoutTimers[Sockettypes.SaveSocket.getValue()].Update();
			if(m_TimeoutTimers[Sockettypes.SaveSocket.getValue()].get_CounterMilliseconds() > m_Timouttime)
			{
				//System.out.println("Client timed out from save socket");
				finishDisconnect();
			}
			
			m_ThreadPool.execute(()->RunReceiveSave());
		}
	}
	
	private void RunReceiveUnsave()
	{
		if(m_Connected.get())
		{
			while(!m_BuffersUnsave.isEmpty())
			{
				//System.out.println("Client received data unsave");
				ByteArrayInputStream stream;
				synchronized (m_BuffersUnsave)
				{
					stream = m_BuffersUnsave.poll();
				}
				HandleReceivedDataUnsave(stream);
			}
			
			m_TimeoutTimers[Sockettypes.UnsaveSocket.getValue()].Update();
			if(m_TimeoutTimers[Sockettypes.UnsaveSocket.getValue()].get_CounterMilliseconds() > m_Timouttime)
			{
				//System.out.println("Client timed out from unsave socket");
				finishDisconnect();
			}
			
			m_ThreadPool.execute(()->RunReceiveUnsave());
		}
	}

	private void HandleReceivedDataSave(ByteArrayInputStream input)
	{
		try
		{
			int flag = input.read();
			//System.out.println(" " + flag +" "+SaveSocketFlag.IsAlive.getValue()+" "+input.available()+" "+m_SizeOfIsAliveMessage);
			if(flag == SaveSocketFlag.Ack.getValue() && input.available() == Long.SIZE/8)
			{
				DataInputStream datainput = new DataInputStream(input);
				m_ReliablePackethandler.ReceivedAck(datainput.readLong());
			}
			else if(flag == SaveSocketFlag.Packet.getValue())
			{
				//System.out.println("Recived packet on save socket");
				receivePacketSave(input);
			}
			else if(flag == SaveSocketFlag.IsAlive.getValue() && input.available() == m_SizeOfIsAliveMessage)
			{//received is alive Packet
				//System.out.println("Send is Alive packet back on Save socket");
				byte arr[] = new byte[m_SizeOfIsAliveMessage];
				input.read(arr,0,m_SizeOfIsAliveMessage);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				output.write(SaveSocketFlag.IsAlive.getValue());
				output.write(arr,0,m_SizeOfIsAliveMessage);
				m_Sockets[Sockettypes.SaveSocket.getValue()].Send(output,m_UdpData[Sockettypes.SaveSocket.getValue()]);
				m_TimeoutTimers[Sockettypes.SaveSocket.getValue()].StartCounter();
			}
		}
		catch (IOException ex)
		{
			System.out.println("Strem exeptio biem empfangen von daten geflogen");
			ex.printStackTrace();
		}
	}
	
	private void HandleReceivedDataUnsave(ByteArrayInputStream input)
	{
		try
		{
			int flag = input.read();
			//System.out.println(" " + flag +" "+UnsaveSocketFlag.IsAlive.getValue()+" "+input.available()+" "+m_SizeOfIsAliveMessage);
			if(flag == UnsaveSocketFlag.IsAlive.getValue() && input.available() == m_SizeOfIsAliveMessage)
			{//received is alive Packet
				//System.out.println("Send is Alive packet back on unsave socket");
				byte arr[] = new byte[m_SizeOfIsAliveMessage];
				input.read(arr,0,m_SizeOfIsAliveMessage);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				output.write(UnsaveSocketFlag.IsAlive.getValue());
				output.write(arr,0,m_SizeOfIsAliveMessage);
				m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output,m_UdpData[Sockettypes.UnsaveSocket.getValue()]);
				m_TimeoutTimers[Sockettypes.UnsaveSocket.getValue()].StartCounter();
			}
			else if(flag == UnsaveSocketFlag.Packet.getValue() && input.available() >= Packet.getMainheadersize())
			{
				this.receivePacketUnsave(input);
			}
			else if(flag == UnsaveSocketFlag.Disconnect.getValue() && input.available() == 1 && input.read() == 0)
			{
				System.out.println("received disconnect");
				m_ByDisconned.set(true);
				m_Connected.set(false);
				m_DisconnectHandler = new DisconnectHandler(m_Sockets[Sockettypes.UnsaveSocket.getValue()],m_UdpData[Sockettypes.UnsaveSocket.getValue()],true);
				m_ThreadPool.execute(()->RunReceiveDisconnect());
			}
		}
		catch (IOException ex)
		{
			System.out.println("Strem exeptio biem empfangen von daten geflogen");
			ex.printStackTrace();
		}
	}
	
	@Override
	protected long getTimestamp()
	{
		return MyTimer.get_TimestampNanoseconds();
	}
}
