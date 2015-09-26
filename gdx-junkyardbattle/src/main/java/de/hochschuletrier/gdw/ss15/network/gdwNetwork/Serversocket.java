package de.hochschuletrier.gdw.ss15.network.gdwNetwork;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

public class Serversocket implements Closeable
{
	private int m_Ports[] = new int[3];
	private UdpSocket m_Sockets[] = new UdpSocket[3];
	private Thread m_Threads[] = new Thread[3];
	private Timer m_Timer = new Timer();
	
	private long m_Clienttokens = 1;
	private long m_TimeoutOutTokens = 1;
	
	private static final short m_LoginKey1 = 42;
	private static final long m_LoginKey2 = 1342797446;
	private static final short m_LoginKeySend1 = 1337;
	private static final short m_LoginkeySend2 = 10815;
	private static final int m_SizeClockSyncRequest = (Long.SIZE+Byte.SIZE)/8;
	
	LinkedList<Serverclientsocket> m_NewClients = new LinkedList<Serverclientsocket>();
	HashMap<UdpData,Serverclientsocket> m_HashMapClientsSave = new HashMap<>();
	HashMap<UdpData,Serverclientsocket> m_HashMapClientsUnsave = new HashMap<>();
	HashMap<Long,UdpData> m_TryToLoginClients = new HashMap<>();
	
