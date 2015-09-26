package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
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


        //SimplePacket sPack = (SimplePacket) pack;
       // if(sPack.m_SimplePacketId == SimplePacket.SimplePacketId.MetalShardsUpdate.getValue())
        //{
       // }
    }
}
