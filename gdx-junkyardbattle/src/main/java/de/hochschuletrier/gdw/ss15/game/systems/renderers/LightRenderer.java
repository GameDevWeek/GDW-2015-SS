package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ChainLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;

public class LightRenderer extends SortedSubIteratingSystem.SubSystem {

    @SuppressWarnings("unchecked")
    public LightRenderer() {
        super(Family.one(PointLightComponent.class, ChainLightComponent.class, ConeLightComponent.class).get());
    }

    @Override
    public void processEntity(Entity entity, float deltaTime){
        PositionComponent position = ComponentMappers.position.get(entity);
        PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        ChainLightComponent chainLight = ComponentMappers.chainLight.get(entity);
        ConeLightComponent coneLight = ComponentMappers.coneLight.get(entity);
        
        if(pointLight != null){
            pointLight.pointLight.setPosition((position.x+pointLight.offsetX)/GameConstants.BOX2D_SCALE, (position.y+pointLight.offsetY)/GameConstants.BOX2D_SCALE);
        }
        if(chainLight != null){
            chainLight.chainLight.setPosition((position.x+chainLight.offsetX)/GameConstants.BOX2D_SCALE,(position.y+chainLight.offsetY)/GameConstants.BOX2D_SCALE);
        }
        if(coneLight != null){
            coneLight.coneLight.setPosition((position.x+coneLight.offsetX)/GameConstants.BOX2D_SCALE, (position.y+coneLight.offsetY)/GameConstants.BOX2D_SCALE);
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
