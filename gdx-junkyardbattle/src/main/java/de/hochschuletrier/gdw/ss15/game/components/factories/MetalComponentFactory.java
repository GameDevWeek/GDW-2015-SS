package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.MetalComponent;


public class MetalComponentFactory 
{
	 
	    public String getType() {
	        return "Metal";
	    }

	    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param)
	    {
	        
	        
	    }
}
