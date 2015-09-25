/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.DeathComponent;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;

/**
 *
 * @author Julien Saevecke
 */
public class DeathComponentFactory extends ComponentFactory<EntityFactoryParam> {

    public String getType() {
        return "Death";
    }

    
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        DeathComponent component = engine.createComponent(DeathComponent.class);
        
        component.dyingDuration = properties.getFloat("dyingDuration", 1000);
        component.stateTime = 0.f;
        
        entity.add(component);
    }
}
 
