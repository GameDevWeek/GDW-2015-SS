package de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.basic;

import de.hochschuletrier.gdw.ss14.networktest.gdwNetwork.data.*;

public interface SocketListener
{
	void receivedPacket(Packet packet,boolean receivedSave);
}