package de.hochschuletrier.gdw.ss15.game.components.factories.light;

import box2dLight.PointLight;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.utils.RenderUtil;

/**
 * <pre>
 * .json Example:
 * {@code
 *   "greenPointLight": {
 *      "meta": {},
 *      "components": {
 *          "Position": {},
 *          "PointLight": {
 *              "color": "0, 1, 0",
 *              "distance": 10,
 *              "static": true,
 *              "xRay": true
 *          }
 *      }
 *   }
 * </pre>   
 *  
 *  Parameter "static": Won't be updated -> no dynamic shadows -> performance boost <br>
 *  Parameter "xRay": No shadows -> performance boost <br>
 *  
 * @author compie
 * 
 */
public class PointLightComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "PointLight";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        PointLightComponent component = engine.createComponent(PointLightComponent.class);
        PointLight light = component.pointLight;
        
        component.setOffset(properties.getFloat("offsetX", 0.f), properties.getFloat("offsetY", 0.f));
        light.setColor(RenderUtil.extractColor(properties.getString("color", null)));
        light.setActive(properties.getBoolean("active", true));
        light.setXray(properties.getBoolean("xRay", false));
        light.setStaticLight(properties.getBoolean("static", false));
        light.setSoft(properties.getBoolean("soft", true));
        light.setDistance(properties.getFloat("distance", 5.f));
        
        entity.add(component);
    }
}
