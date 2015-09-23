package de.hochschuletrier.gdw.ss15.game.systems;

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

/**
 *
 * <p>
 * The camera-system coordinates the camera and any interaction with it.
 * To connect the camera to a player entity this entity needs a <i>PlayerComponent</i>
 * with its attribute <i>isLocalPlayer</i> set to true.
 * </p>
 * @see {@link BoundedCamera}
 * @version 0.2
 * @author Sebastian Schalow
 * 
 */

public class CameraSystem extends EntitySystem implements EntityListener, WeaponCharging.Listener, WeaponUncharged.Listener {

    private BoundedCamera camera = new BoundedCamera();    
    private Entity player;
    
    //private final Logger logger = LoggerFactory.getLogger(getClass());
        
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
        camera.updateForced();
    }

    @Override
    public void finalize(){
        // Deregistering camera ScreenLister
        Main.getInstance().removeScreenListener(camera);
    }
    
    public final BoundedCamera getCamera(){
        return camera;
    }
    
    public void setCameraBounds(float minX, float minY, float maxX, float maxY){
        // Set Camera Bounds to a matching region (e.g. map size)
        // without using this method bounding is disabled
        camera.setBounds(minX, minY, maxX, maxY);
        camera.updateForced();
    }
    
    @Override
    public void update(float deltaTime) {
        // if player dies / not existent
        if(player == null)
            return;
        
        PositionComponent posComp = ComponentMappers.position.get(player);
        if(posComp != null){
            camera.setDestination(posComp.x, posComp.y);  
        }                

        camera.update(deltaTime);
        camera.bind();        
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