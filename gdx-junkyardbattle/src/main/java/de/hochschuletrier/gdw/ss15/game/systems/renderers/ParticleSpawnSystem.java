/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 *
 * @author Julien Saevecke
 */
public class ParticleSpawnSystem implements NetworkReceivedNewPacketClientEvent.Listener{

    private Game game;
    private float footstepTimer = 0.2f;
    private Vector2 distance = new Vector2(64.f, 0);
    
    public ParticleSpawnSystem(Game game)
    {
        this.game = game;
        
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.EntityUpdate, this);
        //NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.SpawnBullet, this);
    }
    
    public void update(float deltaTime)
    {
        footstepTimer -= deltaTime;
    }
            
    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        if(pack.getPacketId() == PacketIds.EntityUpdate.getValue() 
            && ComponentMappers.player.has(ent)
            && footstepTimer <= 0.f)
        {
            footstepTimer = 0.2f;
            
            EntityUpdatePacket updatePacker = (EntityUpdatePacket)pack;
            
            if(Math.abs(updatePacker.velocityX) > 0.f || Math.abs(updatePacker.velocityY) > 0.f){
                Entity particleEffect = game.createEntity("footstepeffect", updatePacker.xPos, updatePacker.yPos);
                ComponentMappers.position.get(particleEffect).rotation = -updatePacker.rotation;
            } 
            return ;
        }
        /*
        if(pack.getPacketId() == PacketIds.SpawnBullet.getValue())
        {
            SpawnBulletPacket bulletPacker = (SpawnBulletPacket)pack;
           
            distance.rotate(bulletPacker.playerRotation);
            Entity particleEffect = game.createEntity("shoteffect", bulletPacker.playerPosition.x + distance.x, bulletPacker.playerPosition.y + distance.y);
            ComponentMappers.position.get(particleEffect).rotation = bulletPacker.playerRotation;
            distance.set(64.f, 0);
        }*/
    }
    
}
