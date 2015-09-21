package de.hochschuletrier.gdw.ss15.sandbox.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.AbstractCamera;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class CameraSystem extends EntitySystem implements EntityListener {

    private LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Entity player;
    private Vector2 playerPos = new Vector2();
        
    public CameraSystem() {
        // TODO Auto-generated constructor stub
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setBounds(0, 0, 200, 200);
        camera.updateForced();
        Main.getInstance().addScreenListener(camera);
    }
    
    public final LimitedSmoothCamera getCamera(){
        return camera;
    }
    
    public void setCameraBounds(float minX, float minY, float maxX, float maxY){
        camera.setBounds(minX, minY, maxX, maxY);
    }    
    
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        camera.bind();
        camera.update(deltaTime);

        //PositionComponent pcomp = ComponentMappers.position.get(player);
        PhysixBodyComponent pcomp = ComponentMappers.physixBody.get(player);
        if(pcomp != null){
            playerPos.set(pcomp.getPosition());
            logger.debug("New Player pos: {} | {}", pcomp.getX(), pcomp.getY());
        }
        camera.setDestination(playerPos);    
    }

    @Override
    public void entityAdded(Entity entity) {
        logger.debug("Entity with PlayerComp added");
        player = entity;
    }

    @Override
    public void entityRemoved(Entity entity) {        
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(PlayerComponent.class).get();
        engine.addEntityListener(family, this);
    }
}
