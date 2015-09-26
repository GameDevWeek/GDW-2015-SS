package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.*;

public class Timeouthandler
{
	private int m_TimeResendIsAlive = 500;
	private int m_IsAlivePacketSendAgain = 100;
	private int m_Timeouttime = 10000;//10 sekunden bis connection endgülitg aufgegeben
	
	private long m_LastLatenzNS=-1;

	MyTimer m_TimerSendAlive = new MyTimer(true);
	MyTimer m_TimerResendIsAlive = new MyTimer();

	UdpSocket m_Socket;
	//addrinfo * m_Adressinfo;
	byte m_Flagbyte;
	
	public Timeouthandler(UdpSocket socket,byte sendflag)
	{
		m_Socket = socket;
		m_Flagbyte = sendflag;
	}
	
	public Timeouthandler(UdpSocket socket,byte sendflag,int resendIsAlive, int sendAgain, int timeout)
	{
		m_Socket = socket;
		m_Flagbyte = sendflag;
		m_TimeResendIsAlive=resendIsAlive;
		m_IsAlivePacketSendAgain=sendAgain;
		m_Timeouttime = timeout;
	}
	
	public boolean Update() throws IOException 
	{
		m_TimerSendAlive.Update();
		//std::cout<<m_TimerSendAlive.GetCountTimeNS()<<std::endl;;
		if(m_TimerSendAlive.get_CounterMilliseconds()>m_TimeResendIsAlive)
		{
			if(m_TimerSendAlive.get_CounterMilliseconds()>m_Timeouttime)
			{//to long no response
				return false;
			}
			if(m_TimerResendIsAlive.get_CounterNanoseconds()==0)
			{
				m_TimerResendIsAlive.StartCounterMS(m_IsAlivePacketSendAgain);
			}
			m_TimerResendIsAlive.Update();
			if(m_TimerResendIsAlive.get_CounterMilliseconds()>=m_IsAlivePacketSendAgain)
			{
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				DataOutputStream outputdata = new DataOutputStream(output);
				outputdata.writeByte(m_Flagbyte);
				outputdata.writeLong(MyTimer.get_TimestampNanoseconds());
				m_Socket.Send(output);
				
				m_TimerResendIsAlive.StartCounterMS(1);
			}
		}
		return true;
	}
	
	public void ReceivedIsAliveAswer(ByteArrayInputStream input) throws IOException
	{
		if(input.available()==Long.SIZE/8)
		{
			DataInputStream datainput = new DataInputStream(input);
			long oldtimestamp = datainput.readLong();
			m_LastLatenzNS = MyTimer.get_TimestampNanoseconds() - oldtimestamp;
			m_TimerSendAlive.UpdateAndStartCounter();
			m_TimerResendIsAlive.UpdateAndStartCounter();
		}
	}
	
	public long getLastLatenz()
	{
		return m_LastLatenzNS;
	}
}