	private ThreadPoolExecutor m_ThreadPool = new ThreadPoolExecutor(1, 2, 3, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
	
	private AtomicBoolean m_Running = new AtomicBoolean(false);
	private boolean m_Localnetwork;
	
	public Serversocket(int portSave,int portUnsave,int portClock,boolean localnetwork)
	{//erstellt einen server socket welcher noch nicht bereit fuer verbindungen ist -> siehe Funktion Open
		System.setProperty("java.net.preferIPv4Stack" , "true");
		m_Ports[Sockettypes.SaveSocket.getValue()]=portSave;
		m_Ports[Sockettypes.UnsaveSocket.getValue()]=portUnsave;
		m_Ports[Sockettypes.ClockSocket.getValue()]=portClock;
		for(int i=0;i<3;i++)
		{
			m_Threads[i]=null;
		}
	}

	public boolean isOpen()
	{
		return m_Running.get();
	}

	public Serversocket(int port,boolean localnetwork)
	{//erstellt einen server socket auf dem uebergebenen und den naechsten zwei ports, welcher noch nicht bereit fuer verbindungen ist -> siehe Funktion Open
		this(port,port+1,port+2,localnetwork);
	}
	
	private void ClearSocketsAndThreads()
	{
		for(int i=0;i<3;i++)
		{
			if(m_Threads[i]!=null)
			{
				try
				{
					m_Threads[i].join();
				}
				catch(InterruptedException ex)
				{
					System.out.println("Thread wurde per interrupt fehlerhafterweise durch Interrupt beendet");
				}
			}
		}
		try
		{
			while(m_ThreadPool.getActiveCount()>0) {
				m_ThreadPool.awaitTermination(10, TimeUnit.MICROSECONDS);
			}
		}
		catch(InterruptedException ex)
		{
			System.out.println("Threadpool wurde per interrupt fehlerhafterweise durch Interrupt beendet");
		}
		
		for(int i=0;i<3;i++)
		{
			if(m_Sockets[i]!=null) {
				try {
					m_Sockets[i].close();
				} catch (SocketException e) {
					e.printStackTrace();
				}
				m_Sockets[i] = null;
			}
		}
	}
	
	public boolean open()
	{//erstellt die drei sockets und wartet auf anfragen ->false falls bereits offen oder socket errror
		if(m_Running.get())
		{
			ClearSocketsAndThreads();
			return false;
		}
		
		for(int i=0;i<3;i++)
		{
			try
			{
				m_Sockets[i] = new UdpSocket(m_Ports[i]);
				m_Sockets[i].setTimeout(100);
			}
			catch(SocketException ex)
			{
				System.out.println("Konnte socket "+i+" auf port "+m_Ports[i]+" nicht erstellen");
				return false;
			}
			System.out.println("Socken listening on port: "+m_Ports[i]);
		}
		
		m_Threads[Sockettypes.SaveSocket.getValue()] = new Thread(()->this.RunReceiveSave());
		m_Threads[Sockettypes.UnsaveSocket.getValue()] = new Thread(()->this.RunReceiveUnsave());
		m_Threads[Sockettypes.ClockSocket.getValue()] = new Thread(()->this.RunReceiveClock());
		
		m_Running.set(true);
		
		for(int i=0;i<3;i++)
		{
			m_Threads[i].start();
		}
		
		System.out.println("Serversocket succesfully startet");
		
		return true;
	}
	
	public void close()
	{//schliest alle drei sockets und beendet alle laufenden threads -> blockend bis alle threads beendet sind
		m_Running.set(false);
		ClearSocketsAndThreads();
		m_NewClients.clear();
		System.out.println("Server socket was succesfully closed");
	}	
	
	public void RegisterClient(Serverclientsocket client)
	{//intern for serverclientsocket
		synchronized (m_HashMapClientsSave)
		{
			m_HashMapClientsSave.put(client.get_UdpDataSave(), client);
		}
		synchronized (m_HashMapClientsUnsave)
		{
			m_HashMapClientsUnsave.put(client.get_UdpDataUnsave(), client);
		}
	}
	
	public void UnregisterClient(Serverclientsocket client)
	{//intern for serverclientsocket
		synchronized (m_HashMapClientsSave)
		{
			m_HashMapClientsSave.remove(client);
		}
		synchronized (m_HashMapClientsUnsave)
		{
			m_HashMapClientsUnsave.remove(client);
		}
	}
	
	public ThreadPoolExecutor getThreadPool()
	{//intern for serverclientsocket
		return m_ThreadPool;
	}
	
	public boolean isNewClientAvaliable()
	{//returns true if new client is avaliable -> siehe Funktion: getNewClient()
		return !m_NewClients.isEmpty();
	}
	
	public Serverclientsocket getNewClient()
	{//gibt ein Objet von Serverclientsocket zuruck fall neuer client verfuegbar ansonsten null -> sollte zuerst mit isNewClientAvaliable()
		if(isNewClientAvaliable())
		{
			synchronized (m_NewClients)
			{
				return m_NewClients.poll();
			}
		}
		return null;
	}
	
	//-------------------------------------------------private functions---------------------------------------------
	
	
	private void RunReceiveSave()
	{
		while(m_Running.get())
		{
			UdpData receivedFrom = new UdpData();
			Tools.Sleep(1);
			ByteArrayInputStream input;
			try
			{
				input = m_Sockets[Sockettypes.SaveSocket.getValue()].ReceiveByte(receivedFrom);
			}
			catch(IOException ex)
			{//todo later remove
				m_Running.set(false);
				System.out.println("Internal Error by creating inputstream");
				ex.printStackTrace();
				break;
			}

			if(input.available()>0)
			{//daten empfangen
				//System.out.println("Rceived data Save");

				Serverclientsocket client;
				synchronized (m_HashMapClientsSave)
				{
					client = m_HashMapClientsSave.get(receivedFrom);
				}
				if(client != null)
				{
					//System.out.println("Client save found");
					byte arr[] = new byte[input.available()];
					input.read(arr, 0, input.available());
					ByteArrayInputStream newinput = new ByteArrayInputStream(arr);
					client.AddNewDataSave(newinput);
				}
			}
		}
	}
	
	private void RunReceiveUnsave()
	{
		while(m_Running.get())
		{
			UdpData receivedFrom = new UdpData();
			Tools.Sleep(1);
			ByteArrayInputStream input;
			try
			{
				input = m_Sockets[Sockettypes.UnsaveSocket.getValue()].ReceiveByte(receivedFrom);
			}
			catch(IOException ex)
			{
				m_Running.set(false);
				System.out.println("Internal Error by creating inputstream");
				ex.printStackTrace();
				break;
			}
			
			if(input.available()>0)
			{
				//System.out.println("Rceived data Unsave");
				
				int flag = input.read();
				if(flag == UnsaveSocketFlag.Login1.getValue())
				{
					ReceivedLoginPart1(input,receivedFrom);
				}
				else if(flag == UnsaveSocketFlag.Login2.getValue())
				{
					ReceivedLoginPart2(input,receivedFrom);
				}
				else if(flag == UnsaveSocketFlag.Packet.getValue() || flag == UnsaveSocketFlag.IsAlive.getValue() || flag == UnsaveSocketFlag.Disconnect.getValue())
				{
					input.reset();
					Serverclientsocket client;
					synchronized (m_HashMapClientsUnsave)
					{
						client = m_HashMapClientsUnsave.get(receivedFrom);
					}
					if(client != null)
					{
						//System.out.println("Client unsave found");
						byte arr[] = new byte[input.available()];
						input.read(arr, 0, input.available());
						ByteArrayInputStream newinput = new ByteArrayInputStream(arr);
						client.AddNewDataUnsave(newinput);
					}
				}
			}
		}
	}
	
	private void RunReceiveClock()
	{
		while(m_Running.get())
		{
			//System.out.println("clock rennt");
			//Tools.Sleep(1);
			try
			{
				ByteArrayInputStream input = m_Sockets[Sockettypes.ClockSocket.getValue()].ReceiveByte();
				if(input.available()==m_SizeClockSyncRequest)
				{
					//System.out.println("Received clock reqest");
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					byte arr[] = new byte[m_SizeClockSyncRequest];
					input.read(arr);
					output.write(arr);
					DataOutputStream dataout = new DataOutputStream(output);
					//dataout.writeLong(MyTimer.get_TimestampNanoseconds()+100000000);
					dataout.writeLong(MyTimer.get_TimestampNanoseconds());
					m_Sockets[Sockettypes.ClockSocket.getValue()].Send(output);
				}
			}
			catch(IOException ex)
			{
				m_Running.set(false);
				System.out.println("Internal Error by creating inputstream");
				ex.printStackTrace();
				break;
			}
		}
	}
	
	private void ReceivedLoginPart1(ByteArrayInputStream input,UdpData udpdata)
	{
		//System.out.println("received Login Part 1 threticls");
		if(input.available()>=Packet.getMainheadersize())
		{
			System.out.println("received Login Part 1");
			DataInputStream datainput = new DataInputStream(input);
			short sh;
			long lh;
			try
			{
				sh = datainput.readShort();
				lh = datainput.readLong();
				if(sh==m_LoginKey1 && lh == m_LoginKey2)
				{//succesfull lock packet
					long token = m_Clienttokens++;
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					DataOutputStream outputdata = new DataOutputStream(output);
					outputdata.writeByte(UnsaveSocketFlag.Login1.getValue());
					outputdata.writeShort(m_LoginKeySend1);
					outputdata.writeLong(token);
					
					synchronized (m_TryToLoginClients){
						m_TryToLoginClients.put(token, udpdata);
					}
					
					m_Timer.schedule(new TimerTask() {
						@Override
						public void run() {
							//System.out.println("Client connect timed out");
							LoginTimedOut(); 
						}
					}, 2000);//TODO magic number
					
					//System.out.println("Send return from login part 1");
					m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output);
				}
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
				System.out.println("Error by reading data by fist login step");
			}
		}
	}
	
