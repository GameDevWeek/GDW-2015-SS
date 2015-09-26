package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 23.09.15.
 */
public class TestListenerServer implements NetworkReceivedNewPacketServerEvent.Listener
{
    public TestListenerServer()
    {
        NetworkReceivedNewPacketServerEvent.registerListener(PacketIds.Movement,this);
    }
    @Override
    public void onReceivedNewPacket(Packet pack,Entity ent) {

        //System.out.println("Received new Movement");
    }
}
