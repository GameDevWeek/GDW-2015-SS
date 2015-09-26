package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameGlobals;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.AttachedParticleEntityComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;

public class EffectAddSystem extends IteratingSystem implements EntityListener {
    private final PooledEngine engine;
    
    @SuppressWarnings("unchecked")
    public EffectAddSystem(PooledEngine engine) {
        super(Family.all(PlayerComponent.class).one(ConeLightComponent.class).get());
        this.engine = engine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(Family.all(PlayerComponent.class, InputComponent.class).get(), this);
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent posComp = ComponentMappers.position.get(entity);
        ConeLightComponent coneLightComp = ComponentMappers.coneLight.get(entity);
        AttachedParticleEntityComponent particleEntityComp = ComponentMappers.particleEntity.get(entity);
        InputComponent inputComp = ComponentMappers.input.get(entity);
        
        if(coneLightComp != null) {
            coneLightComp.coneLight.setDirection(posComp.rotation);
        }
        
        if(particleEntityComp != null && particleEntityComp.entity != null) {
            ParticleEffectComponent particleComp = ComponentMappers.particleEffect.get(particleEntityComp.entity);
            particleComp.draw = inputComp.gather;
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        PositionComponent entityPos = ComponentMappers.position.get(entity);
        
        if(!ComponentMappers.coneLight.has(entity)) {
            ConeLightComponent coneLightComp = engine.createComponent(ConeLightComponent.class);
            coneLightComp.set(new Color(0.f, 1.f, 1.f, 0.9f), 15.f, 0.f, 40.f);
            entity.add(coneLightComp);
        }
        
        if(!ComponentMappers.pointLight.has(entity)) {
            PointLightComponent pointLightComponent = engine.createComponent(PointLightComponent.class);
            pointLightComponent.set(new Color(1.f, 1.f, 0.f, 0.7f), 8.f);
            pointLightComponent.pointLight.setXray(true);
            entity.add(pointLightComponent);
        }
        
        if(!ComponentMappers.particleEntity.has(entity)) {
            Entity particleEntity = engine.createEntity();
            PositionComponent posComp = engine.createComponent(PositionComponent.class);
            posComp.x = entityPos.x;
            posComp.y = entityPos.y;
            ParticleEffectComponent effectComp = engine.createComponent(ParticleEffectComponent.class);
            effectComp.particleEffect = new ParticleEffect(GameGlobals.assetManager.getParticleEffect("particle"));
            effectComp.positionOffsetX = 70.f;
            effectComp.draw = false;
            for(ParticleEmitter e : effectComp.particleEffect.getEmitters())
                e.setAttached(true);
            
            particleEntity.add(effectComp);
            particleEntity.add(entityPos);
            engine.addEntity(particleEntity);
            
            AttachedParticleEntityComponent particleEntityComp = engine.createComponent(AttachedParticleEntityComponent.class);
            particleEntityComp.entity = particleEntity;
            entity.add(particleEntityComp);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
    }

}
