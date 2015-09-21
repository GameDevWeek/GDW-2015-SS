package de.hochschuletrier.gdw.ss15.game.components.light;

import com.badlogic.ashley.core.Component;

import box2dLight.PointLight;
import com.badlogic.gdx.utils.Pool;

/**
 * Implement this Light like this:
 *  PointLightComponent pl = engine.createComponent(PointLightComponent.class);
 *  pl.pointLight = new PointLight(engine.getSystem(SortedRenderSystem.class).getRayHandler(), 
 *                                 GameConstants.LIGHT_RAYS, 
 *                                 new Color(float r,float g,float b, float a), 
 *                                 float distance, 
 *                                 0, 
 *                                 0);    
 * @author Dennis
 *
 */

public class PointLightComponent extends Component implements Pool.Poolable
{ 
    public PointLight pointLight;
    public float offsetX = 0;
    public float offsetY = 0;

    @Override
    public void reset()
    {
    	if(pointLight != null) {
            pointLight.remove();
            pointLight.dispose();
            pointLight = null;
    	}

        offsetX = 0;
        offsetY = 0;
    }
}
