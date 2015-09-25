package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;

import javax.swing.text.Position;

/**
 * Created by glumbatsch on 25.09.2015.
 * KameHameHAH!
 */
public class HudSystem extends IteratingSystem {

    AssetManagerX assetManager;

    public HudSystem(){
        this(Family.one(PlayerComponent.class).get(), GameConstants.PRIORITY_HUD);
    }

    public HudSystem(Family family, int priority) {
        super(family);
        this.priority = priority;
        this.assetManager = Main.getInstance().getAssetManager();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent player = entity.getComponent(PlayerComponent.class);
        if (player.isLocalPlayer){
            InputComponent input = entity.getComponent(InputComponent.class);

            DrawUtil.batch.draw(assetManager.getTexture("crosshair"),input.posX,input.posY);

        }
    }

}
