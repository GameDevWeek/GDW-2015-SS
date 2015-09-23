package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class GoalComponent extends Component implements Poolable
{
	public int pointsCollected;
	public boolean gameEnd;
	@Override
	public void reset() {
		pointsCollected = 0;
		gameEnd = false;
		
	}

	
}
