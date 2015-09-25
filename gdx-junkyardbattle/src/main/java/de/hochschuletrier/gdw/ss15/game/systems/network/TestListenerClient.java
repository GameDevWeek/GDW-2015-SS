package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
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
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.EntityUpdate,this);
    }
    @Override
    public void onReceivedNewPacket(Packet pack,Entity ent) {
        //System.out.println("Received new Position for entity");
    }
}