package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.InterpolatePositionComponent;

public class InterploatePositionComponentFactory extends ComponentFactory<EntityFactoryParam> {

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "Interpolate";
	}

	@Override
	public void run(Entity entity, SafeProperties meta,
			SafeProperties properties, EntityFactoryParam param) {
		InterpolatePositionComponent component = engine.createComponent(InterpolatePositionComponent.class);
        entity.add(component);
	}

}
