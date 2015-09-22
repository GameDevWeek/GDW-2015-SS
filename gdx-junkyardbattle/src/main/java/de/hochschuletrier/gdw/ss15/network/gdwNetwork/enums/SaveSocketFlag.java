package de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums;

public enum SaveSocketFlag
{
	Packet((int)0),
	IsAlive((int)1),
	Ack((int)2);
	
	private final int m_Value;
	private SaveSocketFlag(int value)
	{
		m_Value=value;
	}
	
	public int getValue()
	{
		return m_Value;
	}
}