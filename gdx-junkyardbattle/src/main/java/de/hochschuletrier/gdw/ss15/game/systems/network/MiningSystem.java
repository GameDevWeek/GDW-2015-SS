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

    public MiningSystem()
    {
        MiningEvent.register(this);
    }

    @Override
    public void onMiningEvent(Entity playerEnt, Entity mineableEnt, float channelTime) {
        //System.out.println("Start Shards Player: " + inventoryComp.get(playerEnt).getMetalShards());
        //System.out.println("ChannelTime: " + channelTime);
        //System.out.println("SatInv: " + inventoryComp.get(mineableEnt).getMetalShards());
        //System.out.println("PlaInv: " + inventoryComp.get(playerEnt).getMetalShards());
        int minedMetalShards = (int)Math.ceil((GameConstants.MINING_PER_SECOND * channelTime));

        System.out.println("MinedMetalShards: " + minedMetalShards);
        System.out.println("PlayerShards1: " + inventoryComp.get(playerEnt).getMetalShards());
        System.out.println("SatelliteShards1: " + inventoryComp.get(mineableEnt).getMetalShards());

        transferMines(mineableEnt, playerEnt, minedMetalShards);

        System.out.println("PlayerShards2: " + inventoryComp.get(playerEnt).getMetalShards());
        System.out.println("SatelliteShards2: " + inventoryComp.get(mineableEnt).getMetalShards());
        System.out.println();
       /* System.out.println("minedMetalShards: " + minedMetalShards);
        int actuallyMinedMetalShards = inventoryComp.get(mineableEnt).subMetalShards(minedMetalShards);
        System.out.println("actuallyMinedMetalShards: " + actuallyMinedMetalShards);
        int collected = inventoryComp.get(playerEnt).addMetalShards(actuallyMinedMetalShards);
        System.out.println("Sammeln " + collected);

        System.out.println("Shards Player: " + inventoryComp.get(playerEnt).getMetalShards());
        System.out.println("Shards Satellite: " + inventoryComp.get(mineableEnt).getMetalShards());*/


    }

    private void transferMines(Entity givingEntity, Entity takingEntity, int minedMetalShards)
    {
        int givingEntityHas = inventoryComp.get(givingEntity).getMetalShards();
        int givingEntityMin = inventoryComp.get(givingEntity).minMetalShards;

        int takingEntityHas = inventoryComp.get(takingEntity).getMetalShards();
        int takingEntityMax = inventoryComp.get(takingEntity).maxMetalShards;

        int givingEntityCanGive = givingEntityHas - givingEntityMin;
        int takingEntityCanTake = takingEntityMax - takingEntityHas;

        int shardsToTransfer = Math.min(givingEntityCanGive, Math.min(takingEntityCanTake, minedMetalShards));

        inventoryComp.get(givingEntity).subMetalShards(shardsToTransfer);
        inventoryComp.get(takingEntity).addMetalShards(shardsToTransfer);
    }
}
