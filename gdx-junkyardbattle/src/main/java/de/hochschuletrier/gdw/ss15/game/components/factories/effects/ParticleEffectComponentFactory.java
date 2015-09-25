/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.factories.effects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;

/**
 *
 * @author Julien Saevecke
 */
public class ParticleEffectComponentFactory extends ComponentFactory<EntityFactoryParam>{

    @Override
    public String getType() {
        return "ParticleEffect";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
       ParticleEffectComponent particleEffectComponent = engine.createComponent(ParticleEffectComponent.class);
       
       particleEffectComponent.particleEffect = new ParticleEffect(assetManager.getParticleEffect(properties.getString("effectname")));
       particleEffectComponent.positionOffsetX = properties.getFloat("offsetX", 0.f);
       particleEffectComponent.positionOffsetY = properties.getFloat("offsetY", 0.f);
       particleEffectComponent.initialRotation = properties.getFloat("initialRotation", 0.f);      
       
       entity.add(particleEffectComponent);
    }
    
}
