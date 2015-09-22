package de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums;

public enum UnsaveSocketFlag
{
	Packet((int)0),
	Login1((int)1),
	Login2((int)2),
	IsAlive((int)3),
	Disconnect((int)4);
	
	private final int m_Value;
	private UnsaveSocketFlag(int value)
	{
		m_Value=value;
	}
	
	public int getValue()
	{
		return m_Value;
	}
}