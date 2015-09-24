package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.events.CollisionEvent;
import de.hochschuletrier.gdw.ss15.events.WeaponCharging;
import de.hochschuletrier.gdw.ss15.events.WeaponUncharged;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.FirePacket;
import de.hochschuletrier.gdw.ss15.game.utils.Timer;

/**
 * Created by oliver on 21.09.15.
 */
public class WeaponSystem extends IteratingSystem {

    public WeaponSystem() {
        super(Family.all(PlayerComponent.class/*,
                         WeaponComponent.class,
                         HealthComponent.class,
                         InputComponent.class*/).get());
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
        // ask for left click
        if(input.shoot){
        // left button is clicked
            wpc.fireChannelTime = Math.min(wpc.fireChannelTime + deltaTime, WeaponComponent.maximumFireTime);
            WeaponCharging.emit();
            return; // left mouse > right mouse
        } else {
            if(wpc.fireChannelTime > 0) { // left button is released
                WeaponUncharged.emit();

                FirePacket fire = new FirePacket(wpc.fireChannelTime);
                SendPacketClientEvent.emit(fire, true);

                wpc.fireChannelTime = 0f;
            }
        }
        if(input.gather){
            wpc.harvestChannelTime += deltaTime;
        }


    }

}