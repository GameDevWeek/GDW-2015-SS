package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class PositionComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Position";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        PositionComponent component = engine.createComponent(PositionComponent.class);
        component.x = param.x;
        component.y = param.y;
        component.layer = properties.getFloat("layer", GameConstants.DEFAULT_LAYER);
        entity.add(component);
    }
}