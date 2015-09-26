package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
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

    private float radarScale = 0.1337f;
    Vector2 lineToPlayer = new Vector2(0,0);
    Vector3 mouseScreenPos = new Vector3(0,0,0);
    Camera camera;
    AssetManagerX assetManager;
    Texture crosshairTex;
    Texture hudoverlay;
    BitmapFont font;

    Entity localPlayer;

    public HudSystem(Camera camera){
        this(Family.one(PlayerComponent.class).get(), GameConstants.PRIORITY_HUD);
        this.camera = camera;
        this.crosshairTex = assetManager.getTexture("crosshair");
        this.hudoverlay = assetManager.getTexture("hudoverlay");
        font = assetManager.getFont("quartz_40");
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
            showHudOverlay();

        }
    }

    private void drawCrosshair(Entity entity){

        mouseScreenPos.x = entity.getComponent(InputComponent.class).posX;
        mouseScreenPos.y = entity.getComponent(InputComponent.class).posY;

        DrawUtil.batch.draw(crosshairTex, mouseScreenPos.x - crosshairTex.getWidth() / 4,
                mouseScreenPos.y - crosshairTex.getHeight() / 4, crosshairTex.getWidth() / 2, crosshairTex.getHeight() / 2);
    }
    private void radar(Entity entity) {

        lineToPlayer.x = entity.getComponent(PositionComponent.class).x >
                localPlayer.getComponent(PositionComponent.class).x ? entity.getComponent(PositionComponent.class).x -
                localPlayer.getComponent(PositionComponent.class).x :
                localPlayer.getComponent(PositionComponent.class).x - entity.getComponent(PositionComponent.class).x;
        lineToPlayer.y = entity.getComponent(PositionComponent.class).y >
                localPlayer.getComponent(PositionComponent.class).y ? entity.getComponent(PositionComponent.class).y -
                localPlayer.getComponent(PositionComponent.class).y :
                localPlayer.getComponent(PositionComponent.class).y - entity.getComponent(PositionComponent.class).y;

        lineToPlayer.scl(radarScale);
        //DrawUtil.batch.draw("icon für spieler", radarMitte + vector);

        //Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-Gdx.graphics.getHeight/4
    }


    private void lebensBalken() {
        Color healthColor;
        if (HudDebug.health >= 50 && HudDebug.health <= 100)
            healthColor = Color.GREEN;
        else if (HudDebug.health <= 50 && HudDebug.health >= 25)
            healthColor = Color.YELLOW;
        else
            healthColor = Color.RED;


        float healthSizeFactor = (float) HudDebug.health / 100.0f;
        float scaleYFactor = 995.0f / 1920.0f;

        DrawUtil.fillRect(Gdx.graphics.getWidth() / 2 - HudDebug.health * 2, Gdx.graphics.getHeight() - 40, healthSizeFactor * 995 * scaleYFactor, 75, healthColor);

        font.draw(DrawUtil.batch, "healthSizeFactor: " + healthSizeFactor + " scaleYFactor: " + scaleYFactor + " width: " + Gdx.graphics.getWidth(), 0, Gdx.graphics.getHeight() / 2);
    }

    private void schrottAnzeige(){
        localPlayer.getComponent(InventoryComponent.class).getMetalShards();
    }

    private void showHudOverlay() {
        DrawUtil.batch.draw(hudoverlay, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), - Gdx.graphics.getWidth(), - Gdx.graphics.getHeight());
    }

}
