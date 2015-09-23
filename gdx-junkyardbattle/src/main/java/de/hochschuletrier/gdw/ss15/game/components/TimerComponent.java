package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TimerComponent extends Component implements Poolable
{
	public int deathTimer = 1000;
	public int spawnTimer = 1000;

	@Override
	public void reset() {
        deathTimer = 1000;
        spawnTimer = 1000;
	}
}
