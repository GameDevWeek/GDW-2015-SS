package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.SpawnComponent;

public class InventorySystem extends EntitySystem {
	ImmutableArray<Entity> entities;

	@Override
	public void addedToEngine(Engine engine) {
		entities = engine.getEntitiesFor(Family.all(InventoryComponent.class).get());
	}

	@Override
	public void update(float deltaTime) {
		for (Entity entity : entities) {
			InventoryComponent inventoryComponent = ComponentMappers.inventory
					.get(entity);
			if (inventoryComponent.secondsToRegenerationMax > 0 && inventoryComponent.shardRegeneration > 0) {
				if (inventoryComponent.secondsToRegeneration > 0){
					inventoryComponent.secondsToRegeneration -= deltaTime;
				}
				else {
					inventoryComponent.secondsToRegeneration = inventoryComponent.secondsToRegenerationMax;
					inventoryComponent.addMetalShards(inventoryComponent.shardRegeneration);
				}
			}
		}
	}

}
