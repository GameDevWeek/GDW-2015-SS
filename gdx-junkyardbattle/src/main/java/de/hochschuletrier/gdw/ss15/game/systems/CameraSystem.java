package de.hochschuletrier.gdw.ss15.game.systems;

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

public class CameraSystem extends EntitySystem implements EntityListener, WeaponCharging.Listener, WeaponUncharged.Listener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private BoundedCamera camera = new BoundedCamera();
    
    private Entity player;
        
    public CameraSystem() {
        // Resizing Camera to match window dimensions
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Registering camering for resize-Events
        WeaponCharging.register(this);
        WeaponUncharged.register(this);
        Main.getInstance().addScreenListener(camera);
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
        camera.setBounds(minX, minY, maxX, maxY);
        camera.updateForced();
    }
    
    @Override
    public void update(float deltaTime) {
        if(player == null)
            return;
        
        PositionComponent posComp = ComponentMappers.position.get(player);
        if(posComp != null){
            camera.setDestination(posComp.x, posComp.y);  
        }                

        camera.update(deltaTime);
        //camera.bind();        
    }

    @Override
    public void entityAdded(Entity entity) {
        if(entity.getComponent(PlayerComponent.class).isLocalPlayer){
            player = entity;
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        @SuppressWarnings("unchecked")
        Family family = Family.all(PlayerComponent.class).get();
        engine.addEntityListener(family, this);
    }
    
    private void zoomOut(){
        camera.zoomOut(true);
    }
    
    private void resetZoom(){
        camera.zoomOut(false);
    }
    
    @Override
    public void entityRemoved(Entity entity) {}

    @Override
    public void onWeaponCharging() {
        // TODO Auto-generated method stub
        logger.debug("Weapon is charging");        
        zoomOut();
    }

    @Override
    public void onWeaponUncharged() {
        // TODO Auto-generated method stub
        resetZoom();        
    }
    
    
    
}