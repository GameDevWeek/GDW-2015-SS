/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;

/**
 *
 * @author Julien Saevecke
 */
public class RenderStateUpdateSystem extends IteratingSystem {
    private final PooledEngine engine;
    
    @SuppressWarnings("unchecked")
    public RenderStateUpdateSystem(PooledEngine engine) {
        super(Family.all(HealthComponent.class).get());
        
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComp = ComponentMappers.health.get(entity);
        
        if(healthComp.health <= 0) {
            if(ComponentMappers.animator.has(entity)){
                ComponentMappers.animator.get(entity).draw = false;
            }
            if(ComponentMappers.texture.has(entity)){
                ComponentMappers.texture.get(entity).draw = false;
            }
            if(ComponentMappers.coneLight.has(entity)){
                ComponentMappers.coneLight.get(entity).coneLight.setActive(false);
            }
        }
        else{
            if(ComponentMappers.animator.has(entity)){
                ComponentMappers.animator.get(entity).draw = true;
            }
            if(ComponentMappers.texture.has(entity)){
                ComponentMappers.texture.get(entity).draw = true;
            }
            if(ComponentMappers.coneLight.has(entity)){
                ComponentMappers.coneLight.get(entity).coneLight.setActive(true);
            }
        }
    }
}
        