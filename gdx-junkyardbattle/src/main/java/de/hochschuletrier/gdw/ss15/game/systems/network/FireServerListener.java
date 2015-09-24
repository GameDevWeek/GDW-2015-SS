package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.FirePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 * Created by oliver on 24.09.15.
 *
 * Server received Fire Package
 */
public class FireServerListener implements NetworkReceivedNewPacketServerEvent.Listener{

    private EntityFactory<EntityFactoryParam> factory;

    public FireServerListener(EntityFactory<EntityFactoryParam> factory){
        this.factory = factory;
        NetworkReceivedNewPacketServerEvent.registerListener(PacketIds.Fire, this);
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        PhysixBodyComponent phxc = ComponentMappers.physixBody.get(ent);
        InventoryComponent invc = ComponentMappers.inventory.get(ent);
        try{
            FirePacket packet = (FirePacket)pack;

            float p = packet.channeltime / WeaponComponent.maximumFireTime + 0.0001f;
            float scatter = WeaponComponent.maximumScattering / p;
            Vector2 dir = Vector2.Zero;
//            System.out.println("received fire package: " + packet.channeltime + "seconds channeld");
            for (int i = 0; i < WeaponComponent.ShardsPerShot; ++i) {
                if(invc.getMetalShards() <= 0){
//                    System.out.println("not enough metal shards");
                    return;
                }
//                System.out.println("shard shot");

                dir.set((float) Math.cos((Math.random() - 0.5f) * scatter),
                        (float) Math.sin((Math.random() - 0.5f) * scatter));
                dir.add((float) Math.cos(phxc.getAngle()), (float) Math.sin(phxc.getAngle()));
                // create projectile
                //Components: Bullet, Damage, Physix
                //physix component
                float projectPlayerDistance = 5.f;
                float power = 50.f;
                EntityFactoryParam param = new EntityFactoryParam();
                Vector2 startPosition = phxc.getPosition(); //ent.getComponent(PhysixBodyComponent.class).getBody().getPosition()/*.add(dir.setLength(projectPlayerDistance))*/;
                param.x = startPosition.x;
                param.y = startPosition.y;

                invc.addMetalShards(-1);
                Entity projectile = factory.createEntity("projectile", param);
                projectile.getComponent(PhysixModifierComponent.class).runnables.add(() -> {
                    //projectile.getComponent(PhysixBodyComponent.class).applyImpulse(dir.setLength(power));
                    //                 ComponentMappers.physixBody.get(projectile).setLinearDamping(10);//10 nur als vorläufiger. AUSTESTEN
                });
            }
            }catch (ClassCastException e){}
    }
}
