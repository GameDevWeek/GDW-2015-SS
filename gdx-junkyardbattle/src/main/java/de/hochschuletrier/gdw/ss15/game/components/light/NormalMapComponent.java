package de.hochschuletrier.gdw.ss15.game.components.light;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class NormalMapComponent extends Component implements Pool.Poolable {
	public Texture normalMap;

	@Override
	public void reset() {
		normalMap = null;
	}
	
}
