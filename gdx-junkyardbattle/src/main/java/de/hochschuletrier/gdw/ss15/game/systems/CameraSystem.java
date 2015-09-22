package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.utils.Assert;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.rendering.BoundedCamera;
import de.hochschuletrier.gdw.ss15.sandbox.camera.PlayerComponent;

public class CameraSystem extends EntitySystem implements EntityListener {

    private BoundedCamera camera = new BoundedCamera();
    
    private Entity player;
        
    public CameraSystem() {
        // Resizing Camera to match window dimensions
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Registering camering for resize-Events
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
        camera.bind();        
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
    
    @Override
    public void entityRemoved(Entity entity) {}
    
    
    
}