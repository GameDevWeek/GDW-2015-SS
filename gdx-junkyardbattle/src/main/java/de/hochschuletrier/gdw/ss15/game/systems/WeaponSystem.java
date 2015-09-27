package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.events.WeaponCharging;
import de.hochschuletrier.gdw.ss15.events.WeaponUncharged;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.FirePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.GatherPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.InitEntityPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.MyTimer;

/**
 * Created by oliver on 21.09.15.
 */
public class WeaponSystem extends IteratingSystem implements NetworkReceivedNewPacketClientEvent.Listener {
    
    MyTimer timer = new MyTimer(true);
    float attackCooldownTimer = 0;
    Entity tractorSound = new Entity();

    public WeaponSystem() {
        super(Family.all(PlayerComponent.class,
                WeaponComponent.class,
                HealthComponent.class,
                InputComponent.class).get());
        tractorSound.add(new SoundEmitterComponent());
        ComponentMappers.soundEmitter.get(tractorSound).isPlaying = false;
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.InitEntity, this);
        //ComponentMappers.soundEmitter.get(tractorSound).emitter.
        //System.out.println("TractorVol: " + ComponentMappers.soundEmitter.get(tractorSound).emitter.getGlobalVolume());
        //ComponentMappers.soundEmitter.get(tractorSound).emitter.setGlobalVolume(100);
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
                /*if (ComponentMappers.soundEmitter.has(entity)) {
                    SoundEvent.emit("shotgun_shoot", weaponSound);
                }*/
                WeaponUncharged.emit();

                FirePacket fire = new FirePacket(wpc.fireChannelTime);
                SendPacketClientEvent.emit(fire, true);

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

            //System.out.println("Gathersound");
            //System.out.println("GatherBoolean: " + ComponentMappers.soundEmitter.get(tractorSound).isPlaying);
            //SoundEvent.emit("magnet_beam1", tractorSound, true);


            if(!ComponentMappers.soundEmitter.get(tractorSound).isPlaying)
            {
                //System.out.println("Gathersound is playing");
                SoundEvent.emit("magnet_beam1", tractorSound, true);
                ComponentMappers.soundEmitter.get(tractorSound).isPlaying = true;
            }

        }
        else
        { // right button is released
            wpc.harvestChannelTime = 0f;
            SoundEvent.stopSound(tractorSound);
            if (ComponentMappers.soundEmitter.get(tractorSound).isPlaying) {
                //System.out.println("Gathersound is stopping");
                SoundEvent.stopSound(tractorSound);
                ComponentMappers.soundEmitter.get(tractorSound).isPlaying = false;
            }
        }
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        InitEntityPacket ePack = (InitEntityPacket)pack;
        Entity weaponSound = new Entity();
        weaponSound.add(new SoundEmitterComponent());
        if(ePack.name.equals("projectileClient"))
        {
            SoundEvent.emit("shotgun_shoot", weaponSound);
        }
            }
}