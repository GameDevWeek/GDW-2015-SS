/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.factories.animation;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;

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

        for(AnimationState state : AnimationState.values()){
            
            String animationStateString = "animation_" + state.toString();
            String animation = properties.getString(animationStateString, null);
            
            if( animation != null){
                component.animationStates.put(state, assetManager.getAnimation(animation));
            }
        }
        
        entity.add(component);
    }
}
