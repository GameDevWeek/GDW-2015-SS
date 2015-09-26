package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.SpawnSatelliteComponent;


public class SpawnSatelliteComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "SpawnSatellite";
    }

    @Override
    public void run(Entity entity, SafeProperties meta,
            SafeProperties properties, EntityFactoryParam param) {
        SpawnSatelliteComponent component = engine.createComponent(SpawnSatelliteComponent.class);
        entity.add(component);
    }

}