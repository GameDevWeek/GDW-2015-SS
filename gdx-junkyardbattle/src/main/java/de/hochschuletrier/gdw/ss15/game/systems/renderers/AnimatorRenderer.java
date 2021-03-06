/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.events.rendering.ChangeAnimationEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimationState;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;

/**
 *
 * @author Julien Saevecke
 */
public class AnimatorRenderer extends SortedSubIteratingSystem.SubSystem implements ChangeAnimationEvent.Listener{
    private final Vector2 rotateVector = new Vector2();
    private final Vector2 posVector = new Vector2();
    
    @SuppressWarnings("unchecked")
    public AnimatorRenderer() {
        super(Family.all(AnimatorComponent.class).get());
        
        ChangeAnimationEvent.register(this);
    }
    
    @Override
    public void finalize() throws Throwable{
        super.finalize();
        
        ChangeAnimationEvent.unregister(this);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        AnimatorComponent animator = ComponentMappers.animator.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        if(!animator.draw)
            return;
        
        AnimationExtended currentAnimation = animator.animationStates.get(animator.animationState);
        
        animator.stateTime += deltaTime;
        
        TextureRegion keyFrame = currentAnimation.getKeyFrame(animator.stateTime);
        
        int w = keyFrame.getRegionWidth();
        int h = keyFrame.getRegionHeight();
        
        rotateVector.set(animator.positionOffsetX, animator.positionOffsetY);
        rotateVector.rotate(animator.initialRotation + position.rotation);
        
        posVector.set(
            position.x + rotateVector.x, 
            position.y + rotateVector.y
        );
        
        DrawUtil.batch.draw(keyFrame, posVector.x - w * 0.5f, posVector.y - h * 0.5f, 
                w * 0.5f, h * 0.5f, w, h, animator.scaleX, animator.scaleY, animator.initialRotation + position.rotation);
    }

    @Override
    public void onChangeAnimation(AnimationState state, Entity entity) {
        AnimatorComponent component = ComponentMappers.animator.get(entity);
        component.animationState = state;
        component.stateTime = 0;
    }
}
