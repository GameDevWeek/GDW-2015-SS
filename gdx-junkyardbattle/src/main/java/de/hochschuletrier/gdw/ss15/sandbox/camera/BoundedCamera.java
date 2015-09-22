package de.hochschuletrier.gdw.ss15.sandbox.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;

public class BoundedCamera extends SmoothCamera {
    
    float xMin, yMin, xMax, yMax;
    // factor < 1 slow follow || factor > 1 fast follow
    protected float factor = 0.3f;
    boolean useBounds = false;
    
    @Override
    public void update(float delta) {
        moveDir.set(destination).sub(position);

        float distance = moveDir.len();
        if (distance < 2.f * factor) {
            setCameraPosition(destination);
            position.set(destination);
        } else {
            moveDir.scl(delta * factor * 10);
            position.add(moveDir);
            setCameraPosition(position);
        }
        camera.update(true);
    }
    
    @Override
    public void setDestination(float x, float y) {

        if (useBounds) {
            destination.x = clamp(x, xMin, xMax, camera.viewportWidth);
            destination.y = clamp(y, yMin, yMax, camera.viewportHeight);
        } else {
            destination.x = x;
            destination.y = y;
        }
    }

    private float clamp(float in, float min, float max, float viewportSize) {
        if ((max - min) <= viewportSize) {
            return min + (max - min) / 2;
        }
        min += viewportSize / 2;
        if (in < min) {
            return min;
        }
        max -= viewportSize / 2;
        if (in > max) {
            return max;
        }
        return in;
    }

    public void setBounds(float xMin, float yMin, float xMax, float yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;

        useBounds = true;
    }

    @Override
    protected void onViewportChanged(float width, float height) {
        updateForced();
    }
    
    public void resetBounds() {
        useBounds = false;
    }

    public OrthographicCamera getOrthographicCamera() {
        return camera;
    }
    
    public void setSpringFollowFactor(float fac){
        factor = fac;
    }
     
}
