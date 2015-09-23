package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.DamageComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;

public class DamageComponentFactory extends ComponentFactory<EntityFactoryParam>{

	@Override
	public String getType() {
		return "Damage";
	}

	@Override
	public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
		DamageComponent component = engine.createComponent(DamageComponent.class);
		component.damage = properties.getInt("damageValue");
		component.damageToPlayer = properties.getBoolean("damageToPlayer");
		component.damageToTile = properties.getBoolean("damageToTile");
		component.radius = properties.getFloat("radius");
        entity.add(component);
	}

}
