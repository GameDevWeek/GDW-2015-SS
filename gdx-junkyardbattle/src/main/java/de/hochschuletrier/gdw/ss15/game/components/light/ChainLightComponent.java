package de.hochschuletrier.gdw.ss15.game.components.light;

import box2dLight.ChainLight;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.LightRenderer;

public class ChainLightComponent extends Component implements Pool.Poolable { 
    public ChainLight chainLight = new ChainLight(LightRenderer.rayHandler, GameConstants.LIGHT_RAYS, 
            null, 0.f, 1);
    
    public float offsetX = 0;
    public float offsetY = 0;

    @Override
    public void reset() {
        offsetX = offsetY = 0;
        
        chainLight.setActive(false);
        chainLight.setStaticLight(false);
        chainLight.setXray(false);
    }
}