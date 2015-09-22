package de.hochschuletrier.gdw.ss15.game.components.light;

import box2dLight.ConeLight;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.LightRenderer;

public class ConeLightComponent extends Component implements Pool.Poolable {
    public ConeLight coneLight = new ConeLight(LightRenderer.rayHandler,
            GameConstants.LIGHT_RAYS, null, 1.f, 0.f, 0.f, 0.f, 0.f);
    public float offsetX = 0;
    public float offsetY = 0;

    public void set(Color color, float distance, float directionDegree, float coneDegree, 
            float offsetX, float offsetY, boolean isStatic, boolean active) {

        coneLight.setColor(color);
        coneLight.setActive(active);
        coneLight.setDistance(distance);
        coneLight.setStaticLight(isStatic);
        coneLight.setDirection(directionDegree);
        coneLight.setConeDegree(coneDegree);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void set(Color color, float distance, boolean isStatic, float directionDegree, float coneDegree,
            boolean active) {
        set(color, distance, directionDegree, coneDegree, 0.f, 0.f, isStatic, active);
    }

    public void set(Color color, float distance, float directionDegree, float coneDegree) {
        set(color, distance, directionDegree, coneDegree,0.f, 0.f, true, true);
    }

    public void setOffset(float x, float y) {
        offsetX = x;
        offsetY = y;
    }

    @Override
    public void reset() {
        offsetX = offsetY = 0;

        coneLight.setActive(false);
        coneLight.setStaticLight(false);
        coneLight.setXray(false);
    }
}
