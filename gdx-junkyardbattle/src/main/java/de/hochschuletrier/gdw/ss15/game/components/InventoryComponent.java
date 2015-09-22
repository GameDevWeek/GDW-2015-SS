package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

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
    	//100 mit const MAX LIMIT ersetzen
    	//0 mit const MIN LIMIT ersetzen
    	if(shards <= 100 && shards >= 0)
    	{
    		metalShards = shards;
    		return true;
    	}
    	return false;
    }
    
    public int getMetalShards()
    {
    	return metalShards;
    }

}
