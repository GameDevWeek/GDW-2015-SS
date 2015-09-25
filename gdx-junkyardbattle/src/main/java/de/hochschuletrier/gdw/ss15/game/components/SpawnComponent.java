package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;


public class SpawnComponent extends Component implements Poolable 
{
        public boolean respawn = false;
	    public PositionComponent spawnPoint = null;
        public float respawnTimer = 0.f;
        
	    @Override
	    public void reset() 
	    {
	    	respawn = false;
            spawnPoint = null;
            respawnTimer = 0.f;
	    }
}
