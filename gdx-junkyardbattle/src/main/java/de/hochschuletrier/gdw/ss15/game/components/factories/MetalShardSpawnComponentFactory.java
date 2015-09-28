package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.MetalShardSpawnComponent;

public class MetalShardSpawnComponentFactory extends ComponentFactory<EntityFactoryParam>{

	@Override
	public String getType() {
		return "MetalShardSpawn";
	}

	@Override
	public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
		MetalShardSpawnComponent comp = engine.createComponent(MetalShardSpawnComponent.class);
		comp.minTimeBetweenSpawns = properties.getFloat("spawnTime", 5.0f);
        entity.add(comp);
	}

}
