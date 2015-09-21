package gdwNetwork.tools;

public class MyTimer
{
	boolean m_Counting=false;
	long m_LastTime = 0;
	long m_TimeLastFrame = 0;
	long m_CountTime;
	
	public MyTimer()
	{
		Update();
	}
	
	public MyTimer(boolean counting)
	{
		Update();
		m_Counting =counting;
		m_CountTime=0;
	}
	
	public MyTimer(long StartTime)
	{
		Update();
		m_Counting=true;
		m_CountTime = StartTime;
	}
	
	public void Update()
	{
		long Actualtime = System.nanoTime();
		m_TimeLastFrame = Actualtime- m_LastTime;
		m_LastTime = Actualtime;
		
		if(m_Counting==true)
		{
			m_CountTime+=m_TimeLastFrame;
		}
	}
	
	public void UpdateAndStartCounter()
	{
		Update();
		StartCounter();
	}
	
	public long get_FrameNanosecond()
	{
		return m_TimeLastFrame;
	}
	
	public double get_FrameMilliseconds()
	{
		return (double)m_TimeLastFrame/1000000000;
	}
	
	public double get_FrameSeconds()
	{
		return (double)m_TimeLastFrame/1000000;
	}
	
	static public long get_TimestampNanoseconds()
	{
		return System.nanoTime();
	}
	
	public long get_CounterNanoseconds()
	{
		return m_CountTime;
	}
	
	public double get_CounterMilliseconds()
	{
		return (double)m_CountTime/1000000;
	}
	
	public boolean IsCounting()
	{
		return m_Counting;
	}
	
	public void StartCounter(long start)
	{
		m_Counting=true;
		m_CountTime=start;
	}
	
	public void StartCounterandUpdate(long start)
	{
		Update();
		m_Counting=true;
		m_CountTime=start;
	}
	
	public void StartCounterMS(double start)
	{
		m_Counting=true;
		m_CountTime=(long)(start*1000000);
	}
	
	public void StartCounterMSandUpdate(double start)
	{
		Update();
		m_Counting=true;
		m_CountTime=(long)(start*1000000);
	}
	
	public void StartCounter()
	{
		m_Counting=true;
		m_CountTime=0;
	}
	
	public void StartCounterandUpdate()
	{
		Update();
		m_Counting=true;
		m_CountTime=0;
	}
	
	public void ResetTimer()
	{
		m_CountTime=0;
	}
}
