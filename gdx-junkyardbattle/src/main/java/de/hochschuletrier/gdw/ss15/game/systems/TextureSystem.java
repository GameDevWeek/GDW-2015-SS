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
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;

/**
 *
 * @author Julien Saevecke
 */
public class TextureSystem extends EntitySystem{
    
    private ImmutableArray<Entity> entities;

    public TextureSystem()
    {
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, TextureComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        for (Entity entity : entities) {
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
                    false,
                    false
            );
        }
    }
}
