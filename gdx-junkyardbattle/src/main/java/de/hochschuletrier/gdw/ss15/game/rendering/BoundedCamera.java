package de.hochschuletrier.gdw.ss15.game.rendering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.OrthographicCamera;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.rendering.ZoomingModes.modes;

/**
 * <p>
 * A camera which is able to zoom and be bound to a rectangular region, for e.g. a map.</br>
 * The camera is also featuring a simulated simple damped spring.
 * </p>
 *
 * @author Sebastian Schalow
 * @version 0.3
 */

public class BoundedCamera extends SmoothCamera {
    
    @SuppressWarnings("unused")
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    // use setBounds to use bounding
    float xMin, yMin, xMax, yMax;
    
    // use setBounds to use bounding
    private boolean useBounds = false;
    
    // Viewport settings
    boolean fixedViewport = true;
    int viewportX = 1920, viewportY = 1080;

    // Camera zoom settings
    protected float srcZoom = 1.f, dstZoom = 1.4f;
    protected float zoomSpeed = 0.75f, zoomProgress = 0.f;
    protected float curZoom = srcZoom, deadZone = .15f;
    private boolean resetZoom = true;
    protected modes mode = modes.pow5;
    
    // < 1 slow follow || > 1 fast follow
    protected float followFactor = 1.f;
    
    private CVarFloat followFactCVAR = new CVarFloat("cam_Follow", followFactor, 0.1f, 20.f, 0, "sets camera following speed");
    private CVarFloat zoomSpeedCVAR = new CVarFloat("cam_ZoomSpd", zoomSpeed, 0.1f, 5.f, 0, "sets camera zooming speed");
    private CVarFloat maxZoomOutCVAR = new CVarFloat("cam_ZoomOut", dstZoom, 0.1f, 3.f, 0, "sets camera maxmimum zoom-out");
    private CVarInt camViewpXCVar = new CVarInt("cam_ViewportX", viewportX, 640, 1920, 0, "set camera viewport x-value");
    private CVarInt camViewpYCVar = new CVarInt("cam_ViewportY", viewportY, 360, 1080, 0, "set camera viewport y-value");
    private CVarBool fixViewportCVAR = new CVarBool("cam_FixedVP", fixedViewport, 0, "toggle fixed viewport for camera");
    private CVarEnum<modes> zoomMode = new CVarEnum<modes>("cam_zoomMode", modes.pow5, modes.class, 0, "sets the zooming interpolation mode");
    
    public BoundedCamera() {
        
        // registering CVARs
        Main.getInstance().console.register(followFactCVAR);
        Main.getInstance().console.register(zoomSpeedCVAR);
        Main.getInstance().console.register(maxZoomOutCVAR);
        Main.getInstance().console.register(fixViewportCVAR);
        Main.getInstance().console.register(camViewpXCVar);
        Main.getInstance().console.register(camViewpYCVar);
        Main.getInstance().console.register(zoomMode);
        
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
        
        zoomMode.addListener((CVar cvar) -> {
            mode = zoomMode.get();
            System.out.println("mode nr.: " + mode);
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
        Main.getInstance().console.unregister(zoomMode);
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
            // change interpolation type for camera
            zoomProgress -= zoomSpeed * delta;
            curZoom = ZoomingModes.interpolate(mode, srcZoom, dstZoom, zoomProgress);
        } 
        
        setZoom(curZoom);        
        
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
    
    /**
    * used for zooming out while weapon is charging
    * @param zoomAmount has to be 0..1
    */
    public void zoomOut(float zoomAmount)
    {
    	if(zoomAmount < deadZone)
    		resetZoom = true;
    	else
    	{
    		resetZoom = false;
    		float converted = 0.0f;
    		if(deadZone < 1.0f)
    			converted = (zoomAmount - deadZone) / (1.0f-deadZone);
    		zoomProgress = converted;
    		curZoom = ZoomingModes.interpolate(mode, srcZoom, dstZoom, converted);
    	}
    }
    
    @Override
    public void setDestination(float x, float y) {
        if (useBounds) {
            //xMin = viewportX / 2 / (camera.zoom) * (camera.zoom - 1) + (camera.zoom - 1) * width / 6;
            //yMin = viewportY / 2 / (camera.zoom) * (camera.zoom - 1) + (camera.zoom - 1) * height / 10.5f;
            destination.x = clamp(x, xMin, xMax, camera.viewportWidth / curZoom);
            destination.y = clamp(y, yMin, yMax, camera.viewportHeight / curZoom);
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

    public void setViewport(boolean fixed){
        fixedViewport = fixed;
    }
    
    public void setFollowFactor(float fac){
        followFactor = fac;
    }
    
    @Override
    public void resize(int width, int height) {
        if(!fixedViewport){
            camera.setToOrtho(true, width, height);
        } else {
            camera.setToOrtho(true, viewportX, viewportY);
        }
    }

    public OrthographicCamera getOrthographicCamera() {
        return camera;
    }
     
}
