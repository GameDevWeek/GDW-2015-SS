package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.enums;

public enum Sockettypes
{
	SaveSocket((short)0),
	UnsaveSocket((short)1),
	ClockSocket((short)2);
	
	private final short m_Value;
	private Sockettypes(short value)
	{
		m_Value=value;
	}
	
	public short getValue()
	{
		return m_Value;
	}
}