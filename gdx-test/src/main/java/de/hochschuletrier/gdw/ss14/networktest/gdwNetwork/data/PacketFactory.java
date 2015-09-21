package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data;

import java.util.HashMap;

public class PacketFactory
{
	private static HashMap<Short,Class< ? extends Packet>> m_Packets = new HashMap<Short,Class< ? extends Packet>>() ;
	
	
	public static Packet createPacket(short id)
	{
		//System.out.println("Packet angefrage mit id: "+id);
		try
		{
			//System.out.println("Packet vorhanden");
			return m_Packets.get(id).newInstance();
		}
		catch(Exception ex)
		{
			System.out.println("Packet konnte nicht erstellt oder nicht vorhandern id: "+id+"  -> werden ein leherer kostrukt wird benoetigt/packet muss bei factory angemeldet werden");
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void registerPacket(short id, Class< ? extends Packet> packet)
	{
		System.out.println("Neuen packet in der Factory mit id: "+id);
		m_Packets.put(id,packet);
	}

}
