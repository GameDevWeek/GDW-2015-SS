package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.GameConstants;

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
        if (!setMetalShards(metalShards += shards))
        {
            metalShards = GameConstants.MAX_METALSHARDS;
        };
        return metalShards - oldValueShards;
    }

    public int getMetalShards()
    {
        return metalShards;
    }

}
