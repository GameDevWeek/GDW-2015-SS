/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import java.util.ArrayList;

/**
 *
 * @author Julien Saevecke
 */
public class AnimatorSystem extends EntitySystem
{//SortedSubIteratingSystem.SubSystem{
    
    private ImmutableArray<Entity> entities;

    public AnimatorSystem()
    {
        //super(Family.all(AnimatorComponent.class, PositionComponent.class).get());
    }
    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, AnimatorComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (Entity entity : entities) {
            AnimatorComponent animator = ComponentMappers.animator.get(entity);
            PositionComponent position = ComponentMappers.position.get(entity);
            
            if(animator.previousAnimationState != animator.currentAnimationState)
            {
                animator.stateTime = 0;
                animator.previousAnimationState = animator.currentAnimationState;
            }
            
            animator.stateTime += deltaTime;
            
            TextureRegion keyFrame = animator.animationStates.get(animator.currentAnimationState).getKeyFrame(animator.stateTime);
            
            int w = keyFrame.getRegionWidth();
            int h = keyFrame.getRegionHeight();
            
            DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
        }
    }
    
    /*
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        
            AnimatorComponent animator = ComponentMappers.animator.get(entity);
            PositionComponent position = ComponentMappers.position.get(entity);
            
            animator.stateTime += deltaTime;
            
            TextureRegion keyFrame = animator.animationStates.get(animator.currentAnimationState).getKeyFrame(animator.stateTime);
            
            int w = keyFrame.getRegionWidth();
            int h = keyFrame.getRegionHeight();
            
            DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
    }*/
}
