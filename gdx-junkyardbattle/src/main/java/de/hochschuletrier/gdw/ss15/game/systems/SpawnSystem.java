/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkNewPlayerEvent;

/**
 *
 * @author Julien Saevecke
 */
public class SpawnSystem implements NetworkNewPlayerEvent.Listener{

    public SpawnSystem()
    {
        NetworkNewPlayerEvent.registerListener(this);
    }
    
    @Override
    public void onNetworkNewPacket(Entity ent) {
    }
}
