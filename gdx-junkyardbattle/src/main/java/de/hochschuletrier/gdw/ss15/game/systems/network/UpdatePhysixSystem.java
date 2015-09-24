package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.Main;
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


public class UpdatePhysixSystem extends IteratingSystem
        implements NetworkReceivedNewPacketClientEvent.Listener{

    Timer timer = new Timer(200); // 200 ms timer
	long lastTimestamp = 0;

    public UpdatePhysixSystem(TimerSystem timerSystem)
    {
        super(Family.all(PhysixBodyComponent.class, InterpolatePositionComponent.class).get(), GameConstants.PRIORITY_UPDATE_PHYSIX);
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.EntityUpdate, this);
        
    }

    
    

    /////////////////// CLIENT
    /////// send every x miliseconds the player velocity and angle to the server
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
  
    }

  
    @Override
    public void onReceivedNewPacket(Packet pack, Entity entity) {
    	 
    	try {

    		EntityUpdatePacket p = (EntityUpdatePacket) pack;
            PhysixBodyComponent phxc = entity.getComponent(PhysixBodyComponent.class);
            if (phxc != null) {
            	if(p.getTimestamp()>lastTimestamp)
                {
                	lastTimestamp = p.getTimestamp();
                	float calcTimestamp = (float) (Main.getInstance().getClientConnection().getSocket().getTimeDifferenceMS(p.getTimestamp())/1000);
                	//phxc.setPosition(p.xPos + calcTimestamp*p.velocityX , p.yPos + calcTimestamp*p.velocityY);
                	phxc.setPosition(p.xPos , p.yPos);
                	//phxc.setPosition(pos);
                	//.setLinearVelocity(, );
                	phxc.setLinearVelocity(p.velocityX,p.velocityY);

                    phxc.setAngle(p.rotation*MathUtils.degreesToRadians);
                	
                	//System.out.println("velo" +p.velocityX+ " "+ p.velocityY);
                    
                }
            }
            }catch(ClassCastException ex){
            }

    
    }
    
}
