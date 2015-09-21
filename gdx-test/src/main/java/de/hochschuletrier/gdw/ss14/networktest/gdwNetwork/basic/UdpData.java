package gdwNetwork.basic;

import java.net.InetAddress;

public class UdpData
{
	public InetAddress m_Adress;
	public int m_Port;
	
	public UdpData()
	{
		m_Port=-1;
		m_Adress=null;
	}
	
	public UdpData(InetAddress adress,int port)
	{
		m_Adress = adress;
		m_Port = port;
	}
	
	public void CopyDataTo(UdpData data)
	{
		data.m_Adress=m_Adress;
		data.m_Port=m_Port;
	}
	
	public InetAddress get_InetAdress()
	{
		return m_Adress;
	}
	
	public int get_Port()
	{
		return m_Port;
	}
	
	 @Override
	public int hashCode()
	{
		return m_Port+m_Adress.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof UdpData))
		{
			return false;
		}
		else
		{
			UdpData data = (UdpData)obj;
			return data.m_Port == m_Port && data.m_Adress.equals(m_Adress);
		}
	}
}
