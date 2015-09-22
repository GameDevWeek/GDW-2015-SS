package de.hochschuletrier.gdw.ss15.game.components.factories.light;

import box2dLight.ConeLight;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.utils.RenderUtil;

/**
 * <pre>
 * .json Example:
 * {@code
 *   "blueConeLight": {
 *      "meta": {},
 *      "components": {
 *          "Position": {},
 *          "ConeLight": {
 *              "color": "0, 0, 1",
 *              "distance": 10,
 *              "static": true,
 *              "xRay": true,
 *              "coneDegree": 45,
 *              "degree": 45
 *          }
 *      }
 *   }
 * </pre>   
 *  
 *  Parameter "static": Won't be updated -> no dynamic shadows -> performance boost <br>
 *  Parameter "xRay": No shadows -> performance boost <br>
 *  Parameter "degree": direction in degree in [0, 360]
 *  
 * @author compie
 * 
 */
public class ConeLightComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "ConeLight";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        ConeLightComponent component = engine.createComponent(ConeLightComponent.class);
        ConeLight light = component.coneLight;
        
        component.setOffset(properties.getFloat("offsetX", 0.f), properties.getFloat("offsetY", 0.f));
        light.setColor(RenderUtil.extractColor(properties.getString("color", null)));
        light.setActive(properties.getBoolean("active", true));
        light.setXray(properties.getBoolean("xRay", false));
        light.setStaticLight(properties.getBoolean("static", false));
        light.setSoft(properties.getBoolean("soft", true));
        light.setDistance(properties.getFloat("distance", 5.f));
        
        light.setDirection(properties.getFloat("degree", 0.f));
        light.setConeDegree(properties.getFloat("coneDegree", 0.f));
        
        entity.add(component);
    }
}

