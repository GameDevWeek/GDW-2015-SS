package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.network.Packets.EntityUpdatePacket;

public class InterpolatePositionComponent extends Component implements Pool.Poolable {

	public EntityUpdatePacket firstPos, secondPos;
	public float sumDeltaTime = 0; 
	
	@Override
	public void reset() {
		firstPos = null;
		secondPos = null;
	}

}
