package de.hochschuletrier.gdw.ss15.game.components.light;

import box2dLight.DirectionalLight;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.LightRenderer;

public class DirectionalLightComponent extends Component implements Pool.Poolable { 
    public DirectionalLight directionalLight = new DirectionalLight(LightRenderer.rayHandler, 
            GameConstants.LIGHT_RAYS, null, 0.f);
    public float offsetX = 0;
    public float offsetY = 0;

    @Override
    public void reset() {
        offsetX = offsetY = 0;
        
        directionalLight.setActive(false);
        directionalLight.setStaticLight(false);
        directionalLight.setXray(false);
    }
}
