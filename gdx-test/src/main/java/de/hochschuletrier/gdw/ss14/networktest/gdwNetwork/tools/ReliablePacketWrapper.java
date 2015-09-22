package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.tools;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReliablePacketWrapper
{
	public ByteArrayOutputStream output = new ByteArrayOutputStream();
	
	public ReliablePacketWrapper(byte flag,long packetnumber,ByteArrayOutputStream packetoutput) throws IOException
	{
		DataOutputStream dataoutput = new DataOutputStream(output);
		dataoutput.write(flag);
		dataoutput.writeLong(packetnumber);
		packetoutput.writeTo(dataoutput);
	}
}
