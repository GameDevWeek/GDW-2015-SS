package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public class GatherComponent extends Component implements Poolable
{
	public float currentGatheringTime;
    public boolean mining;
    public Entity lastEntity;

    
    @Override
    public void reset() {
    	currentGatheringTime = 0.0f;
        mining = false;
        lastEntity = null;
    }

}
