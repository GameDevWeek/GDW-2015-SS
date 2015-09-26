package de.hochschuletrier.gdw.ss15.game.systems.RealNetwork;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by lukas on 23.09.15.
 */
public class TestListenerClient implements NetworkReceivedNewPacketClientEvent.Listener
{
    public TestListenerClient()
    {
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.Simple,this);
    }
    @Override
    public void onReceivedNewPacket(Packet pack,Entity ent) {
        //System.out.print("Called");
        SimplePacket sPack = (SimplePacket) pack;
        if(sPack.m_SimplePacketId == SimplePacket.SimplePacketId.MetalShardsUpdate.getValue())
        {
            //System.out.println("Metal chards Update: "+sPack.m_Moredata);
        }
    }
}
