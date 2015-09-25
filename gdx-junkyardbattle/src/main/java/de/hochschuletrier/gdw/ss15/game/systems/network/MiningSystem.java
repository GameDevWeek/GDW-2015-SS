package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.MiningEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.BasePointComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.MineableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class MiningSystem extends EntitySystem implements MiningEvent.Listener {

    ComponentMapper<MineableComponent> mineableComp = ComponentMappers.mineable;
    ComponentMapper<PlayerComponent> playerComp = ComponentMappers.player;
    ComponentMapper<InventoryComponent> inventoryComp = ComponentMappers.inventory;

    @Override
    public void onMiningEvent(Entity playerEnt, Entity mineableEnt, float channelTime) {
        int minedMetalShards = (int)(GameConstants.MINING_PER_SECOND * channelTime);

        int actuallyMinedMetalShards = inventoryComp.get(mineableEnt).subMetalShards(minedMetalShards);
        inventoryComp.get(playerEnt).addMetalShards(actuallyMinedMetalShards);
    }
}
