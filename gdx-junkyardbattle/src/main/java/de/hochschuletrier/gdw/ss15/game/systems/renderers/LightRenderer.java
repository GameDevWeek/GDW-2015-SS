package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.devcon.cvar.ICVarListener;
import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.Settings;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ChainLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;

public class LightRenderer extends SortedSubIteratingSystem.SubSystem {
    private Matrix4 scaleMatrix = new Matrix4();
    private final CVarFloat ambientLight = new CVarFloat("light_ambient", GameConstants.LIGHT_AMBIENT, 0, 1, 0, "Ambient light value");
    private final CVarBool culling = new CVarBool("light_culling", true, 0, "Light Culling");
    private final CVarBool shadows = new CVarBool("light_shadows", true, 0, "Light Shadows");
    private final CVarInt blur = new CVarInt("light_blur", 2, 0, 3, 0, "Light blur value");
    private final DevConsole console;
    
    public static RayHandler rayHandler;
    
    @SuppressWarnings({ "unchecked", "static-access" })
    public LightRenderer(RayHandler rayHandler) {
        super(Family.one(PointLightComponent.class, ChainLightComponent.class, ConeLightComponent.class).get());
        this.rayHandler = rayHandler;
        
        rayHandler.setAmbientLight(GameConstants.LIGHT_AMBIENT);
        rayHandler.setBlur(GameConstants.LIGHT_BLUR);
        rayHandler.setBlurNum(GameConstants.LIGHT_BLURNUM);
        rayHandler.setShadows(GameConstants.LIGHT_SHADOW);
        RayHandler.useDiffuseLight(GameConstants.LIGHT_DIFFUSE);
        
//      Filter lightfilter = new Filter();
//      lightfilter.categoryBits = EntityCreator.WORLDSENSOR;
//      lightfilter.maskBits = (short) (EntityCreator.EVERYTHING & ~EntityCreator.WORLDSENSOR);
//      PointLight.setContactFilter(lightfilter);
//      ChainLight.setContactFilter(lightfilter);
//      ConeLight.setContactFilter(lightfilter);
      
      console = Main.getInstance().console;
      addCVar(ambientLight, this::onAmbientLightChange);
      addCVar(culling, this::onCullingChange);
      addCVar(blur, this::onBlurChange);
      addCVar(shadows, this::onShadowsChange);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime){
        PositionComponent position = ComponentMappers.position.get(entity);
        PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        ChainLightComponent chainLight = ComponentMappers.chainLight.get(entity);
        ConeLightComponent coneLight = ComponentMappers.coneLight.get(entity);
        
        if(pointLight != null){
            pointLight.pointLight.setPosition((position.x + pointLight.offsetX) / GameConstants.BOX2D_SCALE, 
                                              (position.y + pointLight.offsetY) / GameConstants.BOX2D_SCALE);
        }
        if(chainLight != null){
            chainLight.chainLight.setPosition((position.x + chainLight.offsetX) / GameConstants.BOX2D_SCALE,
                                              (position.y + chainLight.offsetY) / GameConstants.BOX2D_SCALE);
        }
        if(coneLight != null){
            coneLight.coneLight.setPosition((position.x + coneLight.offsetX) / GameConstants.BOX2D_SCALE, 
                                            (position.y + coneLight.offsetY) / GameConstants.BOX2D_SCALE);
        }
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
    
    public void render(OrthographicCamera camera){
        // rayHandler.updateAndRender() not allowed between begin() and end()
        if(Settings.LIGHTS.get()){
            DrawUtil.safeEnd();

            scaleMatrix.set(camera.combined).scl(GameConstants.BOX2D_SCALE);
            rayHandler.setCombinedMatrix(scaleMatrix, 
                                         camera.position.x/GameConstants.BOX2D_SCALE, 
                                         camera.position.y/GameConstants.BOX2D_SCALE, 
                                         camera.viewportWidth*camera.zoom/GameConstants.BOX2D_SCALE, 
                                         camera.viewportHeight*camera.zoom/GameConstants.BOX2D_SCALE);

            rayHandler.updateAndRender(); 
            
            DrawUtil.safeBegin();
        }
    }

    public static void setLightsActive(Entity entity, boolean active) {
        PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        ChainLightComponent chainLight = ComponentMappers.chainLight.get(entity);
        ConeLightComponent coneLight = ComponentMappers.coneLight.get(entity);
        
        if(pointLight != null)
            pointLight.pointLight.setActive(active);
        
        if(chainLight != null)
            chainLight.chainLight.setActive(active);
        
        if(coneLight != null)
            coneLight.coneLight.setActive(active);
    }  
  
}
