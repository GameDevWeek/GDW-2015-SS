package de.hochschuletrier.gdw.ss15.game.components.light;

import com.badlogic.ashley.core.Component;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.systems.RenderSystem;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.LightRenderer;

/**
 * @author Dennis, compie
 *
 */
public class PointLightComponent extends Component implements Pool.Poolable {
    public PointLight pointLight = new PointLight(LightRenderer.rayHandler, GameConstants.LIGHT_RAYS);
    public float offsetX = 0;
    public float offsetY = 0;

    public void set(Color color, float distance, float offsetX, 
                    float offsetY, boolean isStatic, boolean active) {

        pointLight.setColor(color);
        pointLight.setActive(active);
        pointLight.setDistance(distance);
        pointLight.setStaticLight(isStatic);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    public void set(Color color, float distance, boolean isStatic, boolean active) {
        set(color, distance, 0.f, 0.f, isStatic, active);
    }
    
    public void set(Color color, float distance) {
        set(color, distance, 0.f, 0.f, true, true);
    }

    public void setOffset(float x, float y) {
        offsetX = x;
        offsetY = y;
    }
    
    @Override
    public void reset() {
        offsetX = offsetY = 0;
        
        pointLight.setActive(false);
        pointLight.setStaticLight(false);
        pointLight.setXray(false);
    }
}
