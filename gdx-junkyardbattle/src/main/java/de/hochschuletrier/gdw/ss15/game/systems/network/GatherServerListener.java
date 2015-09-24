package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.GatherPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class GatherServerListener implements NetworkReceivedNewPacketServerEvent.Listener{

	private PhysixSystem physixSystem;
	private Fixture closestFixture;

    public GatherServerListener(PhysixSystem physixSystem){
        this.physixSystem = physixSystem;
        NetworkReceivedNewPacketServerEvent.registerListener(PacketIds.Gather, this);
    }
    
	@Override
	public void onReceivedNewPacket(Packet pack, Entity ent) {
		// TODO Auto-generated method stub
		try{
			GatherPacket packet = (GatherPacket)pack;
			float channelTime = packet.channelTime;
			
			Entity entity = checkHarvestRayCollision(ent);
			if(entity != null)
			{
				//Gamelogik
				//Strahl ein Entity getroffen
				//prüfen ob Abbauschuss ein Objekt mit der Komponente Mineable getroffen hat etc.
			}
			
		}catch(ClassCastException e){}
		
	}
	
	private Entity checkHarvestRayCollision(Entity entity){
    	Entity hitEntity = null;
    	float range = 5f;
    	
    	//player position
    	Vector2 pos =  ComponentMappers.physixBody.get(entity).getPosition();
    	
    	//end of ray position
    	float rotation = entity.getComponent(PositionComponent.class).rotation;
    	Vector2 rayPos = new Vector2((float)Math.cos(rotation), (float)Math.sin(rotation));
    	rayPos.nor();
    	rayPos.scl(range);
    	rayPos.add(pos);
    	
        RayCastCallback lineOfSightCallback = new RayCastCallback() {
                 	
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point,
                    Vector2 normal, float fraction) {
            	closestFixture = fixture;
                return fraction;
            }
        };
        
    	closestFixture = null;
        physixSystem.getWorld().rayCast(lineOfSightCallback,
                pos, rayPos);
    
        if(closestFixture.getBody().getUserData() instanceof PhysixBodyComponent)
        {
        	hitEntity = ((PhysixBodyComponent)closestFixture.getBody().getUserData()).getEntity();
        }
		return hitEntity;
	}

}
