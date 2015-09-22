package de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic;

import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.*;

public interface SocketListener
{
	void receivedPacket(Packet packet,boolean receivedSave);
}