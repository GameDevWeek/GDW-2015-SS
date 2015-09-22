package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DeathComponent extends Component implements Poolable
{
   
    public float dyingDuration = 30000;
    public float stateTime = 0;
    
    
    @Override
    public void reset()
    {
        dyingDuration = 0;
    }
    
    
    
}