package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.FirePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.ReciveShotPacketClient;
import de.hochschuletrier.gdw.ss15.game.rendering.ZoomingModes;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 24.09.15.
 *
 * Server received Fire Package
 */
public class FireServerListener extends EntitySystem implements NetworkReceivedNewPacketServerEvent.Listener{

    private ServerGame game;
    private static final float damping = 5.f;
    private static final float projectPlayerDistance = 55;
    private static final float NORMAL_SHOOT_POWER = 1000.f;
    private static final float MAX_SHOOT_POWER = 1800.f;
    private static final float SHOT_CONE_DEGREE = 45.f;
    private static final float CHARGED_CONE_DEGREE = 10.f;
    
    // Just to avoid garbage collection
    private static Vector2 dummyVector = new Vector2(); 
    private static Vector2 dummyVector2 = new Vector2();
    
    public FireServerListener(ServerGame game){
        super();
        this.game = game;
        NetworkReceivedNewPacketServerEvent.registerListener(PacketIds.Fire, this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        NetworkReceivedNewPacketServerEvent.unregisterListener(PacketIds.Fire, this);
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        InventoryComponent invc = ComponentMappers.inventory.get(ent);
        
        FirePacket packet = (FirePacket)pack;


        // Should be in [0, 1]
        float channelFactor = packet.channeltime / WeaponComponent.maximumFireTime + 0.0001f;
        
        int shootshards = Math.min(invc.getMetalShards(), WeaponComponent.ShardsPerShot);
        invc.addMetalShards(-shootshards);

        float coneDegree = ZoomingModes.interpolate(GameConstants.ZOOM_MODE, 
                SHOT_CONE_DEGREE, CHARGED_CONE_DEGREE, channelFactor);
        shoot(ent, channelFactor, shootshards, (float) Math.toRadians(coneDegree));
    }
    
    public void shoot(Entity shootingEntity, float channelFactor, int shardNum, float radiansConeDegree) {
        if(shardNum <= 0)
            return;
        
        PhysixBodyComponent physixComp = ComponentMappers.physixBody.get(shootingEntity);
        float angleStep = radiansConeDegree / shardNum;

        // Player should not be able to shoot if he is dead!
        if(ComponentMappers.death.has(shootingEntity))
            return;
        
        boolean shooted = false;

        for(int i = -shardNum / 2; i <= shardNum / 2; ++i) {
            if(i == 0 && shardNum % 2 == 0)
                continue;


            shooted = true;
            float rotation = physixComp.getAngle() + i * angleStep;
            dummyVector.set((float) Math.cos(rotation), (float) Math.sin(rotation));
            
            // unit circle -> no need to normalize
            dummyVector.scl(projectPlayerDistance);
            dummyVector.add(physixComp.getPosition());
            
            int chargePower = (int) (ZoomingModes.interpolate(GameConstants.ZOOM_MODE, 
                    NORMAL_SHOOT_POWER, MAX_SHOOT_POWER, channelFactor));
            Entity projectile = game.createEntity("projectile", dummyVector.x, dummyVector.y);
            createProjectile(projectile, rotation, chargePower);
            
            BulletComponent bullet = projectile.getComponent(BulletComponent.class);
            bullet.playerID = shootingEntity.getComponent(PlayerComponent.class).playerID;
            bullet.rotation = rotation;
            bullet.power = chargePower;
            bullet.playerrotation = physixComp.getAngle() * MathUtils.radiansToDegrees;
            bullet.playerpos.set(physixComp.getPosition());
        }

        if(shooted == true)
        {
            PositionSynchComponent comp = ComponentMappers.positionSynch.get(shootingEntity);
            ReciveShotPacketClient pack = new ReciveShotPacketClient(comp.networkID,1);
            SendPacketServerEvent.emit(pack,true);
        }
    }

    public static void createProjectile(Entity entity, float rotation, int chargePower){
        if(entity.getComponent(PhysixModifierComponent.class) == null){
            entity.add(new PhysixModifierComponent());
        }

        entity.getComponent(PhysixModifierComponent.class).runnables.add(() -> {
            PhysixBodyComponent physixBodyComponent = entity.getComponent(PhysixBodyComponent.class);
            entity.getComponent(BulletComponent.class).playerpos.set(physixBodyComponent.getPosition());

            physixBodyComponent.setAngle(rotation);
            dummyVector2.set((float)Math.cos(physixBodyComponent.getAngle()), (float)Math.sin(physixBodyComponent.getAngle()));
            dummyVector2.scl(chargePower); // No need to normalize since it's a unit circle
            physixBodyComponent.applyImpulse(dummyVector2);

            physixBodyComponent.setLinearDamping(damping);
        });
    }
}
