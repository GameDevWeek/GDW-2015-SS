package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class UdpSocket implements Closeable
{
	static final int MaxDatagramSize = 65507;
	
	private byte m_Buffer[] = new byte [MaxDatagramSize];
	private DatagramSocket m_Socket = null;
	private UdpData m_UdpData = null;
	private boolean m_IsServerSocket;
	private InetAddress m_InetAddress = null;
	
	private String m_Ip;
	private int m_Port;
	
	private static final Charset m_Charset = Charset.forName("UTF-8");
	
	public UdpSocket(int port) throws SocketException
	{
		m_IsServerSocket=true;
		m_Ip = new String();
		m_Port = port;
		m_Socket = new DatagramSocket(m_Port);
	}
	
	public UdpSocket(String ip,int port) throws SocketException, UnknownHostException
	{
		m_IsServerSocket=false;
		m_Ip=ip;
		m_Port = port;
		m_Socket = new DatagramSocket();
		m_InetAddress = InetAddress.getByName(m_Ip);
	}

	public boolean Send(String message,InetAddress destination,int port)
	{
		final byte[] outBuffer = message.getBytes(Charset.forName("UTF-8"));
		DatagramPacket packet = new DatagramPacket(outBuffer, outBuffer.length, destination,port);
		try
		{
			m_Socket.send(packet);
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	public boolean Send(ByteArrayOutputStream datastream,InetAddress destination,int port)
	{
		DatagramPacket packet = new DatagramPacket(datastream.toByteArray(), datastream.size(), destination,port);
		try
		{
			m_Socket.send(packet);
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}

	public boolean Send(ByteArrayOutputStream datastream,UdpData udpdata)//doto maybe syncronize
	{
		DatagramPacket packet = new DatagramPacket(datastream.toByteArray(), datastream.size(), udpdata.m_Adress,udpdata.m_Port);
		try
		{
			m_Socket.send(packet);
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	public boolean Send(String message)
	{
		try
		{
			final byte[] outBuffer = message.getBytes(m_Charset);
			
			DatagramPacket packet;
			if(m_IsServerSocket)
			{
				packet = new DatagramPacket(outBuffer, outBuffer.length, m_UdpData.m_Adress,m_UdpData.m_Port);
			}
			else
			{
				packet = new DatagramPacket(outBuffer, outBuffer.length, m_InetAddress ,m_Port);
			}
			m_Socket.send(packet);
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	public boolean Send(ByteArrayOutputStream datastream)
	{
		try
		{
			DatagramPacket packet;
			if(m_IsServerSocket)
			{
				packet = new DatagramPacket(datastream.toByteArray(), datastream.size(), m_UdpData.m_Adress,m_UdpData.m_Port);
			}
			else
			{
				packet = new DatagramPacket(datastream.toByteArray(), datastream.size(), m_InetAddress,m_Port);
			}
			m_Socket.send(packet);
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	public boolean Send(ByteArrayOutputStream datastream,byte flagbyte)
	{
		try
		{
			DatagramPacket packet;
			
			byte arr[] = datastream.toByteArray();
			arr[0]=flagbyte;
			
			if(m_IsServerSocket)
			{
				packet = new DatagramPacket(arr, datastream.size(), m_UdpData.m_Adress,m_UdpData.m_Port);
			}
			else
			{
				packet = new DatagramPacket(arr, datastream.size(), m_InetAddress,m_Port);
			}
			m_Socket.send(packet);
		}
		catch(IOException ex)
		{
			return false;
		}
		return true;
	}
	
	public String Receive()
	{
		DatagramPacket Packet = new DatagramPacket(m_Buffer, m_Buffer.length);
		try
		{
			m_Socket.receive(Packet);
		}
		catch(IOException ex)
		{
			return new String();
		}
		m_UdpData = new UdpData(Packet.getAddress(), Packet.getPort());
		return new String(m_Buffer,0,Packet.getLength(),m_Charset);
	}
	
	public ByteArrayInputStream ReceiveByte() throws IOException
	{
		DatagramPacket Packet = new DatagramPacket(m_Buffer, m_Buffer.length);
		try
		{
			m_Socket.receive(Packet);
		}
		catch(SocketTimeoutException ex)
		{
			return new ByteArrayInputStream(m_Buffer,0,0);
		}
		m_UdpData = new UdpData(Packet.getAddress(), Packet.getPort());
		ByteArrayInputStream input = new ByteArrayInputStream(m_Buffer, 0, Packet.getLength());
		return input;
	}
	
	public ByteArrayInputStream ReceiveByte(UdpData udpData) throws IOException
	{
		DatagramPacket Packet = new DatagramPacket(m_Buffer, m_Buffer.length);
		try
		{
			m_Socket.receive(Packet);
		}
		catch(SocketTimeoutException ex)
		{
			return new ByteArrayInputStream(m_Buffer,0,0);
		}
		m_UdpData = new UdpData(Packet.getAddress(), Packet.getPort());
		m_UdpData.CopyDataTo(udpData);
		return new ByteArrayInputStream(m_Buffer,0,Packet.getLength());
	}
	
	public String Receive(UdpData udpData) throws IOException
	{
		DatagramPacket Packet = new DatagramPacket(m_Buffer, m_Buffer.length);
		
		try
		{
			m_Socket.receive(Packet);
		}
		catch(SocketTimeoutException ex)
		{
			return new String();
		}
		m_UdpData = new UdpData(Packet.getAddress(), Packet.getPort());
		m_UdpData.CopyDataTo(udpData);
		return new String(m_Buffer,0,Packet.getLength(),m_Charset);
	}
	
	public void setTimeout(int timeout) throws SocketException
	{
		m_Socket.setSoTimeout(timeout);
	}
	
	public DatagramSocket get_DatagramSocket()
	{
		return m_Socket;
	}
	
	public InetAddress get_InetAdress()
	{
		return m_InetAddress;
	}
	
	public UdpData get_UdpData()
	{
		if(m_IsServerSocket)
		{
			return m_UdpData;
		}
		return new UdpData(m_InetAddress,m_Port);
	}

	@Override
	public void close() throws SocketException
	{
		m_Socket.close();
	}
	
	public static void ClearSocket(UdpSocket socket) throws SocketException
	{
		if(socket!=null)
		{
			socket.close();
			socket = null;
		}
	}
}
