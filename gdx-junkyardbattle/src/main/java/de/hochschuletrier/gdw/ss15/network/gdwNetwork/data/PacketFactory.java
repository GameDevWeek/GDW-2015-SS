package de.hochschuletrier.gdw.ss15.network.gdwNetwork.data;

import java.util.HashMap;

public class PacketFactory
{
	private static HashMap<Short,Class< ? extends Packet>> m_Packets = new HashMap<Short,Class< ? extends Packet>>() ;
	
	
	public static Packet createPacket(short id)
	{
		try
		{
			return m_Packets.get(id).newInstance();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void registerPacket(short id, Class< ? extends Packet> packet)
	{
		m_Packets.put(id,packet);
	}

}
