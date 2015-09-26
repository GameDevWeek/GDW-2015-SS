package de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools;


public class Tools
{
	@SuppressWarnings("resource")
	public static void SystemPause()
	{
		System.out.println("Press Any Key To Continue...");
		new java.util.Scanner(System.in).nextLine();
	}
	
	public static void PrintException(Exception ex)
	{
		System.out.println("Exception geflogen ->");
		System.out.println(ex.getMessage());
		System.out.println(ex.getStackTrace());
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
