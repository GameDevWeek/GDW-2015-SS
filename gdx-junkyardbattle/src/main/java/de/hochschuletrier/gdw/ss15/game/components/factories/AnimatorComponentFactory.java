/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;

/**
 *
 * @author Julien Saevecke
 */
public class AnimatorComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "Animator";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        AnimatorComponent component = engine.createComponent(AnimatorComponent.class);
        //component.AnimatorComponent = assetManager.getAnimation(properties.getString("animation"));
        //assert (component.animation != null);
        entity.add(component);
    }
}
