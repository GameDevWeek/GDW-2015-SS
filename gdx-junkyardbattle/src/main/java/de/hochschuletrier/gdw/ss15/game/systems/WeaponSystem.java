package de.hochschuletrier.gdw.ss15.game.systems;

import org.lwjgl.Sys;

import ch.qos.logback.core.net.SyslogOutputStream;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.events.WeaponCharging;
import de.hochschuletrier.gdw.ss15.events.WeaponUncharged;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.FirePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.GatherPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

/**
 * Created by oliver on 21.09.15.
 */
public class WeaponSystem extends IteratingSystem {
    
    MyTimer timer = new MyTimer(true);
    float attackCooldownTimer = 0;

    public WeaponSystem() {
        super(Family.all(PlayerComponent.class,
                         WeaponComponent.class,
                         HealthComponent.class,
                         InputComponent.class).get());
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
        

        
        if(attackCooldownTimer < wpc.fireCooldown){
        	attackCooldownTimer+= deltaTime;
        	if(attackCooldownTimer >= wpc.fireCooldown){
        		wpc.fireCooldownReady = true;
        	}
        }
        
        // ask for left click
        if(input.shoot && wpc.fireCooldownReady){
        // left button is clicked
            wpc.fireChannelTime = Math.min(wpc.fireChannelTime + deltaTime, WeaponComponent.maximumFireTime);
            WeaponCharging.emit((float)(wpc.fireChannelTime/wpc.maximumFireTime));
            return; // left mouse > right mouse
        } else {
            if(wpc.fireChannelTime > 0) { // left button is released
                WeaponUncharged.emit();

                FirePacket fire = new FirePacket(wpc.fireChannelTime);
                SendPacketClientEvent.emit(fire, true);
//                System.out.println("emit fire package! " + wpc.fireChannelTime);

                wpc.fireChannelTime = 0f;
                attackCooldownTimer = 0.0f;
                wpc.fireCooldownReady = false;
            }
        }
        /*if(input.gather){
            // right button is clicked
//            wpc.harvestChannelTime = Math.min(wpc.harvestChannelTime + deltaTime, WeaponComponent.maximumHarvestTime);
            wpc.harvestChannelTime += deltaTime;
        } else {
            if(wpc.harvestChannelTime > 0) { // right button is released
//                WeaponUncharged.emit(); // fürs harvesten?
                GatherPacket gather = new GatherPacket(wpc.harvestChannelTime);
                SendPacketClientEvent.emit(gather, true);
                System.out.println("gathered for " + wpc.harvestChannelTime + "m/n/whatevsec");
                
                wpc.harvestChannelTime = 0f;
            }
        }*/
        if(input.gather)
        { // right button is clicked
            wpc.harvestChannelTime += deltaTime;
            timer.Update();
            if(timer.get_CounterMilliseconds()>50)
            {
                timer.StartCounter();
                GatherPacket gather = new GatherPacket(wpc.harvestChannelTime);
                SendPacketClientEvent.emit(gather, true);
            }
            //System.out.println("gather: " + wpc.harvestChannelTime);
        }
        else
        { // right button is released
            wpc.harvestChannelTime = 0f;
        }
    }
}