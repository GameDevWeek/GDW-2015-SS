package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 23.09.15.
 */
public class TestListenerClient implements DoNotTouchPacketEvent.Listener
{
    public TestListenerClient()
    {
        DoNotTouchPacketEvent.registerListener(this);
    }
    @Override
    public void onDoNotTouchPacket(Packet pack) {
        //System.out.print("Called");

        //System.out.println("Packet: "+pack.getPacketId());

        //SimplePacket sPack = (SimplePacket) pack;
       // if(sPack.m_SimplePacketId == SimplePacket.SimplePacketId.MetalShardsUpdate.getValue())
        //{
       // }
    }
}
