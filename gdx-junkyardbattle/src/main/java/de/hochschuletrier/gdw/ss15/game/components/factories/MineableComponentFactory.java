package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

public class MineableComponentFactory extends ComponentFactory<EntityFactoryParam>{

	@Override
	public String getType() {
		return "Mineable";
	}

	@Override
	public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
		// TODO Auto-generated method stub
		
	}

}
