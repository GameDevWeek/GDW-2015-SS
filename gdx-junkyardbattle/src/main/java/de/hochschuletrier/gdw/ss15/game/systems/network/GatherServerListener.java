package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.events.MiningEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.GatherComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.GatherPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.ReciveShotPacketClient;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class GatherServerListener extends EntitySystem implements NetworkReceivedNewPacketServerEvent.Listener{

    private PhysixSystem physixSystem;
    private Fixture closestFixture;

    public GatherServerListener(PhysixSystem physixSystem){
        super();
        this.physixSystem = physixSystem;
        NetworkReceivedNewPacketServerEvent.registerListener(PacketIds.Gather, this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        NetworkReceivedNewPacketServerEvent.unregisterListener(PacketIds.Gather, this);
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
    	//ent = spieler
        //entity = objekt
        try{
            GatherPacket packet = (GatherPacket)pack;


            PositionSynchComponent comp = ComponentMappers.positionSynch.get(ent);
            ReciveShotPacketClient packett = new ReciveShotPacketClient(comp.networkID,2);
            SendPacketServerEvent.emit(packett,true);



            float channelTime = packet.channelTime;
            Entity entity = checkHarvestRayCollision(ent);
            GatherComponent gatherComp = ComponentMappers.gather.get(ent);
            if(ComponentMappers.gather.has(ent))
            {
            	if(entity == null || !ComponentMappers.mineable.has(entity) || channelTime == 0f)
            	{
            		gatherComp.lastEntity = null;
            		gatherComp.currentGatheringTime = 0.0f;
            		gatherComp.mining = false;
            	}
            }
            
            
            if (entity != null)
            {
                if (ComponentMappers.player.has(ent) && ComponentMappers.mineable.has(entity)) {
                	if(entity != gatherComp.lastEntity)
                	{
                		gatherComp.lastEntity = entity;
                		gatherComp.currentGatheringTime = 0.0f;
                		gatherComp.mining = false;
                	}
                	
                	gatherComp.mining = true;
                	gatherComp.currentGatheringTime += channelTime;
                    MiningEvent.emit(ent, entity, gatherComp.currentGatheringTime, channelTime);
                }/* else if (ComponentMappers.player.has(ent)) {
                    SimplePacket miningPacket = new SimplePacket(SimplePacket.SimplePacketId.MiningPacket.getValue(), 1);
                    SendPacketServerEvent.emit(packet, true);
                }*/
            }





        }catch(ClassCastException e){}

    }

    private Entity checkHarvestRayCollision(Entity entity){
        Entity hitEntity = null;

        //player position
        Vector2 pos =  ComponentMappers.physixBody.get(entity).getBody().getPosition();

        //end of ray position
        float rotation = entity.getComponent(PhysixBodyComponent.class).getAngle();
        Vector2 rayPos = new Vector2((float)Math.cos(rotation), (float)Math.sin(rotation));
        rayPos.nor();
        rayPos.scl(GameConstants.GATHERING_RANGE);
        physixSystem.toBox2D(rayPos, rayPos);
        rayPos.add(pos);



        RayCastCallback lineOfSightCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point,
                                          Vector2 normal, float fraction) {
                //if(fixture.getBody().getUserData() != null && ((PhysixBodyComponent)fixture.getBody().getUserData()).getEntity() != entity)
                {
                    closestFixture = fixture;
                }
                return fraction;
            }
        };

        closestFixture = null;
        
        physixSystem.getWorld().rayCast(lineOfSightCallback,
                pos, rayPos);

        if(closestFixture != null && closestFixture.getBody().getUserData() instanceof PhysixBodyComponent)
        {

            hitEntity = ((PhysixBodyComponent)closestFixture.getBody().getUserData()).getEntity();
        }
        return hitEntity;
    }

}
