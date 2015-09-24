package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.CollisionEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;

/**
 * Created by oliver on 21.09.15.
 */
public class WeaponSystem extends IteratingSystem implements CollisionEvent.Listener {

    private EntityFactory factory;

    public WeaponSystem() {
        super(Family.all(WeaponComponent.class,
                         PlayerComponent.class,
                         HealthComponent.class,
                         InputComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent plc = ComponentMappers.player.get(entity);
        if(! plc.isLocalPlayer) return;

        HealthComponent hc = ComponentMappers.health.get(entity);
        if(hc.healthState != HealthComponent.HealthState.ALIVE) return;

        WeaponComponent wpc = ComponentMappers.weapon.get(entity);
        PositionComponent psc = ComponentMappers.position.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);


            // TODO: wait for input and physics

        // ask for left click
        if(input.shoot){
        // left button is clicked
            wpc.fireChannelTime = Math.min(wpc.fireChannelTime + deltaTime, wpc.maximumFireTime);
            return; // left mouse > right mouse
        } else {
            // left button is released
            // shotgun shot
            float p = wpc.fireChannelTime / wpc.maximumFireTime + 0.0001f;
            float scatter = wpc.maximumScattering / p;
            Vector2 dir = Vector2.Zero;
            for (int i = 0; i < wpc.maximumShardsPerShot / p; ++i) {

                dir.set((float) Math.cos((Math.random() - 0.5f) * scatter),
                        (float) Math.sin((Math.random() - 0.5f) * scatter));
                dir.add((float) Math.cos(psc.rotation), (float) Math.sin(psc.rotation));
                // create projectile
                //Components: Bullet, Damage, Physix
                //physix component
                float projectPlayerDistance = 5.f;
                float power = 50.f;
                EntityFactoryParam param = new EntityFactoryParam();
                Vector2 startPosition = entity.getComponent(PhysixBodyComponent.class).getBody().getPosition().add(dir.setLength(projectPlayerDistance));
                param.x = startPosition.x;
                param.y = startPosition.y;

//                Entity projectile = factory.createEntity("Projectile", param);
//                projectile.getComponent(PhysixBodyComponent.class).applyImpulse(dir.setLength(power));
            }
        }

        if(input.gather){
            wpc.harvestChannelTime += deltaTime;
        }


    }


    @Override
    public void onCollisionEvent(PhysixContact physixContact) {

    }
}
