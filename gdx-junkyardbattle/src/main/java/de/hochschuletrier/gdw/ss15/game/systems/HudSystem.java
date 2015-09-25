package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

import javax.swing.text.Position;

/**
 * Created by glumbatsch on 25.09.2015.
 * KameHameHAH!
 */
public class HudSystem extends IteratingSystem {

    public HudSystem(){
        this(Family.one(PlayerComponent.class).get());
    }

    public HudSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        PlayerComponent player = entity.getComponent(PlayerComponent.class);
        if (player.isLocalPlayer){

        }
    }
}
