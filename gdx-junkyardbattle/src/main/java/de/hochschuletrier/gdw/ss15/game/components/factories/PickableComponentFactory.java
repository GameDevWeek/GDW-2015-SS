package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;

public class PickableComponentFactory  extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Pickable";
    }

    @Override
    public void run(Entity entity, SafeProperties meta,
            SafeProperties properties, EntityFactoryParam param)
    {
        PickableComponent component = engine.createComponent(PickableComponent.class);
        component.metalShards = properties.getInt("metalShards");
        entity.add(component);
    }
	
}
