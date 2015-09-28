package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
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

    }
}
