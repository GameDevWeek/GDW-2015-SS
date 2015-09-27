package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.systems.network.TestMovementSystem;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 23.09.15.
 *
 * Receives Movement packets and updates physixbody
 */
public class UpdatePhysixServer extends EntitySystem implements NetworkReceivedNewPacketServerEvent.Listener{
    private final static Vector2 dummyVector = new Vector2();
	
    public UpdatePhysixServer()
    {
        super();
        NetworkReceivedNewPacketServerEvent.registerListener(PacketIds.Movement, this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        NetworkReceivedNewPacketServerEvent.unregisterListener(PacketIds.Movement, this);
    }

    /////////////////// SERVER
    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        PhysixBodyComponent phxc = ComponentMappers.physixBody.get(ent);
        try{
        	
            MovementPacket p = (MovementPacket)pack;
            InventoryComponent inventory = ComponentMappers.inventory.get(ent);
            MoveComponent move = ComponentMappers.move.get(ent);
            
            Vector2 vel = dummyVector.set(p.xPos,p.yPos);

			if( (inventory.getMetalShards()<=inventory.maxMetalShards) && (inventory.getMetalShards()>0) )
            {
            	float invtemp = (float)inventory.getMetalShards()/(float)inventory.maxMetalShards;
            	vel.scl(move.speed-move.speed*(invtemp*0.75f));
            }
            else
            {
        	    vel.scl(move.speed);
            }

            
            phxc.setLinearVelocity(vel);
            //phxc.setAngle(p.rotation);
            phxc.setAngle(p.rotation * MathUtils.degreesToRadians);
            }catch (ClassCastException e){}
    }
   

}
