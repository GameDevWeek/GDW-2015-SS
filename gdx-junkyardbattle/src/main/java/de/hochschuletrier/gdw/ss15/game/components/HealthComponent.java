package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable
{
	public int health;
    public int decrementByValueNextFrame = 0;
    public float spawningDuration;
    public enum HealthState
    {
        DEAD, DYING, ALIVE;
    }

    public HealthState healthState = HealthState.ALIVE;
    
    @Override
    public void reset() {
        health = 0;
        healthState = HealthState.DEAD;
        decrementByValueNextFrame = 0;
        spawningDuration = 0;
    }

}
