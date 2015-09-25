package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;
import de.hochschuletrier.gdw.ss15.game.hudDebugTemporary.HudDebug;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

import javax.swing.text.Position;

/**
 * Created by glumbatsch on 25.09.2015.
 * KameHameHAH!
 */
public class HudSystem extends IteratingSystem {

    Vector2 lineToPlayer = new Vector2(0,0);
    Vector3 mouseScreenPos = new Vector3(0,0,0);
    Camera camera;
    AssetManagerX assetManager;
    Texture crosshairTex;

    Entity localPlayer;

    public HudSystem(Camera camera){
        this(Family.one(PlayerComponent.class).get(), GameConstants.PRIORITY_HUD);
        this.camera = camera;
        this.crosshairTex = assetManager.getTexture("crosshair");
    }

    public HudSystem(Family family, int priority) {
        super(family);
        this.priority = priority;
        this.assetManager = Main.getInstance().getAssetManager();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent player = entity.getComponent(PlayerComponent.class);
        Main.getInstance().screenCamera.bind();
        if (player.isLocalPlayer){
            if (localPlayer == null){
                this.localPlayer = entity;
            }
            drawCrosshair(entity);
            lebensBalken();

        }
    }

    private void drawCrosshair(Entity entity){

        mouseScreenPos.x = entity.getComponent(InputComponent.class).posX;
        mouseScreenPos.y = entity.getComponent(InputComponent.class).posY;
        DrawUtil.batch.draw(crosshairTex,mouseScreenPos.x - crosshairTex.getWidth()/4,
                mouseScreenPos.y - crosshairTex.getHeight()/4, crosshairTex.getWidth()/2, crosshairTex.getHeight()/2);

    }
    private void radar(Entity entity) {

    }
    private void lebensBalken() {
        DrawUtil.fillRect(Gdx.graphics.getWidth() / 2 - HudDebug.health, Gdx.graphics.getHeight() - 20, 2 * HudDebug.health, 40, Color.RED);
        DrawUtil.drawRect(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 20, 200, 40, Color.WHITE);

    }

}
