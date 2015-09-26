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
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.rendering.ChangeModeOnEffectEvent;
import de.hochschuletrier.gdw.ss15.events.rendering.ChangePositionOnEffectEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.EffectMode;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.SmokeComponent;

/**
 *
 * @author Julien Saevecke
 */
public class ParticleEffectRenderer extends SortedSubIteratingSystem.SubSystem implements EntityListener, ChangeModeOnEffectEvent.Listener, ChangePositionOnEffectEvent.Listener{

    private Vector2 rotateVector = new Vector2(0, 1);
    
    @SuppressWarnings("unchecked")
    public ParticleEffectRenderer(Engine engine) {
        super(Family.all(ParticleEffectComponent.class).exclude(SmokeComponent.class).get());
        engine.addEntityListener(Family.all(ParticleEffectComponent.class).get(), this);
        
         ChangeModeOnEffectEvent.register(this);
         ChangePositionOnEffectEvent.register(this);
    }
    
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        ChangeModeOnEffectEvent.unregister(this);
        ChangePositionOnEffectEvent.unregister(this);
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
            rotateVector.set(particleEffectComponent.positionOffsetX, particleEffectComponent.positionOffsetY);
            rotateVector.rotate(particleEffectComponent.initialRotation + position.rotation);
            
            particleEffectComponent.particleEffect.setPosition(
                position.x + rotateVector.x, 
                position.y + rotateVector.y
            );
            
            particleEffectComponent.setRotation(particleEffectComponent.initialRotation + position.rotation);

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

    @Override
    public void onChangeModeOnEffect(EffectMode mode, Entity entity) {
        ParticleEffectComponent particleEffectComponent = ComponentMappers.particleEffect.get(entity);
        particleEffectComponent.changeMode(mode);
    }

    @Override
    public void onChangePositionOnEffect(Vector2 position, Entity entity) {
        PositionComponent positionComponent = ComponentMappers.position.get(entity);
        positionComponent.x = position.x;
        positionComponent.y = position.y;
    }
}
