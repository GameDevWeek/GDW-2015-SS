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
 * Created by David on 25.09.2015.
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
        DrawUtil.resetColor();
        if (player.isLocalPlayer){
            if (localPlayer == null){
                this.localPlayer = entity;
            }
            drawCrosshair(entity);
            lebensBalken();
            showHudOverlay();
            schrottAnzeige();
            timer();
            radar(entity);
        }
    }

    private void drawCrosshair(Entity entity){

        mouseScreenPos.x = entity.getComponent(InputComponent.class).posX;
        mouseScreenPos.y = entity.getComponent(InputComponent.class).posY;

        DrawUtil.batch.draw(crosshairTex, mouseScreenPos.x - crosshairTex.getWidth() / 4,
                mouseScreenPos.y - crosshairTex.getHeight() / 4, crosshairTex.getWidth() / 2, crosshairTex.getHeight() / 2);
    }

    private void radar(Entity entity) {

        lineToPlayer.x = entity.getComponent(PositionComponent.class).x - localPlayer.getComponent(PositionComponent.class).x;
        lineToPlayer.y = entity.getComponent(PositionComponent.class).y - localPlayer.getComponent(PositionComponent.class).y;

        lineToPlayer.scl(radarScale);
        //DrawUtil.batch.draw("icon für spieler", radarMitte + vector);
        DrawUtil.drawRect(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 10, 10);
        //Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-Gdx.graphics.getHeight/4
    }


    private void lebensBalken() {
        Color healthColor;
        int health = localPlayer.getComponent(HealthComponent.class).health;
        if (HudDebug.health >= 50 && HudDebug.health <= 100)
            healthColor = Color.GREEN;
        else if (HudDebug.health <= 50 && HudDebug.health >= 25)
            healthColor = Color.YELLOW;
        else
            healthColor = Color.RED;


        float healthSizeFactor = (float) HudDebug.health / 100.0f;
        float scaleXFactor = 392.0f / (float) hudoverlay.getWidth();
        float relativeXPosLeft = 646.0f / hudoverlay.getWidth() * Gdx.graphics.getWidth();
        float relativeYPosLeft = Gdx.graphics.getHeight() - 51.0f / hudoverlay.getHeight() * Gdx.graphics.getHeight();
        float relativeXPosRight = 646.0f / hudoverlay.getWidth() * Gdx.graphics.getWidth();
        float relativeYPosRight = Gdx.graphics.getHeight() - 51.0f / hudoverlay.getHeight() * Gdx.graphics.getHeight();
        float barHeight = 28.0f / hudoverlay.getHeight() * Gdx.graphics.getHeight();
        float barWidth = 392.0f / hudoverlay.getWidth() * Gdx.graphics.getWidth();

        //linkeBox
        DrawUtil.fillRect(relativeXPosLeft - (barWidth * (healthSizeFactor -1)), relativeYPosLeft, barWidth * healthSizeFactor, barHeight, healthColor);
        //rechteBox
        //DrawUtil.fillRect(relativeXPosRight, relativeYPosRight, barWidth * healthSizeFactor, barHeight, healthColor);

        //font.draw(DrawUtil.batch, "health: " + health, 0, Gdx.graphics.getHeight() / 2);
        //font.draw(DrawUtil.batch, "barWidth: " + barWidth , 0, Gdx.graphics.getHeight() / 2 + 200);
    }

    private void schrottAnzeige(){
        int schrottcount = localPlayer.getComponent(InventoryComponent.class).getMetalShards();
        float relativeXPos = 646.0f / hudoverlay.getWidth() * Gdx.graphics.getWidth();
        float relativeYPos = Gdx.graphics.getHeight() - 51.0f / hudoverlay.getHeight() * Gdx.graphics.getHeight();

        //font.draw(DrawUtil.batch, "" + schrottcount, 0, Gdx.graphics.getHeight() / 2);
    }

    private void timer() {

    }

    private void showHudOverlay() {
        DrawUtil.batch.draw(hudoverlay, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), - Gdx.graphics.getWidth(), - Gdx.graphics.getHeight());
    }

}