	private void LoginTimedOut()
	{
		synchronized (m_TryToLoginClients){
			m_TryToLoginClients.remove(m_TimeoutOutTokens++);
		}
	}
	
	private void ReceivedLoginPart2(ByteArrayInputStream input,UdpData udpdata)
	{
		System.out.println("received Login Part 2");
		DataInputStream datainput = new DataInputStream(input);
		short sh;
		long lh;
		try
		{
			sh = datainput.readShort();
			lh = datainput.readLong();
			if(sh == m_LoginKey1 + m_LoginKeySend1)
			{//right key
				//System.out.println("keys are right");
				Serverclientsocket alreadyfinischedclient;
				synchronized (m_HashMapClientsSave)
				{
					alreadyfinischedclient = m_HashMapClientsSave.get(udpdata);
				}
				if(alreadyfinischedclient != null)
				{//client already accepted
					System.out.println("Client allready loged in send succes again");
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					DataOutputStream outputdata = new DataOutputStream(output);
					outputdata.writeByte(UnsaveSocketFlag.Login2.getValue());
					outputdata.writeShort(m_LoginkeySend2);
					outputdata.writeLong(lh + m_LoginKey1 + m_LoginKeySend1);
					m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output,alreadyfinischedclient.get_UdpDataUnsave());
					return;
				}
				
				
				UdpData dataUnsave;
				synchronized (m_TryToLoginClients){
					dataUnsave = m_TryToLoginClients.get(lh);
				}
				if(dataUnsave != null)
				{//login successfully
					Serverclientsocket client = new Serverclientsocket(this, m_Sockets[Sockettypes.SaveSocket.getValue()], udpdata,
							m_Sockets[Sockettypes.UnsaveSocket.getValue()], dataUnsave,m_Localnetwork);
				
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					DataOutputStream outputdata = new DataOutputStream(output);
					outputdata.writeByte(UnsaveSocketFlag.Login2.getValue());
					outputdata.writeShort(m_LoginkeySend2);
					outputdata.writeLong(lh + m_LoginKey1 + m_LoginKeySend1);
				
					synchronized (m_NewClients){
						m_NewClients.add(client);
					}
			
					
					m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output,client.get_UdpDataUnsave());
					System.out.println("Neuer client hat sich angemeldet");
				}
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.out.println("Error by reading data by fist login step");
		}
	}
}
