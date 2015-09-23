package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.rendering.TileMapCreator;

/**
 * 
 * All Entities that have to be rendered require a PositionComponent. <br>
 * If at least one of them is not provided the Entity won't be rendered.
 *
 */
public class RenderSystem extends SortedSubIteratingSystem {
    private static final class RenderComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            PositionComponent p1 = ComponentMappers.position.get(e1);
            PositionComponent p2 = ComponentMappers.position.get(e2);
            
            return p1.layer - p2.layer;
        }
    }

    private final static RenderComparator renderComparator = new RenderComparator();
    
    private final OrthographicCamera camera;
    private final LightRenderer lightRenderer;
    private final TileMapCreator tileMapCreator = new TileMapCreator();
    private final FogRenderer fogRenderer = new FogRenderer();
    
    @SuppressWarnings("unchecked")
	public RenderSystem(PhysixSystem physixSystem, OrthographicCamera camera) {
        super(Family.all(PositionComponent.class).get(), renderComparator, GameConstants.PRIORITY_RENDER_SYSTEM);

        this.camera = camera;
        
        lightRenderer = new LightRenderer(new RayHandler(physixSystem.getWorld()));
        
        // Order of adding = order of renderer selection for the entity
        addSubSystem(new TextureRenderer());
        addSubSystem(new AnimatorRenderer());
        addSubSystem(new NormalMapRenderer());
        addSubSystem(lightRenderer);
    }
    
    public TileMapCreator getTileMapCreator() {
        return tileMapCreator;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        tileMapCreator.init((PooledEngine) (engine));
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }
    
	@Override
	public void entityRemoved (Entity entity) {
		super.entityRemoved(entity);
		forceSort(); // sort is needed after an entity is removed!
	}

    @Override
    public void processEntity(Entity entity, float deltaTime) {
    	if(!ComponentMappers.normalMap.has(entity)) 
    		DrawUtil.setShader(null);
    	
    	super.processEntity(entity, deltaTime);
    }
    
    @Override
	public void update (float deltaTime) {
        
        super.update(deltaTime);
        lightRenderer.render(camera);
//        fogRenderer.preRender();
	}
}


