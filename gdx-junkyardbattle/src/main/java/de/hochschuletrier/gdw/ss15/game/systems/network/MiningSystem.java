package de.hochschuletrier.gdw.ss15.game.systems.network;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import de.hochschuletrier.gdw.ss15.events.MiningEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.BasePointComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.MineableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;

/**
 * Created by Ricardo on 25.09.2015.
 */
public class MiningSystem extends EntitySystem implements MiningEvent.Listener {

    ComponentMapper<InventoryComponent> inventoryComp = ComponentMappers.inventory;

    @Override
    public void onMiningEvent(Entity playerEnt, Entity mineableEnt, float channelTime) {
        int minedMetalShards = (int)(GameConstants.MINING_PER_SECOND * channelTime);

        int actuallyMinedMetalShards = inventoryComp.get(mineableEnt).subMetalShards(minedMetalShards);
        int collected = inventoryComp.get(playerEnt).addMetalShards(actuallyMinedMetalShards);
        System.out.println("Sammeln " + collected);


    }
}
