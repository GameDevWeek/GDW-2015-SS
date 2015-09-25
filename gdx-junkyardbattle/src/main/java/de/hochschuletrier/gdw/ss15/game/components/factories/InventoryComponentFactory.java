package de.hochschuletrier.gdw.ss15.game.components.factories;

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
		component.setMetalShards(properties.getInt("metalShards"));
		entity.add(component);
	}

}
