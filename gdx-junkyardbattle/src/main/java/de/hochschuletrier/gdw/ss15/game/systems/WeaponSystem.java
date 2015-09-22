package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;

/**
 * Created by oliver on 21.09.15.
 */
public class WeaponSystem extends IteratingSystem {
	
	private EntityFactory factory;

    public WeaponSystem() {
        super(Family.all(WeaponComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(! ComponentMappers.player.has(entity)) return;
        PlayerComponent plc = ComponentMappers.player.get(entity);
        if(! plc.isLocalPlayer) return;

        HealthComponent hc = ComponentMappers.health.get(entity);
        if(hc.healthState != HealthComponent.HealthState.ALIVE) return;

        WeaponComponent wpc = ComponentMappers.weapon.get(entity);
        PositionComponent psc = ComponentMappers.position.get(entity);

            // TODO: wait for input and physics

        // if(entity.getComponent(PlayerComponent.class))
        // ask for left click
        // if(Input.GetButton("Fire1"){
        // left button is clicked
            wpc.fireChannelTime = Math.min(wpc.fireChannelTime + deltaTime, wpc.maximumFireTime);
        //} else
        // if(Input.GetButtonUp("Fire1"){
        // left button is released
            if(wpc.fireChannelTime >= wpc.maximumFireTime){
                // railgun shot
                // create Projectile
            }else{
                // shotgun shot
                float p = wpc.fireChannelTime/ wpc.maximumFireTime + 0.0001f;
                float scatter = wpc.maximumScattering / p;
                Vector2 dir = Vector2.Zero;
                for(int i = 0; i < wpc.maximumShardsPerShot / p; ++i){

                    dir.set((float)Math.cos((Math.random() - 0.5f) * scatter),
                            (float)Math.sin((Math.random() - 0.5f) * scatter));
                    dir.add((float)Math.cos(psc.rotation), (float)Math.sin(psc.rotation));
                    // create projectile
                    //Components: Bullet, Damage, Physix
                    //physix component
                    float projectPlayerDistance = 5.f;
                    float power = 50.f;
                    EntityFactoryParam param = new EntityFactoryParam();
                    Vector2 startPosition = entity.getComponent(PhysixBodyComponent.class).getBody().getPosition().add(dir.setLength(projectPlayerDistance));
                    param.x = startPosition.x;
                    param.y = startPosition.y;
                    
                    Entity projectile = factory.createEntity("Projectile", param);
                    projectile.getComponent(PhysixBodyComponent.class).applyImpulse(dir.setLength(power));
                }
            }
        //} else // ask for right click
        // if(Input.GetButton("Fire2"){
            wpc.harvestChannelTime += deltaTime;
        //}


    }


}