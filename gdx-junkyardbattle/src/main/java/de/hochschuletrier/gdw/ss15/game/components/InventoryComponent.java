package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import sun.java2d.pipe.SpanShapeRenderer;

/**
 * Stores the number of MetallShards some object contains.
 *      for a player it is e.g. his ammunation
 *      for a destructable object its the amount of metalshards to be dropped
 */
public class InventoryComponent extends Component implements Pool.Poolable {

    private int metalShards = 0;


    @Override
    public void reset() {
        metalShards = 0;
    }

    public boolean setMetalShards(int shards)
    {

        if(shards <= GameConstants.MAX_METALSHARDS && shards >= GameConstants.MIN_METALSHARDS)
        {
            metalShards = shards;
            SimplePacket packet = new SimplePacket(SimplePacket.SimplePacketId.MetalShardsUpdate.getValue(), metalShards);
            SendPacketServerEvent.emit(packet, true);
            return true;
        }
        return false;
    }

    /**
     * @param shards
     * @return number of shards that are actually added, if you add too much the maximum number of metal shards will be set
     */
    public int addMetalShards(int shards)
    {
        int oldValueShards = metalShards;
        if (!setMetalShards(metalShards + shards))
        {
            if (metalShards + shards > GameConstants.MAX_METALSHARDS)
            {
                metalShards = GameConstants.MAX_METALSHARDS;
            }
            else if (metalShards - shards < 0)
            {
                metalShards = 0;
            }

        };
        return metalShards - oldValueShards;
    }

    public int subMetalShards(int shards)
    {
        return addMetalShards(-shards);
    }


    public int getMetalShards()
    {
        return metalShards;
    }

}
