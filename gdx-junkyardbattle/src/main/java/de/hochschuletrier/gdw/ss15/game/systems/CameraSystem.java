package de.hochschuletrier.gdw.ss15.game.systems;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.rendering.BoundedCamera;
import de.hochschuletrier.gdw.ss15.events.WeaponCharging;
import de.hochschuletrier.gdw.ss15.events.WeaponUncharged;
import de.hochschuletrier.gdw.ss15.events.SatelliteColliding;

/**
 *
 * <p>
 * The camera-system coordinates the camera and any interaction with it.
 * To connect the camera to a player entity this entity needs a <i>PlayerComponent</i>
 * with its attribute <i>isLocalPlayer</i> set to true.</br>
 * The system features camera shaking over given time with given force.
 * </p>
 * @see {@link BoundedCamera}
 * @version 0.3
 * @author Sebastian Schalow
 * 
 */

public class CameraSystem extends EntitySystem 
             implements EntityListener, WeaponCharging.Listener, WeaponUncharged.Listener, SatelliteColliding.Listener {

    private BoundedCamera camera = new BoundedCamera();    
    private CameraRumble rumble = new CameraRumble();
    private Entity player;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
        
    public CameraSystem() {
        // Resizing Camera to match window dimensions
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Registering camering for resize-Events
        Main.getInstance().addScreenListener(camera);
        // TODO:
        // to be replaced by future implementation of
        // corresponding events by input / gamelogic group        
        WeaponCharging.register(this);
        WeaponUncharged.register(this);
        SatelliteColliding.register(this);
        camera.updateForced();
    }

    @Override
    public void finalize(){
        // Deregistering camera ScreenLister
        Main.getInstance().removeScreenListener(camera);
        WeaponCharging.unregister(this);
        WeaponUncharged.unregister(this);
        SatelliteColliding.unregister(this);
    }
    
    public final BoundedCamera getCamera(){
        return camera;
    }
    
    public void setCameraBounds(float minX, float minY, float maxX, float maxY){
        // Set Camera Bounds to a matching region (e.g. map size)
        // without using this method bounding is disabled
        // ONLY WORKING WITH FIXED ZOOM LEVEL OF 1.0
        camera.setBounds(minX, minY, maxX, maxY);
        camera.updateForced();
    }
    
    // Fixes the viewport size
    public void setFixedViewport(boolean fixed){
        camera.setFixedViewport(fixed);
    }
    
    @Override
    public void update(float deltaTime) {
        // if player dies / is not existent
        if(player == null)
            return;
        
        PositionComponent posComp = ComponentMappers.position.get(player);
        if(posComp != null){
            camera.setDestination(posComp.x, posComp.y);  
        }                

        if(rumble.time != 0.f){
            rumble.tick(deltaTime, camera, player);
        }
        
        camera.update(deltaTime);
        camera.bind();        
    }

    // Initializes a camera shake
    public void initializeShake(float time, float force){
        rumble.rumble(force, time);
    }
    
    @Override
    public void onWeaponCharging() { 
        camera.zoomOut(true);
    }

    @Override
    public void onWeaponUncharged() {
        camera.zoomOut(false);        
    }
    
    @Override
    public void onSatelliteColliding() {
        initializeShake(1, 80);   
        logger.info("initializing shake");
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        @SuppressWarnings("unchecked")
        Family family = Family.all(PlayerComponent.class).get();
        engine.addEntityListener(family, this);
    }
    
    @Override
    public void entityAdded(Entity entity) {
        if(entity.getComponent(PlayerComponent.class).isLocalPlayer){
            player = entity;
        }
    }
    
    @Override
    public void entityRemoved(Entity entity) {}
    
}

// CameraSystem only used classes
class CameraRumble {

    public float time;
    Random random;
    float x, y;
    float current_time;
    float power;
    float current_power;

    public CameraRumble(){
      time = 0;
      current_time = 0;
      power = 0;
      current_power = 0;
    }
    
    // Call this function with the force of the shake 
    // and how long it should last      
    public void rumble(float power, float time) {
      random = new Random();
      this.power = power;
      this.time = time;
      this.current_time = 0;
    }
          
    public void tick(float delta, BoundedCamera camera, Entity player){
        // BoundedCamera is the camera attached in CameraSystem
        // player is the character centre screen
        // who has an PositionComponent
      
        PositionComponent poscomp = ComponentMappers.position.get(player);
        
        if(current_time <= time) {
            current_power = power * ((time - current_time) / time);
            // generate random new x and y values taking into account
            // how much force was passed in
            x = (random.nextFloat() - 0.5f) * 2 * current_power;
            y = (random.nextFloat() - 0.5f) * 2 * current_power;
        
            // Set the camera to this new x/y position           
            camera.setDestination(poscomp.x-x, poscomp.y-y);
            current_time += delta;
        } else {
            // When the shaking is over move the camera back to the player position
            camera.setDestination(poscomp.x, poscomp.y);
            time = 0.f;
        }
    }      
}