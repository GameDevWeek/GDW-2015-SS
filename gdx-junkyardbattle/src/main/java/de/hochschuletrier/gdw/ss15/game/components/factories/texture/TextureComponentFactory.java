/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.factories.texture;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;

/**
 *
 * @author Julien Saevecke
 */
public class TextureComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Texture";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        TextureComponent component = engine.createComponent(TextureComponent.class);
        component.texture = assetManager.getTexture(properties.getString("texture"));
        component.width = properties.getInt("width", 24);
        component.height = properties.getInt("height", 24);
        component.srcX = properties.getInt("srcX", 0);
        component.srcY = properties.getInt("srcY", 0);
        component.scaleX = properties.getFloat("scaleX", 1.0f);
        component.scaleY = properties.getFloat("scaleY", 1.0f);
        component.flipX = properties.getBoolean("flipX", false);
        component.flipY = properties.getBoolean("flipY", false);
        component.offsetX = properties.getFloat("offsetX", 0.f);
        component.offsetY = properties.getFloat("offsetY", 0.f);
        component.initialRotation = properties.getFloat("initialRotation", 0);
        
        assert(component.texture != null);
        
        entity.add(component);
    }
}
