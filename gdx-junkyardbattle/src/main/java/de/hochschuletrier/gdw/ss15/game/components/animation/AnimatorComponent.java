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
    public AnimationState currentAnimationState = AnimationState.IDLE;
    public AnimationState previousAnimationState = AnimationState.IDLE;
    public float stateTime;
    public int layer;

    @Override
    public void reset() 
    {
        animationStates.clear();
        currentAnimationState = AnimationState.IDLE;
        previousAnimationState = AnimationState.IDLE;
        stateTime = 0;
        layer = 0;
    }
}
