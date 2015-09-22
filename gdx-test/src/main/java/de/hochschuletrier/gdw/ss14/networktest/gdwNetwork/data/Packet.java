package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet
{	
	private short m_PacketId;
	
	//output
	private ByteArrayOutputStream m_Output;
	private DataOutputStream m_Dataoutput;
	private long m_Timestmp;
	
	public Packet(short packetid)
	{
		m_PacketId = packetid;
		m_Timestmp = 0;
	}
	
	public Packet(short packetid, long timestamp)
	{
		m_PacketId = packetid;
		m_Timestmp = timestamp;
	}
	
	public short getPacketId()
	{
		return m_PacketId;
	}
	
	public void setTimestamp(long timestamp)
	{
		//System.out.println("timestamp set to: "+timestamp);
		m_Timestmp = timestamp;
	}
	
	public long getTimestamp()
	{
		return m_Timestmp;
	}
	
	public ByteArrayOutputStream getOutputstream(boolean rebuild) throws IOException
	{
		if(rebuild = true)
		{
			buildPacket();
		}
		return m_Output;
	}
	
	private void buildPacket() throws IOException
	{
		m_Output = new ByteArrayOutputStream();
		m_Dataoutput = new DataOutputStream(m_Output);
		m_Dataoutput.writeShort(m_PacketId);
		m_Dataoutput.writeLong(m_Timestmp);
		this.pack(m_Dataoutput);
	}
	
	public void buildPacketOnData(DataInputStream input) throws IOException
	{//intneal for clientsockets
		this.unpack(input);
	}
	
	protected abstract void pack(DataOutputStream dataOutput) throws IOException;
	
	protected abstract void unpack(DataInputStream input) throws IOException;
	
	public abstract int getSize();
	
	public static int getMainheadersize()
	{
		return (Short.SIZE + Long.SIZE) / 8;
	}
	
}
