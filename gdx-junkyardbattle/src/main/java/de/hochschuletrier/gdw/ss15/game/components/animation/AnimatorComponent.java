/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.components.animation;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import java.util.HashMap;

/**
 *
 * @author Julien Saevecke
 */
public class AnimatorComponent extends Component implements Pool.Poolable
{
    public HashMap<AnimationState, AnimationExtended> animationStates = new HashMap<>();
    public AnimationState animationState = AnimationState.IDLE;
    public float stateTime = 0;
    public float initialRotation = 0;
    public boolean draw = true;
    public float rotation = 0.f;
    public float positionOffsetX = 0;
    public float positionOffsetY = 0;
    public float scaleX = 1.f;
    public float scaleY = 1.f;
    
    public void changeAnimationState(AnimationState state)
    {
        animationState = state;
        stateTime = 0;
    }
    
    @Override
    public void reset() 
    {
        animationStates.clear();
        animationState = AnimationState.IDLE;
        stateTime = 0;
    }
}
