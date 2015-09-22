package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.DamageComponent;

public class DamageComponentFactory extends ComponentFactory<EntityFactoryParam> {

	@Override
	public String getType()
	{
		return "Damage" ;
	}

	@Override
	public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
		DamageComponent component = engine.createComponent(DamageComponent.class);
		component.damage = Integer.parseInt(properties.getString("damage"));
		component.radius = Float.parseFloat(properties.getString("radius"));
		component.damageToPlayer = Boolean.parseBoolean(properties.getString("damageToPlayer"));
		component.damageToTile = Boolean.parseBoolean(properties.getString("damageToTile"));
		
	}

}
