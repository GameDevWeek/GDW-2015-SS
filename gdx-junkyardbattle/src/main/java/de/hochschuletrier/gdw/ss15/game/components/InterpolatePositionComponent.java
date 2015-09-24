package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;

public class InterpolatePositionComponent extends Component implements Pool.Poolable {

	public EntityUpdatePacket firstPos, secondPos;
	public Vector2 velocity;
	public float sumDeltaTime = 0;
	public boolean used = false;
	
	@Override
	public void reset() {
		firstPos = null;
		secondPos = null;
	}

}
