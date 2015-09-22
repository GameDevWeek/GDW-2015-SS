package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import de.hochschuletrier.gdw.ss15.game.systems.renderers.AnimatorRenderer;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.TextureRenderer;
import java.util.Comparator;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.LightRenderer;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.NormalMapRenderer;

/**
 * 
 * All Entities that have to be rendered require a PositionComponent and a LayerComponent. <br>
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
    
    @SuppressWarnings("unchecked")
	public RenderSystem(RayHandler rayHandler, OrthographicCamera camera) {
        super(Family.all(PositionComponent.class).get(), renderComparator, GameConstants.PRIORITY_RENDER_SYSTEM);

        this.camera = camera;
        
        lightRenderer = new LightRenderer(rayHandler);
        
        // Order of adding = order of renderer selection for the entity
        addSubSystem(new TextureRenderer());
        addSubSystem(new AnimatorRenderer());
        addSubSystem(new NormalMapRenderer());
        addSubSystem(lightRenderer);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
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
	}
}


