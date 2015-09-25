package de.hochschuletrier.gdw.ss15.game.rendering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
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
    
    // use setBounds to use bounding
    boolean useBounds = false;
    float xMin, yMin, xMax, yMax;
    
    // Viewport settings
    boolean fixedViewport = true;
    int viewportX = 1920, viewportY = 1080;

    // Camera zoom settings
    private float srcZoom = 1.f, dstZoom = 2.f, curZoom = 1.f, zoomSpeed = 0.75f, zoomProgress = 0.f;
    private boolean resetZoom = true;
    
    // < 1 slow follow || > 1 fast follow
    protected float followFactor = 1.f;
    
    private CVarFloat followFactCVAR = new CVarFloat("cam_Follow", followFactor, 0.1f, 20.f, 0, "sets camera following speed");
    private CVarFloat zoomSpeedCVAR = new CVarFloat("cam_ZoomSpd", zoomSpeed, 0.1f, 5.f, 0, "sets camera zooming speed");
    private CVarFloat maxZoomOutCVAR = new CVarFloat("cam_ZoomOut", dstZoom, 0.1f, 3.f, 0, "sets camera maxmimum zoom-out");
    private CVarInt camViewpXCVar = new CVarInt("cam_ViewportX", viewportX, 640, 1920, 0, "set camera viewport x-value");
    private CVarInt camViewpYCVar = new CVarInt("cam_ViewportY", viewportY, 360, 1080, 0, "set camera viewport y-value");
    private CVarBool fixViewportCVAR = new CVarBool("cam_FixedVP", fixedViewport, 0, "toggle fixed viewport for camera");
    
    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    public BoundedCamera() {
        
        // registering CVARs
        Main.getInstance().console.register(followFactCVAR);
        Main.getInstance().console.register(zoomSpeedCVAR);
        Main.getInstance().console.register(maxZoomOutCVAR);
        Main.getInstance().console.register(fixViewportCVAR);
        Main.getInstance().console.register(camViewpXCVar);
        Main.getInstance().console.register(camViewpYCVar);
        
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
        
        fixViewportCVAR.addListener((CVar cvar) -> {
           fixedViewport = !fixedViewport; 
        });
        
        camViewpXCVar.addListener((CVar cvar) -> {
           viewportX = camViewpXCVar.get();
           resize(viewportX, viewportY);
        });
        
        camViewpYCVar.addListener((CVar cvar) -> {
            viewportY = camViewpYCVar.get();
            resize(viewportX, viewportY);
         });
        
    }
    
    @Override
    public void finalize(){
        // unregistering CVars
        Main.getInstance().console.unregister(followFactCVAR);
        Main.getInstance().console.unregister(zoomSpeedCVAR);
        Main.getInstance().console.unregister(maxZoomOutCVAR);
        Main.getInstance().console.unregister(fixViewportCVAR);
        Main.getInstance().console.unregister(camViewpXCVar);
        Main.getInstance().console.unregister(camViewpYCVar);
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
        {
            zoomProgress -= zoomSpeed * delta;
            checkProgressBounds();
            curZoom = Interpolation.pow4.apply(srcZoom, dstZoom, zoomProgress);
            setZoom(curZoom);
        }
//        else
//            zoomProgress += zoomSpeed * delta;
        
        
        // change interpolation type for camera
                
        
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
    
    
    //used for zooming out while weapon charing
    //value between 0..1
    public void zoomOut(float zoomAmount)
    {
    	if(zoomAmount <= 0.1f)
    		resetZoom = true;
    	else
    	{
    		resetZoom = false;
    		zoomProgress = zoomAmount;
    		setZoom(srcZoom + zoomAmount*(dstZoom-srcZoom));
    	}
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

    public void setFixedViewport(boolean fixed){
        fixedViewport = fixed;
    }
    
    @Override
    public void resize(int width, int height) {
        if(!fixedViewport){
            camera.setToOrtho(true, width, height);
        } else {
            camera.setToOrtho(true, viewportX, viewportY);
        }
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
