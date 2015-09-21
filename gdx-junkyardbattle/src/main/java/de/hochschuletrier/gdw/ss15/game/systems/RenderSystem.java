package de.hochschuletrier.gdw.ss15.game.systems;

import java.util.Comparator;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Filter;

import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.devcon.cvar.ICVarListener;
import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.AbstractCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.Settings;
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
    
    public RayHandler rayHandler;
	private Matrix4 scaleMatrix = new Matrix4();
    private final CVarFloat ambientLight = new CVarFloat("light_ambient", 0.5f, 0, 1, 0, "Ambient light value");
    private final CVarBool culling = new CVarBool("light_culling", true, 0, "Light Culling");
    private final CVarBool shadows = new CVarBool("light_shadows", true, 0, "Light Shadows");
    private final CVarInt blur = new CVarInt("light_blur", 2, 0, 3, 0, "Light blur value");
    private final DevConsole console;
    private final static RenderComparator renderComparator = new RenderComparator();
    
    private final OrthographicCamera camera;
    
    @SuppressWarnings("unchecked")
	public RenderSystem(RayHandler rayHandler, int priority, OrthographicCamera camera) {
        super(Family.all(PositionComponent.class).get(), renderComparator, priority);

        this.camera = camera;
        
        // Order of adding = order of renderer selection for the entity
        addSubSystem(new NormalMapRenderer());
        addSubSystem(new LightRenderer());
        
        this.rayHandler = rayHandler;
  
        this.rayHandler.setAmbientLight(GameConstants.LIGHT_AMBIENT);
        this.rayHandler.setBlur(GameConstants.LIGHT_BLUR);
        this.rayHandler.setBlurNum(GameConstants.LIGHT_BLURNUM);
        this.rayHandler.setShadows(GameConstants.LIGHT_SHADOW);
        RayHandler.useDiffuseLight(GameConstants.LIGHT_DIFFUSE);
        
//        Filter lightfilter = new Filter();
//        lightfilter.categoryBits = EntityCreator.WORLDSENSOR;
//        lightfilter.maskBits = (short) (EntityCreator.EVERYTHING & ~EntityCreator.WORLDSENSOR);
//        PointLight.setContactFilter(lightfilter);
//        ChainLight.setContactFilter(lightfilter);
//        ConeLight.setContactFilter(lightfilter);
        
        console = Main.getInstance().console;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        addCVar(ambientLight, this::onAmbientLightChange);
        addCVar(culling, this::onCullingChange);
        addCVar(blur, this::onBlurChange);
        addCVar(shadows, this::onShadowsChange);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        
        console.unregister(ambientLight);
        console.unregister(culling);
        console.unregister(blur);
        console.unregister(shadows);
    }

    private void addCVar(CVar cvar, ICVarListener listener) {
        console.register(cvar);
        cvar.addListener(listener);
        listener.modified(cvar);
    }
    
    private void onAmbientLightChange(CVar cvar) {
        rayHandler.setAmbientLight(ambientLight.get());
    }
    
    private void onCullingChange(CVar cvar) {
        rayHandler.setCulling(culling.get());
    }
    
    private void onBlurChange(CVar cvar) {
        rayHandler.setBlurNum(blur.get());
    }
    
    private void onShadowsChange(CVar cvar) {
        rayHandler.setShadows(shadows.get());
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
        
    	// rayHandler.updateAndRender() not allowed between begin() and end()
        if(Settings.LIGHTS.get()){
        	DrawUtil.safeEnd();
        	updateRayHandler();
        	DrawUtil.safeBegin();
        }
	}
    
    private void updateRayHandler(){
        scaleMatrix.set(camera.combined).scl(GameConstants.BOX2D_SCALE);
        rayHandler.setCombinedMatrix(scaleMatrix, 
                                     camera.position.x/GameConstants.BOX2D_SCALE, 
                                     camera.position.y/GameConstants.BOX2D_SCALE, 
                                     camera.viewportWidth*camera.zoom/GameConstants.BOX2D_SCALE, 
                                     camera.viewportHeight*camera.zoom/GameConstants.BOX2D_SCALE);

        rayHandler.updateAndRender(); 
    }

    public RayHandler getRayHandler(){
        return this.rayHandler;
    }
}


