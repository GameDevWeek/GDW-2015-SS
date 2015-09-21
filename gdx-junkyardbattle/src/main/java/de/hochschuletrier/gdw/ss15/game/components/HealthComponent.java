package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable
{
	public int Value = 100;
    public int DecrementByValueNextFrame = 0;
    public float spawningDuration = 1000;
    public enum HealthState 
    {
        DEAD, DYING, ALIVE;
    }

    public HealthState health = HealthState.ALIVE;
    
    @Override
    public void reset() {
        Value = 0;
        health = HealthState.DEAD;
        DecrementByValueNextFrame = 0;
        spawningDuration = 0;
    }

}
