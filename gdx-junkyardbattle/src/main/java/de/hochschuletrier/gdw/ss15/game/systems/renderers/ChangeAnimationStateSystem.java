/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

/**
 *
 * @author Julien Saevecke
 */
public class ChangeAnimationStateSystem implements NetworkReceivedNewPacketClientEvent.Listener{

    public ChangeAnimationStateSystem()
    {
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.EntityUpdate, this);
    }
            
    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        if(pack.getPacketId() == PacketIds.EntityUpdate.getValue() && ComponentMappers.player.has(ent))
        {
            EntityUpdatePacket updatePacker = (EntityUpdatePacket)pack;
            AnimatorComponent animator = ComponentMappers.animator.get(ent);
            
            if(Math.abs(updatePacker.velocityX) > 0.f || Math.abs(updatePacker.velocityY) > 0.f){
                animator.animationState = AnimationState.WALK;
            }
            else{
                animator.animationState = AnimationState.IDLE;
            }
        }
    }
}