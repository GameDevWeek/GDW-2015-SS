package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.FirePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SpawnBulletPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 24.09.15.
 *
 * Server received Fire Package
 */
public class FireServerListener extends EntitySystem implements NetworkReceivedNewPacketServerEvent.Listener{

    private ServerGame game;
    private static final float power = 4000;
    private static final float damping = 10;
    private static final float projectPlayerDistance = 55;


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
        PhysixBodyComponent phxc = ComponentMappers.physixBody.get(ent);
        InventoryComponent invc = ComponentMappers.inventory.get(ent);
        
        //System.out.print("akjsdklasdjlaskdashkdjashkjdahjksad");
        try{
            FirePacket packet = (FirePacket)pack;

            float p = packet.channeltime / WeaponComponent.maximumFireTime + 0.0001f;
            float scatter = WeaponComponent.maximumScattering * (1.05f-p);
            Vector2 dir = Vector2.Zero;
//            System.out.printf("\n fireing: %.2f -> scattering: %.2f \n", packet.channeltime, scatter);
            for (int i = 0; i < WeaponComponent.ShardsPerShot; ++i) {
                if(invc.getMetalShards() <= 0){
//                    System.out.println("not enough metal shards ("+invc.getMetalShards()+")");
                    return;
                }
//                System.out.println("shard shot");

                Vector2 playerLookDirection = new Vector2((float) Math.cos(phxc.getAngle()), (float) Math.sin(phxc.getAngle()));

                // create projectile
                //Components: Bullet, Damage, Physix
                //physix component
                playerLookDirection.nor().scl(projectPlayerDistance);
                EntityFactoryParam param = new EntityFactoryParam();
                Vector2 startPosition = playerLookDirection.add(phxc.getPosition()); // dir.setLength(projectPlayerDistance).add(phxc.getPosition());


                invc.addMetalShards(-1);

                //System.out.println(invc.getMetalShards());

                Entity projectile = game.createEntity("projectile", startPosition.x, startPosition.y);

                float rotation = (float) (phxc.getAngle() + (Math.random() - 0.5f) * scatter);
                createProjectile(projectile, rotation);
                SpawnBulletPacket spawnBulletPacket = new SpawnBulletPacket();
                spawnBulletPacket.position = new Vector2(startPosition.x, startPosition.y);
                spawnBulletPacket.rotation = rotation;
                SendPacketServerEvent.emit(spawnBulletPacket, true);
            }
            }catch (ClassCastException e){}
    }

    public static void createProjectile(Entity entity, float rotation){
        if(entity.getComponent(PhysixModifierComponent.class) == null){
            entity.add(new PhysixModifierComponent());
        }

        entity.getComponent(PhysixModifierComponent.class).runnables.add(() -> {
            PhysixBodyComponent physixBodyComponent = entity.getComponent(PhysixBodyComponent.class);
            entity.getComponent(BulletComponent.class).startpos = new Vector2(physixBodyComponent.getPosition());

            physixBodyComponent.setAngle(rotation);
            Vector2 v2 = new Vector2((float)Math.cos(physixBodyComponent.getAngle()), (float)Math.sin(physixBodyComponent.getAngle()));
            v2.nor().scl(power);
            entity.getComponent(PhysixBodyComponent.class).applyImpulse(v2);
//            physixBodyComponent.setLinearDamping(damping);
        });
    }
}
