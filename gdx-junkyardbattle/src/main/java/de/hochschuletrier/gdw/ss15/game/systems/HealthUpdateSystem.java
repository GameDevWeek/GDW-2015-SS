/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.HealthPacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 *
 * @author Julien Saevecke
 */
public class HealthUpdateSystem implements NetworkReceivedNewPacketClientEvent.Listener{
    
    public HealthUpdateSystem()
    {
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.Health, this);
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        HealthPacket healthPacket = (HealthPacket)pack;
        
        HealthComponent healthComponent = ComponentMappers.health.get(ent);
        healthComponent.health = healthPacket.health;
    }
}
