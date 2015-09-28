package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;

public class InventorySystem extends IteratingSystem {
	
	

	public InventorySystem() {
        super(Family.all(InventoryComponent.class).get());
    }
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		InventoryComponent inventoryComponent = ComponentMappers.inventory.get(entity);
		if (inventoryComponent.secondsToRegenerationMax > 0
				&& inventoryComponent.shardRegeneration > 0) {
			if (inventoryComponent.secondsToRegeneration > 0) {
				inventoryComponent.secondsToRegeneration -= deltaTime;
			} else {
				inventoryComponent.secondsToRegeneration = inventoryComponent.secondsToRegenerationMax;
				inventoryComponent.addMetalShards(inventoryComponent.shardRegeneration);
			}
		}
		if(inventoryComponent.oldMetalShards != inventoryComponent.metalShards && ComponentMappers.client.has(entity))
		{
			inventoryComponent.oldMetalShards = inventoryComponent.metalShards;
			SimplePacket sPack = new SimplePacket(SimplePacket.SimplePacketId.MetalShardsUpdate.getValue(),inventoryComponent.metalShards);
			ComponentMappers.client.get(entity).client.sendPacket(sPack,true);
		}
	}

}
