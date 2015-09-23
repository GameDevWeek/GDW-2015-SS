package de.hochschuletrier.gdw.ss15.game.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.ss15.Main;

/**
 * <p>
 * A camera which is able to zoom and be bound to a rectangular region, for e.g. a map.</br>
 * The camera is also featuring a simulated simple damped spring.
 * </p>
 *
 * @author Sebastian Schalow
 * @version 0.2
 */

public class BoundedCamera extends SmoothCamera {
    
    float xMin, yMin, xMax, yMax;
    // use setBounds to use bounding
    boolean useBounds = false;
    
    // setting initial state
    private boolean resetZoom = true;

    // Camera zoom settings
    private float srcZoom = 1.f, dstZoom = 2.f, curZoom = 1.f, zoomSpeed = 0.75f, zoomProgress = 0.f;
    
    // < 1 slow follow || > 1 fast follow
    protected float followFactor = 1.f;
    
    private CVarFloat followFactCVAR = new CVarFloat("camFollow", followFactor, 0.1f, 20.f, 0, "sets camera following speed");
    private CVarFloat zoomSpeedCVAR = new CVarFloat("camZoomSpd", zoomSpeed, 0.1f, 5.f, 0, "sets camera zooming speed");
    private CVarFloat maxZoomOutCVAR = new CVarFloat("camZoomOut", dstZoom, 0.1f, 3.f, 0, "sets camera maxmimum zoom-out");
    
    //private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public BoundedCamera() {
        // registering CVARs
        Main.getInstance().console.register(followFactCVAR);
        Main.getInstance().console.register(zoomSpeedCVAR);
        Main.getInstance().console.register(maxZoomOutCVAR);
        
        // attach listeners
        followFactCVAR.addListener((CVar cvar) -> {
            followFactor = followFactCVAR.get();
        });
        
        zoomSpeedCVAR.addListener((CVar cvar) -> {
            zoomSpeed = zoomSpeedCVAR.get();
        });

        maxZoomOutCVAR.addListener((CVar cvar) -> {
            dstZoom = maxZoomOutCVAR.get();
        });
        
    }
    
    @Override
    public void finalize(){
        // unregistering CVars
        Main.getInstance().console.unregister(followFactCVAR);
        Main.getInstance().console.unregister(zoomSpeedCVAR);
        Main.getInstance().console.unregister(maxZoomOutCVAR);
    }
    
    @Override
    public void update(float delta) {
        moveDir.set(destination).sub(position);

        float distance = moveDir.len();
        if (distance < 1.f * followFactor) {
            setCameraPosition(destination);
            position.set(destination);
        } else {
            moveDir.scl(delta * followFactor * 10);
            position.add(moveDir);
            setCameraPosition(position);
        }
        
        if(resetZoom)
            zoomProgress -= zoomSpeed * delta;
        else
            zoomProgress += zoomSpeed * delta;
        checkProgressBounds();
        
        // change interpolation type for camera
        setZoom(Interpolation.pow4.apply(srcZoom, dstZoom, zoomProgress));        
        
        camera.update(true);
    }
    
    // needs to be used continously for zooming out
    // e.g. using chargingWeapon-Event in camera System
    // with parameter true for zooming out mode
    // for zoom-reset call once with false
    public void zoomOut(boolean out){
        if(out)
            resetZoom = false;
        else
            resetZoom = true;
    }
    
    // Check if zoomProgress variable is out of bounds
    private void checkProgressBounds(){
        if(zoomProgress > 1.f){
            zoomProgress = 1.f;
        } else if (zoomProgress < 0.f){
            zoomProgress = 0.f;
        }
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
