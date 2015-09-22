package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TimerComponent extends Component implements Poolable
{
	public float deathTimer = 0.25f;
	public int spawnTimer = 1000;

	@Override
	public void reset() {
        deathTimer = 0.25f;
        spawnTimer = 1000;
	}
}
