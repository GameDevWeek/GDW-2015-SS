/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;

/**
 *
 * @author Julien Saevecke
 */
public class TextureRenderer extends SortedSubIteratingSystem.SubSystem {

    @SuppressWarnings("unchecked")
    public TextureRenderer() {
        super(Family.all(TextureComponent.class).get());
    }
    
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TextureComponent texture = ComponentMappers.texture.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);

        DrawUtil.batch.draw(
                texture.texture, 
                position.x - (float)texture.width * 0.5f, 
                position.y - (float)texture.height * 0.5f, 
                (float)texture.width * 0.5f,
                (float)texture.height * 0.5f,
                texture.width, 
                texture.height,
                texture.scaleX, 
                texture.scaleY, 
                position.rotation,
                texture.srcX, 
                texture.srcY,
                texture.width,
                texture.height,
                texture.flipX,
                texture.flipY
                
        );
    }
}
