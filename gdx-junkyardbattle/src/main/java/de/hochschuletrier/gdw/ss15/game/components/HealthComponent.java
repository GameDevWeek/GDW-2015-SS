package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable
{
	public int value = 100;
    public int decrementByValueNextFrame = 0;
    public float spawningDuration = 1000;
    public enum healthState
    {
        DEAD, DYING, ALIVE;
    }

    public healthState health = healthState.ALIVE;
    
    @Override
    public void reset() {
        value = 0;
        health = healthState.DEAD;
        decrementByValueNextFrame = 0;
        spawningDuration = 0;
    }

}
