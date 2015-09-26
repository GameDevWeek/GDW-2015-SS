/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.ss15.events.PlayerDiedEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.DeathComponent;

/**
 *
 * @author Julien Saevecke
 */
public class DeathSystem extends EntitySystem{
    
    private ImmutableArray<Entity> entities;
    private Engine engine;
    
    @Override
	public void addedToEngine (Engine engine) {
        this.engine = engine;
        entities = engine.getEntitiesFor(Family.all(DeathComponent.class).get());
	}
    
    @Override
	public void removedFromEngine (Engine engine) {
	}
    
    @Override
	public void update (float deltaTime) {
        for(Entity entity : entities)
        {
            DeathComponent deathComponent = ComponentMappers.death.get(entity);
            deathComponent.stateTime += deltaTime;
            
            if(deathComponent.dyingDuration <= deathComponent.stateTime)
            {
                if(ComponentMappers.player.has(entity))
                    PlayerDiedEvent.emit(entity);
                else
                    engine.removeEntity(entity);
            }
        }
	}
}
