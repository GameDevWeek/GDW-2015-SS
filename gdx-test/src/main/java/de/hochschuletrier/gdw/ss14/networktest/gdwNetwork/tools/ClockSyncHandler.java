package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools;

import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.enums.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.basic.*;
import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClockSyncHandler
{
	private MyTimer m_ToSyncTimer;
	private UdpSocket m_ClockSocket;
	
	private static final int m_TimeNextClockSnySend = 150;
	private static final int m_TimeUntilTimeout = 2000;
	private static final int m_ClockSyncAnswerSize = (Long.SIZE*2+Byte.SIZE)/8;
	private boolean m_ByClockSync;
	private static final short m_ClockSyncNumber = 5;
	private short m_ActualClockSyncNumber;
	private MyTimer m_ClockTimer = new MyTimer();
	private MyTimer m_TimoutTimer = new MyTimer();
	private byte m_ActualSyncCount=0;
	
	private long m_CalculatedDifference;
	
	private UdpData m_UdpData = new UdpData();//data form server <- here so not evry update new alocated
	
	public ClockSyncHandler(UdpSocket clocksocket)
	{
		m_ClockSocket = clocksocket;
		m_ByClockSync=false;
	}
	
	public boolean StartNewSynconisation(MyTimer timer)
	{
		if(m_ByClockSync==true)
		{
			return false;
		}
		m_ByClockSync = true;
		m_ToSyncTimer = timer;
		if(m_ActualSyncCount==255)
		{
			m_ActualSyncCount=0;
		}
		else
		{
			m_ActualSyncCount++;
		}
		m_TimoutTimer.StartCounterandUpdate();
		m_ClockTimer.StartCounterandUpdate();
		return true;
	}
	
	public int Update() throws IOException
	{//returns true if finished
		m_TimoutTimer.Update();
		if(m_TimoutTimer.get_CounterMilliseconds()>m_TimeUntilTimeout)
		{
			System.out.println("Clocksync timed out");
			return -1;
		}
		
		m_ClockTimer.Update();
		if(m_ClockTimer.get_CounterMilliseconds()>m_TimeNextClockSnySend)
		{
			//System.out.println("send sync request");
			m_ClockTimer.StartCounter();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			DataOutputStream dataoutput = new DataOutputStream(output);
			dataoutput.writeByte(m_ActualSyncCount);
			dataoutput.writeLong(MyTimer.get_TimestampNanoseconds());
			m_ClockSocket.Send(output);
		}
		
		ByteArrayInputStream input = m_ClockSocket.ReceiveByte(m_UdpData);
		if(input.available()==m_ClockSyncAnswerSize && m_UdpData.equals(m_ClockSocket.get_UdpData()))
		{
			long actualtimestamp = MyTimer.get_TimestampNanoseconds();
			//System.out.println("Received anser of clock request");
			DataInputStream inputdata = new DataInputStream(input);
			byte synccount = inputdata.readByte();
			//System.out.println("Synccoutn: "+synccount);
			
			if(synccount == m_ActualSyncCount)
			{
				//System.out.println("Received anser of clock request sdkflöasdkf");
				long mytimestamp = inputdata.readLong();
				long servertimestamp = inputdata.readLong();
				
				long latenz = (actualtimestamp-mytimestamp);
				long calculatedservertime = servertimestamp - latenz/2;// %2 because time to send and back
				long timedistanz = calculatedservertime - MyTimer.get_TimestampNanoseconds();
				
				//System.out.println("Received count: "+m_ActualClockSyncNumber);
				//System.out.println("Mytimestamp: "+mytimestamp);
				//System.out.println("Servertimestamp: "+servertimestamp);
				//System.out.println("Actualtime: "+MyTimer.get_TimestampNanoseconds());
				//System.out.println("Calculated timestamp: "+calculatedservertime);
				//System.out.println("Timedistanz: "+timedistanz);
				//System.out.println("Latenz: "+latenz);
				
				
				
				//System.out.println("Differenzum einene timer: "+m_CalculatedDifference);
				m_CalculatedDifference = (m_CalculatedDifference * m_ActualClockSyncNumber +timedistanz) / (m_ActualClockSyncNumber+1);
				
				m_ActualClockSyncNumber++;
				if(m_ActualClockSyncNumber == m_ClockSyncNumber)
				{
					m_ToSyncTimer.StartCounterandUpdate(MyTimer.get_TimestampNanoseconds()+m_CalculatedDifference);
					
					System.out.println("CalculatedDifference: "+m_CalculatedDifference);
					//System.out.println("Test real snytime: "+(MyTimer.get_TimestampNanoseconds()-m_ToSyncTimer.get_CounterNanoseconds()));
					System.out.println("Serverzeit synconisierung abgeschlossen");
					//System.out.println("Servertime: "+m_ToSyncTimer.get_CounterMilliseconds());
					//System.out.println("Differenzum einene timer: "+m_CalculatedDifference);
					return 1;
				}
			}
		}
		return 0;
	}
}
