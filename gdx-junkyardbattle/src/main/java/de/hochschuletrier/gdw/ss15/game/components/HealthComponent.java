package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import de.hochschuletrier.gdw.ss15.game.GameConstants;

public class HealthComponent extends Component implements Poolable
{
    //public static int startHealth;
	public int health;
    public int decrementByValueNextFrame = 0;
    public float dyingTimer = GameConstants.DYING_TIMER;

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
        dyingTimer = GameConstants.DYING_TIMER;
    }

}
