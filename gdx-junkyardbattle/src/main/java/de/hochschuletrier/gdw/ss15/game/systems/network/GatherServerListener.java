package de.hochschuletrier.gdw.ss15.game.systems.network;

import ch.qos.logback.core.net.SyslogOutputStream;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
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
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.GatherPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
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
        // TODO Auto-generated method stub
        try{
            GatherPacket packet = (GatherPacket)pack;
            float channelTime = packet.channelTime;
            //System.out.println("Sammeln0");
            Entity entity = checkHarvestRayCollision(ent);

            if (entity != null)
                //ent = spieler
                //entity = objekt
            {
                System.out.println("checkHarvestEntity != null");
                //for (int i = 0; i < entity.getComponents().size(); i++) {
                //    System.out.println(entity.getComponents().get(i));
                //}
                if (ComponentMappers.player.has(ent) && ComponentMappers.mineable.has(entity)) {
                    System.out.println("checkHarvestEntity mineable");
                    MiningEvent.emit(ent, entity, channelTime);
                } else if (ComponentMappers.player.has(ent)) {
                    SimplePacket miningPacket = new SimplePacket(SimplePacket.SimplePacketId.MiningPacket.getValue(), 1);
                    SendPacketServerEvent.emit(packet, true);
                }
            }





        }catch(ClassCastException e){}

    }

    private Entity checkHarvestRayCollision(Entity entity){
        //System.out.println("checkHarvestRayCollision");
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

        //System.out.println("playerPos: " + pos);
        //System.out.println("rayPos: " + rayPos);


        RayCastCallback lineOfSightCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point,
                                          Vector2 normal, float fraction) {
                //if(fixture.getBody().getUserData() != null && ((PhysixBodyComponent)fixture.getBody().getUserData()).getEntity() != entity)
                {
                    //System.out.println("setting closestFixture");
                    closestFixture = fixture;
                }
                //System.out.println("PosFixture: " + fixture.getBody().getPosition());
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
