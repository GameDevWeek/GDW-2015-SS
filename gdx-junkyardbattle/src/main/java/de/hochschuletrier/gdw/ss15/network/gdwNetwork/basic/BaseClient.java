package de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseClient implements Closeable
{
	protected boolean m_InsertTimestampAutomaticly = true;
	protected AtomicBoolean m_Connected = new AtomicBoolean(false);
	protected AtomicBoolean m_ByDisconned = new AtomicBoolean(false);
	protected AtomicBoolean m_CalledDisconnectlistener = new AtomicBoolean(true);
	
	protected LinkedList<Packet> m_ReceivedPacketsSave = new LinkedList<>();
	protected LinkedList<Packet> m_ReceivedPacketsUnsave = new LinkedList<>();
	
	private HashMap<Integer, HolderSocketListener<Object>> m_Listeners = new HashMap<>();
	private HashMap<Integer, HolderSocketDisconnectListener<Object>> m_DisconnectListeners = new HashMap<>();
	
	protected UdpSocket m_Sockets[] = new UdpSocket[2];
	
	protected ReliablePackethandler m_ReliablePackethandler;
	protected DisconnectHandler m_DisconnectHandler = null;
	
	protected ThreadPoolExecutor m_ThreadPool; 
	
	protected int m_LatenzValue;
	
	public BaseClient(boolean localnetwork)
	{
		if(localnetwork)
		{
			m_LatenzValue = 1;
		}
		else
		{
			m_LatenzValue=10;
		}
	}
	
	public boolean isConnected()
	{//returns if client is connected
		return m_Connected.get();
	}
	
	public void registerListenerLambda(SocketListener linstener,Object bindto)
	{//registriert callback Funktion als Lambda f�r empfngen von Paketen
		m_Listeners.put(linstener.hashCode(), new HolderSocketListener<>(linstener,bindto));
	}
	
	public void registerListener(SocketListener linstener)
	{//registriert callback Funktion mit einem Objekt f�r empfangen von Paketen
		m_Listeners.put(linstener.hashCode(), new HolderSocketListener<>(linstener,linstener));
	}
	
	public void registerDisconnectListenerLambda(SocketDisconnectListener linstener,Object bindto)
	{
		m_DisconnectListeners.put(linstener.hashCode(), new HolderSocketDisconnectListener<>(linstener,bindto));
	}
	
	public void callListeners()
	{//fuert alle registrierten Listeners fur alle empfangen Packete aus (extra funktion damit nicht threadet)
		while(isSavePacketAvaliable())
		{
			Packet packet = getSaveReceivedPacket();
			Iterator<Entry<Integer, HolderSocketListener<Object>>> it = m_Listeners.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<Integer, HolderSocketListener<Object>> pair = (Map.Entry<Integer, HolderSocketListener<Object>>)it.next();
				if(pair.getValue().isEnqueued())
				{
					it.remove();
				}
				else
				{
					pair.getValue().ReceivedPacket(packet, true);
				}
			}
		}
		
		while(isUnsavePacketAvaliable())
		{
			Packet packet = getUnsaveReceivedPacket();
			Iterator<Entry<Integer, HolderSocketListener<Object>>> it = m_Listeners.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<Integer, HolderSocketListener<Object>> pair = (Map.Entry<Integer, HolderSocketListener<Object>>)it.next();
				if(pair.getValue().isEnqueued())
				{
					it.remove();
				}
				else
				{
					pair.getValue().ReceivedPacket(packet, false);
				}
			}
		}
		this.justCallDisconnectHandler();
	}

	public void justCallDisconnectHandler()
	{
		if(m_CalledDisconnectlistener.get() == false)
		{
			m_CalledDisconnectlistener.set(true);
			Iterator<Entry<Integer, HolderSocketDisconnectListener<Object>>> it = m_DisconnectListeners.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<Integer, HolderSocketDisconnectListener<Object>> pair = (Map.Entry<Integer, HolderSocketDisconnectListener<Object>>)it.next();
				if(pair.getValue().get() == null || pair.getValue().isEnqueued())
				{
					it.remove();
				}
				else
				{
					pair.getValue().SocketDisconnected();
				}
			}
		}
	}
	
	public void registerDisconnectListener(SocketDisconnectListener linstener)
	{
		m_DisconnectListeners.put(linstener.hashCode(), new HolderSocketDisconnectListener<>(linstener,linstener));
	}
	
	public abstract boolean disconnect();
	
	protected void RunReceiveDisconnect()
	{
		if(m_ByDisconned.get())
		{
			UdpData from = new UdpData();
			ByteArrayInputStream input;
			try
			{
				input = m_Sockets[Sockettypes.UnsaveSocket.getValue()].ReceiveByte(from);
			
				if(input.available()>0)
				{
				}
				if(input.available() ==2)//todo savty missing check socket my clientclient
				{//data available and come from right socket
					if(input.read() == UnsaveSocketFlag.Disconnect.getValue())
					{
						m_DisconnectHandler.ReceivedDisconnectMessage(input);
					}
				}
				if(m_DisconnectHandler.update())
				{
					m_DisconnectHandler=null;
					m_ByDisconned.set(false);
					m_CalledDisconnectlistener.set(false);
					return;
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				m_DisconnectHandler=null;
				m_ByDisconned.set(false);
				e.printStackTrace();
				m_CalledDisconnectlistener.set(false);
				return;
			}
			m_ThreadPool.execute(()->RunReceiveDisconnect());
		}
		else
		{
			m_CalledDisconnectlistener.set(false);
		}
	}

	public boolean isByDisconnect()
	{
		return m_ByDisconned.get();
	}


	
	public void close()
	{
		disconnect();
		while(m_Connected.get() || m_ByDisconned.get())
		{
			Tools.Sleep(1);
		}
	}
	
	protected void finishDisconnect()
	{
		if(m_Connected.get()==true)
		{
			m_CalledDisconnectlistener.set(false);
		}
		m_Connected.set(false);
	}

	public boolean clientInUse()
	{
		return m_Connected.get() || m_ByDisconned.get();
	}
	
	//-------------------recive packet stuff--------------------
	
	protected void receivePacketUnsave(ByteArrayInputStream input) throws IOException
	{
		if(input.available()<Packet.getMainheadersize())
		{
			return;
		}
		DataInputStream datainput = new DataInputStream(input);
		short packetid = datainput.readShort();
		long timestamp = datainput.readLong();
		Packet pack = PacketFactory.createPacket(packetid);
		if(pack != null && (pack.getSize()==-1 || pack.getSize() == input.available()))
		{
			pack.setTimestamp(timestamp);
			pack.buildPacketOnData(datainput);
			synchronized (m_ReceivedPacketsUnsave)
			{
				m_ReceivedPacketsUnsave.addLast(pack);
			}
		}
		else
		{
		}
	}
	
	protected void receivePacketSave(ByteArrayInputStream input) throws IOException
	{
		if(input.available()<Packet.getMainheadersize() + ReliablePackethandler.getReliableHeadSize())
		{
			return;
		}
		DataInputStream datainput = new DataInputStream(input);
		long packetnumber = datainput.readLong();
		
		if(m_ReliablePackethandler.ReceivedPacketShouldUse(packetnumber))
		{
			short packetid = datainput.readShort();
			long timestamp = datainput.readLong();
			
			Packet pack = PacketFactory.createPacket(packetid);
			if(pack != null && (pack.getSize()==-1 || pack.getSize() == input.available()))
			{
				pack.setTimestamp(timestamp);
				pack.buildPacketOnData(datainput);
				synchronized (m_ReceivedPacketsSave)
				{
					m_ReceivedPacketsSave.addLast(pack);
				}
			}
			else
			{
			}
		}
	}
	
	//----------------------------------timestamp packet suff---------------------------------------
	
	public void setInsertTimestampAutomaticly(boolean value)
	{//setzt das Flag um Timestamp beim senden eines Packetes automatisch einzufuegen
		m_InsertTimestampAutomaticly=value;
	}
	
	public boolean getInsertTimestampAutomaticly()
	{//gibt den status des Timestmap automatisch setzten Flags zurueck
		return m_InsertTimestampAutomaticly;
	}
	
	public long getTimeDifference(long timestamp)
	{//gibt die Zeitdifferenz zu syncroniserten Clock in Nanosekunden zurueck 
		long difference = getTimestamp()-timestamp;
		if(difference<0)
		{//fastern then sync counter so null
			difference = 0;
		}
		return difference;
	}
	
	public double getTimeDifferenceMS(long timestamp)
	{//gibt die Zeitdifferenz zu syncroniserten Clock in Millisekunden zurueck 
		return ((double)(getTimeDifference(timestamp)))/1000000;
	}
	
	//-------------------receive Packet stuff---------------------
	
	public boolean isSavePacketAvaliable()
	{//gibt true zurueck wenn Packet ueber sicheren socket verfuegbar ist
		return !m_ReceivedPacketsSave.isEmpty();
	}
	
	public boolean isUnsavePacketAvaliable()
	{//gibt true zurueck wenn Packet ueber unsicheren socket verfuegbar ist
		return !m_ReceivedPacketsUnsave.isEmpty();
	}

	public boolean isPacketAvaliable()
	{//gibt true zurueck wenn Packet auf sicherm oder unsicherem socket verfuegbar ist
		return this.isSavePacketAvaliable() || this.isUnsavePacketAvaliable();
	}

	public Packet getSaveReceivedPacket()
	{//gibt empfangens packet ueber sicheren socket zurueck -> null falls keins vorhanden
		 if(isSavePacketAvaliable())
		 {
			 synchronized (m_ReceivedPacketsSave)
			 {
				 return m_ReceivedPacketsSave.poll();
			 }
		 }
		 return null;
	}
	
	public Packet getUnsaveReceivedPacket()
	{//gibt empfangenes packet �ber unsichren socket zurueck-> null falls keins vorhanden
		 if(isUnsavePacketAvaliable())
		 {
			 synchronized (m_ReceivedPacketsUnsave)
			 {
				 return m_ReceivedPacketsUnsave.poll();
			 }
		 }
		 return null;
	}
	
	public Packet getReceivedPacket()
	{//gibt empfangenes packet zurueck -> null falls keins vorhanden, sollte vorher mit PacketAvaliable() geprueft werden
		if(isPacketAvaliable())
		{
			if(isSavePacketAvaliable())
			{
				return getSaveReceivedPacket();
			}
			return getUnsaveReceivedPacket();
		}
		return null;
	}
	
	//-------------------send Packet stuff--------------------------
	
	public boolean sendPacketSave(Packet pack,boolean rebuildPacket)
	{//Senden packet ueber sicheren socket, rebuild sollte nur dann true sein wenn packet neu gebaut werden muss (performance)
		if(m_Connected.get())
		{
			if(rebuildPacket && m_InsertTimestampAutomaticly)
			{
				pack.setTimestamp(getTimestamp());
			}
			return m_ReliablePackethandler.SendPacket(pack, rebuildPacket);
		}
		return false;
	}
	
	public boolean sendPacketSave(Packet pack)
	{//Senden packet ueber sicheren socket, rebuild ist immer true (nicht gut performancetechnisch)
		return sendPacketSave(pack,true);
	}
	
	public boolean sendPacketUnsave(Packet pack,boolean rebuildPacket)
	{//Senden packet ueber unsicheren socket, rebuild sollte nur dann true sein wenn packet neu gebaut werden muss (performance)
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
				return m_Sockets[Sockettypes.UnsaveSocket.getValue()].Send(output);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public boolean sendPacketUnsave(Packet pack)
	{//Senden packet ueber unsicheren socket, rebuild ist immer true (nicht gut performancetechnisch)
		return sendPacketUnsave(pack,true);
	}
	
	public boolean sendPacket(Packet pack,boolean rebuildPacket)
	{//Senden packet ueber sicheren socket, rebuild sollte nur dann true sein wenn packet neu gebaut werden muss (performance)
		return sendPacketSave(pack,rebuildPacket);
	}
	
	public boolean sendPacket(Packet pack)
	{//Senden packet ueber sicheren socket, rebuild ist immer true (nicht gut performancetechnisch)
		return sendPacket(pack,true);
	}

	protected abstract long getTimestamp();
}
