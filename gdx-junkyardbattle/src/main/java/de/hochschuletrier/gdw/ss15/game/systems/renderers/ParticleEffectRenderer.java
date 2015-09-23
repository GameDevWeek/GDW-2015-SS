/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;

/**
 *
 * @author Julien Saevecke
 */
public class ParticleEffectRenderer extends SortedSubIteratingSystem.SubSystem implements EntityListener{

    @SuppressWarnings("unchecked")
    public ParticleEffectRenderer(Engine engine) {
        super(Family.all(ParticleEffectComponent.class).get());
        engine.addEntityListener(Family.all(ParticleEffectComponent.class).get(), this);
    }
    
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        ParticleEffectComponent particleEffectComponent = ComponentMappers.particleEffect.get(entity);
        
        if(particleEffectComponent.loop){
           for(ParticleEmitter particleEmitter: particleEffectComponent.particleEffect.getEmitters()){
               particleEmitter.durationTimer=0;
           }
        }
        
        if(particleEffectComponent.isPlaying){
            particleEffectComponent.particleEffect.setPosition(
                position.x + particleEffectComponent.positionOffsetX, 
                position.y + particleEffectComponent.positionOffsetY
            );

            particleEffectComponent.particleEffect.update(deltaTime);
            particleEffectComponent.particleEffect.draw(DrawUtil.batch);
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        ParticleEffectComponent particleEffectComponent = ComponentMappers.particleEffect.get(entity);
        
        if(particleEffectComponent.start)
        {
            particleEffectComponent.particleEffect.reset();
            particleEffectComponent.particleEffect.flipY();
            particleEffectComponent.particleEffect.start();
            particleEffectComponent.isPlaying = true;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
    }
}
