package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicReference;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.*;

public class DisconnectHandler
{
	MyTimer m_TimoutTimer = new MyTimer();
	MyTimer m_GiveUpTimer = new MyTimer(true);
	UdpSocket m_Socket;
	UdpData m_UdpData = null;
	static final int m_TimeUntilDisconnectResend = 200;
	static final int m_TimeUntilSocketCloses = 2000;
	static final int m_TimeUntilGiveUp = 5000;
	private AtomicReference<DisconnectStatus> m_Status = new AtomicReference<>();
	
	private enum DisconnectStatus
	{
		WaitForAck,
		WaitForFinAck,
		WaitForTimeout_BecauseAckCouldGetLost,
		Finished
	}
	
	public DisconnectHandler(UdpSocket socket,boolean receivedDisconnect)
	{
		this(socket,null,receivedDisconnect);
	}
	
	public DisconnectHandler(UdpSocket socket,UdpData udpdata,boolean receivedDisconnect)
	{
		m_Socket=socket;
		m_UdpData = udpdata;
		m_TimoutTimer.StartCounterMSandUpdate(m_TimeUntilDisconnectResend);
		if(receivedDisconnect)
		{
			m_TimoutTimer.StartCounterandUpdate();
			m_Status.set(DisconnectStatus.WaitForAck);
			SendMessage((byte)1);
		}
		else
		{
			m_TimoutTimer.StartCounterandUpdate();
			m_Status.set(DisconnectStatus.WaitForFinAck);
			SendMessage((byte)0);
		}
	}
	
	public boolean update()
	{
		if(m_Status.get()==DisconnectStatus.Finished)
		{
			return true;
		}
		m_GiveUpTimer.Update();
		if(m_GiveUpTimer.get_CounterMilliseconds()>m_TimeUntilGiveUp)
		{//if the programm closes by disconect
			m_Status.set(DisconnectStatus.Finished);
			return true;
		}
		m_TimoutTimer.Update();
		if(m_Status.get()==DisconnectStatus.WaitForFinAck)
		{
			if(m_TimoutTimer.get_CounterMilliseconds()>=m_TimeUntilDisconnectResend)
			{
				SendMessage((byte)0);
				m_TimoutTimer.StartCounter();
			}
		}
		else if(m_Status.get() == DisconnectStatus.WaitForAck)
		{
			if(m_TimoutTimer.get_CounterMilliseconds()>=m_TimeUntilDisconnectResend)
			{
				SendMessage((byte)1);
				m_TimoutTimer.StartCounter();
			}
		}
		else if(m_Status.get() == DisconnectStatus.WaitForTimeout_BecauseAckCouldGetLost)
		{
			if(m_TimoutTimer.get_CounterMilliseconds()>=m_TimeUntilSocketCloses)
			{
				m_Status.set(DisconnectStatus.Finished);
				return true;
			}
		}
		
		return false;
	}
	
	public void ReceivedDisconnectMessage(ByteArrayInputStream input)
	{
		if(input.available()==1 && m_Status.get() != DisconnectStatus.Finished)
		{
			int flag = input.read();
			if(flag == 0)
			{//Fin
				SendMessage((byte)1);//send FinAck
				m_TimoutTimer.StartCounter();
			}
			else if(flag == 1)
			{//FinAck
				m_Status.set(DisconnectStatus.WaitForTimeout_BecauseAckCouldGetLost);
				SendMessage((byte)2);//send Ack
				m_TimoutTimer.StartCounter();
			}
			else if(flag == 2)
			{//Ack
				m_Status.set(DisconnectStatus.Finished);
			}
		}
	}
	
	private void SendMessage(byte flag)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(UnsaveSocketFlag.Disconnect.getValue());
		out.write(flag);
		if(m_UdpData==null)
		{
			m_Socket.Send(out);
		}
		else
		{
			m_Socket.Send(out,m_UdpData);
		}
	}
}
