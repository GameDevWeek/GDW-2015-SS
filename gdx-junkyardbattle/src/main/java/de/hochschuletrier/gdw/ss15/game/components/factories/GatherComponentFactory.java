package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.GatherComponent;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;

public class GatherComponentFactory extends ComponentFactory<EntityFactoryParam> {


    
    public String getType() {
        return "Gather";
    }

    
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        GatherComponent component = engine.createComponent(GatherComponent.class);
        component.currentGatheringTime = properties.getFloat("currentGatheringTime",0.0f);
        component.mining = properties.getBoolean("mining",false);
        component.lastEntity = null;
        entity.add(component);
    }
}
