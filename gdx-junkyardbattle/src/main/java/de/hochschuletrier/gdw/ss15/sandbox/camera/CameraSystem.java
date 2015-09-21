package de.hochschuletrier.gdw.ss15.sandbox.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.utils.Assert;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class CameraSystem extends EntitySystem implements EntityListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private LimitedSmoothCamera camera = new LimitedSmoothCamera();
    
    private Entity player;
    // TODO: fehlenden Player handlen?
        
    public CameraSystem() {
        // Resizing Camera to match window dimensions
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Registering camering for resize-Events
        Main.getInstance().addScreenListener(camera);
        camera.updateForced();
    }
    
    public void bind(){
        camera.bind();
    }
    
    public final LimitedSmoothCamera getCamera(){
        return camera;
    }
    
    public void setCameraBounds(float minX, float minY, float maxX, float maxY){
        // Set Camera Bounds to a matching region (e.g. map size)
        camera.setBounds(minX, minY, maxX, maxY);
        camera.updateForced();
    }
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // TODO: Update der Superklasse ohne Effekt?
        camera.update(deltaTime);

        PositionComponent posComp = ComponentMappers.position.get(player);
        if(posComp != null){
            camera.setDestination(posComp.x, posComp.y);  
        }
        
    }

    @Override
    public void entityAdded(Entity entity) {
        logger.debug("Entity with PlayerComponent added to Engine!");
        Assert.that(player == null, "Only one Entity with PlayerComponent allowed at same time!");
        if(entity != null){
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
    
    // TODO: Abmeldung des Listeners benötigt? Wenn ja, dann hier mit custom dispose-Methode?
    public void dispose(){
        Main.getInstance().removeScreenListener(camera);
    }
    
}