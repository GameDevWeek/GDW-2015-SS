package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DamageComponent extends Component implements Poolable 
{
	public int damage;
    public boolean damageToPlayer;
    public boolean damageToTile;
    public float radius;

	@Override
	public void reset() 
	{
		damage = 0;
        damageToPlayer = false;
        damageToTile = false;	
        radius = 0;
	}

}
