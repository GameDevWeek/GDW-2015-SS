package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;



public class ReliablePackethandler
{
	private HashMap<Long, ReliablePacketWrapper> m_UnreliaedPackets = new HashMap<>();
	private int m_PacketTimeout;
	private UdpSocket m_Socket;
	private UdpData m_UdpData = null;
	private Timer m_Timer = new Timer();
	
	long m_LowestPacketNumber = 0;
	long m_ActuallPacketnumber = 1;
	private PriorityQueue<Long> m_AckedPackets = new PriorityQueue<Long>();//thx to Wissenschaftlichem Arbeiten Dijkstra

	public ReliablePackethandler(int packetTimeout,UdpSocket socket,UdpData udpdata)
	{
		m_PacketTimeout = packetTimeout;
		m_Socket = socket;
		m_UdpData = udpdata;
	}
	
	public ReliablePackethandler(int packetTimeout,UdpSocket socket)
	{
		m_PacketTimeout = packetTimeout;
		m_Socket = socket;
	}
	
	public boolean SendPacket(Packet pack,boolean rebuildPacket)
	{
		try
		{
			//System.out.println("Send packet with number:  "+ m_ActuallPacketnumber);
			ReliablePacketWrapper wrapper = new ReliablePacketWrapper((byte)SaveSocketFlag.Packet.getValue(), m_ActuallPacketnumber, pack.getOutputstream(rebuildPacket));
			Send(wrapper.output);
			synchronized (m_UnreliaedPackets)
			{
				m_UnreliaedPackets.put(m_ActuallPacketnumber,wrapper);
			}
			
			m_Timer.schedule(new MyTimerTask(m_ActuallPacketnumber), m_PacketTimeout);
			
			m_ActuallPacketnumber++;
		}
		catch (IOException ex)
		{
			//System.out.println("Stream Fehler biem senden im reliiable packethandler");
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void PacketTimedOut(long packetnumber)
	{
		ReliablePacketWrapper wrapper;
		synchronized (m_UnreliaedPackets)
		{
			wrapper = m_UnreliaedPackets.get(packetnumber);
		}
		if(wrapper!=null)
		{
			//System.out.println("Missed acke resend packet");
			Send(wrapper.output);
			m_Timer.schedule(new MyTimerTask(m_ActuallPacketnumber), m_PacketTimeout);
		}
	}
	
	private void Send(ByteArrayOutputStream stream)
	{
		if(m_UdpData==null)
		{
			m_Socket.Send(stream);
		}
		else
		{
			m_Socket.Send(stream,m_UdpData);
		}
	}
		
	public void ReceivedAck(long PacketNumber)
	{
		//System.out.println("Received Ackk: "+PacketNumber);
		synchronized (m_UnreliaedPackets)
		{
			m_UnreliaedPackets.remove(PacketNumber);
		}
	}
	
	public boolean ReceivedPacketShouldUse(long packetNumber) throws IOException
	{
		//System.out.println("Send akc packet: "+packetNumber);
		SendAck(packetNumber);
		if(packetNumber<=m_LowestPacketNumber)
		{//packet allready used
			//System.out.println("Beretis genutztes packet verwofen: "+packetNumber);
			return false;
		}
		else
		{
			if(m_AckedPackets.contains(packetNumber))
			{//packet allready used
				return false;
			}
			else
			{
				m_AckedPackets.add(packetNumber);//TODO if fuer (optimierung)
				while(m_AckedPackets.peek()==m_LowestPacketNumber+1)
				{
					m_LowestPacketNumber++;
					//System.out.println("neues lowest packet: "+m_LowestPacketNumber);
					m_AckedPackets.remove();
					
					if(m_AckedPackets.peek() == null)
					{
						break;
					}
				}
				//System.out.println("Use received packet ->");
				return true;
			}
		}
	}
	
	public void SendAck(long packetnumber) throws IOException
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream dataoutput = new DataOutputStream(output);
		dataoutput.write(SaveSocketFlag.Ack.getValue());
		dataoutput.writeLong(packetnumber);//TODO senden mehr headerdaten
		Send(output);
		//System.out.println("Sende Ack");
	}
	
	static public int getReliableHeadSize()
	{
		return Long.SIZE/8;
	}
	
	class MyTimerTask extends TimerTask
	{
		private long m_Packetnumber;
		public MyTimerTask(long packetnumber)
		{
			super();
			m_Packetnumber = packetnumber;
		}
		
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			PacketTimedOut(m_Packetnumber);
		}
	};
}

