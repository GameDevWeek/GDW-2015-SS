package de.hochschuletrier.gdw.ss15.game.systems.network;

import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 23.09.15.
 */
public class TestListenerClient implements NetworkReceivedNewPacketClientEvent.Listener
{
    public TestListenerClient()
    {
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.Position,this);
    }
    @Override
    public void onReceivedNewPacket(Packet pack) {
        System.out.println("Received new Position");
    }
}
