package gdwNetwork.basic;

import gdwNetwork.data.Packet;

public interface SocketListener
{
	void receivedPacket(Packet packet,boolean receivedSave);
}