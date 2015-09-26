package de.hochschuletrier.gdw.ss15.game.components.factories;

import java.awt.SecondaryLoop;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;


public class InventoryComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Inventory";
    }

    @Override
    public void run(Entity entity, SafeProperties meta,
                    SafeProperties properties, EntityFactoryParam param) {
        InventoryComponent component = engine.createComponent(InventoryComponent.class);
        component.minMetalShards = properties.getInt("minMetalShards");
        component.maxMetalShards = properties.getInt("maxMetalShards");
        component.minMetalShardsForBase = properties.getInt("minMetalShardsForBase");
        component.setMetalShards(properties.getInt("metalShards"));

        component.secondsToRegenerationMax = component.secondsToRegeneration = properties.getFloat("secondsToRegenerate",0.0f);
        if(component.secondsToRegenerationMax > 0.0f)
            component.shardRegeneration = properties.getInt("amountToRegenerate",0);
        entity.add(component);
    }

}
