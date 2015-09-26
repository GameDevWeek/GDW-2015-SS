package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.PlayerDiedEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.ServerGame;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

/**
 * Drops metal shards when a player dies (on/near the corpse).
 * Make sure the "PlayerDiedEvent" is emitted via: "PlayerDiedEvent.emit(player)"
 * 
 * @author compie
 *
 */
public class MetalShardDropSystem extends EntitySystem implements PlayerDiedEvent.Listener {
    private final ServerGame game;
    
    public MetalShardDropSystem(ServerGame game) {
        super(GameConstants.PRIORITY_METAL_SHARD_DROP_SYSTEM);
        PlayerDiedEvent.register(this);
        this.game = game;
    }

    @Override
    public void onPlayerDied(Entity player) {
        InventoryComponent playerInventory = ComponentMappers.inventory.get(player);
        
        // Spawn all metal shards on player's position
        if(playerInventory != null) {
            PositionComponent playerPos = ComponentMappers.position.get(player);
            
            Entity e = game.createEntity("metalServer", playerPos.x, playerPos.y);
            InventoryComponent metalPickupInventory = ComponentMappers.inventory.get(e);
            metalPickupInventory.setMetalShards(playerInventory.getMetalShards());
        }  
    }
}
