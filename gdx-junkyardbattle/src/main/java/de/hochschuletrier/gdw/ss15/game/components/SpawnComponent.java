package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import de.hochschuletrier.gdw.ss15.game.GameConstants;


public class SpawnComponent extends Component implements Poolable 
{
        public boolean respawn = false;
	    public Vector2 spawnPoint = new Vector2();
        public float respawnTimer = 0.f;
        
	    @Override
	    public void reset() 
	    {
	    	respawn = false;
            spawnPoint = Vector2.Zero;
            respawnTimer = GameConstants.RESPAWN_TIMER;
	    }
}
