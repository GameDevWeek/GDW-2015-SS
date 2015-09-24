package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 23.09.15.
 *
 * Receives Movement packets and updates physixbody
 */
public class UpdatePhysixServer implements NetworkReceivedNewPacketServerEvent.Listener{


    public UpdatePhysixServer()
    {
        NetworkReceivedNewPacketServerEvent.registerListener(PacketIds.Movement, this);
    }

    /////////////////// SERVER
    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        PhysixBodyComponent phxc = ComponentMappers.physixBody.get(ent);
        try{
            MovementPacket p = (MovementPacket)pack;
            phxc.setLinearVelocity(p.xPos, p.yPos);
            phxc.setAngle(p.rotation);
        }catch (ClassCastException e){}
    }

}
