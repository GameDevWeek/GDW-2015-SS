package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.InterpolatePositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.MovementPacket;
import de.hochschuletrier.gdw.ss15.game.utils.Timer;
import de.hochschuletrier.gdw.ss15.game.utils.TimerSystem;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 23.09.15.
 */
public class UpdatePhysixSystem extends IteratingSystem
        implements NetworkReceivedNewPacketClientEvent.Listener{

    Timer timer = new Timer(200); // 200 ms timer


    public UpdatePhysixSystem(TimerSystem timerSystem)
    {
        super(Family.all(PhysixBodyComponent.class, InterpolatePositionComponent.class).get(), GameConstants.PRIORITY_UPDATE_PHYSIX);
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.EntityUpdate, this);
    }


    /////////////////// CLIENT
    /////// send every x miliseconds the player velocity and angle to the server
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    	
    	InterpolatePositionComponent interp = ComponentMappers.interpolate.get(entity);
    	PhysixBodyComponent body = ComponentMappers.physixBody.get(entity);
    	
    	if(interp.secondPos != null && interp.firstPos != null ){
    		body.setPosition(	Interpolation.linear.apply(interp.firstPos.xPos, interp.secondPos.xPos, interp.sumDeltaTime),
    							Interpolation.linear.apply(interp.firstPos.yPos, interp.secondPos.yPos, interp.sumDeltaTime));
        }
    	if(interp.sumDeltaTime < 1 ){
    		interp.sumDeltaTime += deltaTime;
    	}
    }

  
    @Override
    public void onReceivedNewPacket(Packet pack, Entity entity) {
        try {

            EntityUpdatePacket p = (EntityUpdatePacket) pack;
            PhysixBodyComponent phxc = entity.getComponent(PhysixBodyComponent.class);
            if (phxc != null) {
                phxc.setPosition(p.xPos, p.yPos);
                phxc.setAngle(p.rotation * MathUtils.degreesToRadians);
            }
            }catch(ClassCastException ex){
            }
  /*
    //System.out.println("used new rotation: "+p.rotation);
    InterpolatePositionComponent interp = entity.getComponent(InterpolatePositionComponent.class);
    
    
    if(interp.secondPos == null && interp.firstPos == null ){
    	interp.firstPos = p;
    	return;
    } else if (interp.secondPos == null){
    	if(interp.firstPos.getTimestamp() > p.getTimestamp()){
    		interp.secondPos = interp.firstPos;
    		interp.firstPos = p;
    	} else{
    		interp.secondPos = p;
    	}
    	return;
    }
    
    if(p.getTimestamp() > interp.secondPos.getTimestamp()){
    	interp.firstPos = interp.secondPos;
    	interp.secondPos = p;
    	phxc.getPosition().x = interp.firstPos.xPos;
    	phxc.getPosition().y = interp.firstPos.yPos;
    	interp.sumDeltaTime = 0;
    	return;
    }
    
    if(p.getTimestamp() > interp.firstPos.getTimestamp()) {
    	interp.firstPos = p;
    	phxc.getPosition().x = interp.firstPos.xPos;
    	phxc.getPosition().y = interp.firstPos.yPos;
    	interp.sumDeltaTime = 0;
    	return;
    }
    
}catch(ClassCastException ex){}
*/
    }
}
