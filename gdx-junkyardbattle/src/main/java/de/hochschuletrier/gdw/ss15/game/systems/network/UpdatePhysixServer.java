package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
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

	
    public UpdatePhysixServer()
    {
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
        	//System.out.println("Received rotation"+ p.rotation);
            InventoryComponent inventory = new InventoryComponent();
            //InventoryComponent inventory = ComponentMappers.inventory.get(ent);
            MoveComponent move = ComponentMappers.move.get(ent);
            
            Vector2 vel = new Vector2(p.xPos,p.yPos);

           // System.out.println("Base : "+vel);
			if(inventory.getMetalShards()<=700 && inventory.getMetalShards()>0)
            {
            	float invtemp = inventory.getMetalShards()/700;
            	
            	vel.scl(move.speed-move.speed*(invtemp*0.75f));
            }
            else
            {
        	    vel.scl(move.speed);
            }

            //System.out.println(vel);
            
            phxc.setLinearVelocity(vel);
            //phxc.setAngle(p.rotation);
            phxc.setAngle(p.rotation * MathUtils.degreesToRadians);
            }catch (ClassCastException e){}
    }
   

}
