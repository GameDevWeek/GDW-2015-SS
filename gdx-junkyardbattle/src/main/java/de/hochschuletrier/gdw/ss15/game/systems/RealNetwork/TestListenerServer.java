package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 23.09.15.
 */
public class TestListenerServer implements DoNotTouchPacketEvent.Listener
{
    public TestListenerServer()
    {
        DoNotTouchPacketEvent.registerListener(this);
    }
    @Override
    public void onDoNotTouchPacket(Packet pack) {

        System.out.println("Receive packet on server: "+pack.getPacketId());
    }
}
