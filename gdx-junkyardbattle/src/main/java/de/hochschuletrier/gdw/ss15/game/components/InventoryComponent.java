package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;

/**
 * Stores the number of MetallShards some object contains.
 *      for a player it is e.g. his ammunation
 *      for a destructable object its the amount of metalshards to be dropped
 */
public class InventoryComponent extends Component implements Pool.Poolable {

    private int metalShards = 0;
    public int minMetalShards = 0;
    public int minMetalShardsForBase = 0;
    public int maxMetalShards = 0;
	public float secondsToRegeneration = 0.0f;
    public float secondsToRegenerationMax = 0.0f;
    public int shardRegeneration = 0;


    @Override
    public void reset() {
        metalShards = 0;
    }

    public boolean setMetalShards(int shards)
    {

        if(shards <= maxMetalShards && shards >= minMetalShards)
        {
            metalShards = shards;
            send();

            //System.out.println("Shards: " + shards);
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
        if (shards == 0)
        {
            return 0;
        }
        int oldValueShards = metalShards;
        if (!setMetalShards(metalShards + shards))
        {
            if (metalShards + shards > maxMetalShards)
            {
                metalShards = maxMetalShards;
            }
            else if (metalShards + shards < 0)
            {
                metalShards = 0;
            }
            send();
            //System.out.println("Shards: " + shards);
            return metalShards - oldValueShards;

        };

        //System.out.println("Shards: " + shards);
        return shards;
    }



    public int subMetalShards(int shards)
    {
        return - addMetalShards(-shards);
    }


    public int getMetalShards()
    {
        return metalShards;
    }

    private void send()
    {
        SimplePacket packet = new SimplePacket(SimplePacket.SimplePacketId.MetalShardsUpdate.getValue(), metalShards);
        SendPacketServerEvent.emit(packet, true);
    }

}
