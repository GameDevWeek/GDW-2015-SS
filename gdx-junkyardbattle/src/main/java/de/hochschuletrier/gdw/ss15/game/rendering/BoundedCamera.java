package de.hochschuletrier.gdw.ss15.game.rendering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;

public class BoundedCamera extends SmoothCamera {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    float xMin, yMin, xMax, yMax;
    float newZoom = 1.0f;
    float curZoom = 1.0f;
    float zoomProgress = 0.f;
    // factor < 1 slow follow || factor > 1 fast follow
    protected float followFactor = 10.f;
    boolean useBounds = false;
    
    @Override
    public void update(float delta) {
        moveDir.set(destination).sub(position);

        float distance = moveDir.len();
        if (distance < 2.f * followFactor) {
            setCameraPosition(destination);
            position.set(destination);
        } else {
            moveDir.scl(delta * followFactor * 10);
            position.add(moveDir);
            setCameraPosition(position);
        }
        
        if(zoomProgress < 1.f){
            curZoom = Interpolation.fade.apply(curZoom, newZoom, zoomProgress);
            zoomProgress += 0.001f;
            setZoom(curZoom);            
        }
        
        camera.update(true);
    }
    
    public void zoom(float newZoom){           
        this.newZoom += newZoom;
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
        camera.viewportWidth *= curZoom;
        camera.viewportHeight *= curZoom;
        updateForced();
    }
    
    public void resetBounds() {
        useBounds = false;
    }

    public OrthographicCamera getOrthographicCamera() {
        return camera;
    }
    
    public void setSpringFollowFactor(float fac){
        followFactor = fac;
    }
     
}
