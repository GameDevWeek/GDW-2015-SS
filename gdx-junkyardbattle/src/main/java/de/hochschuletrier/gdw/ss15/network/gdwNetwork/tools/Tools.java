package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;


public class Tools
{
	@SuppressWarnings("resource")
	public static void SystemPause()
	{
		new java.util.Scanner(System.in).nextLine();
	}
	
	public static void PrintException(Exception ex)
	{
	}
	
	public static void Sleep(int millieseconds)
	{
		try
		{
			Thread.sleep(millieseconds);
		}
		catch(InterruptedException ex)
		{
			PrintException(ex);
		}
	}

	public static boolean IntToBool(int i)
	{
		return i==1;
	}

	public static int BoolToInt(boolean i)
	{
		if(i)
			return 1;
		return 0;
	}

}
