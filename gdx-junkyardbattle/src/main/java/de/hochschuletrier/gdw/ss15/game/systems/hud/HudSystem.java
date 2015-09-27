package de.hochschuletrier.gdw.ss15.game.systems.hud;

import java.nio.channels.NetworkChannel;
import java.util.Timer;
import java.util.TimerTask;

import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.Highscore;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;
import de.hochschuletrier.gdw.ss15.game.hudDebugTemporary.HudDebug;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.HealthPacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket.SimplePacketId;
import de.hochschuletrier.gdw.ss15.game.systems.network.TestSatelliteSystem;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;

import javax.swing.text.Position;

/**
 * Created by David on 25.09.2015.
 */
public class HudSystem extends IteratingSystem implements NetworkReceivedNewPacketClientEvent.Listener {

    private static float radarRange;
    private static float radarScale = 123;
    Vector3 lineToSatellite = new Vector3(0, 0, 0);
    Vector3 lineToPlayer = new Vector3(0, 0, 0);
    Vector3 mouseScreenPos = new Vector3(0, 0, 0);
    Camera camera;
    AssetManagerX assetManager;
    Texture crosshairTex;
    Texture hudoverlay;
    Texture hudoverlay_blue;
    Texture hudoverlay_orange;
    Texture punktestand;
    Texture uhr;
    Texture schrott;
    BitmapFont font;
    Texture miniSatellite;
    Texture gegnerPunktO;
    Texture gegnerPunktB;

    Timer timer = new Timer();

    int schrottcount = 20;
    int timcounter = 0;
    int teamA = 0;
    int healthpoints = 100;
    int teamB = 0;
    //....
    Entity localPlayer;
    SpriteBatch batch = new SpriteBatch();

    public HudSystem(Camera camera) {
        this(Family.one((PlayerComponent.class), (SpawnSatelliteComponent.class)).get(), GameConstants.PRIORITY_HUD);
        this.camera = camera;
        this.crosshairTex = assetManager.getTexture("crosshair");
        this.hudoverlay_blue = assetManager.getTexture("hud_blue");
        this.hudoverlay_orange = assetManager.getTexture("hud_orange");
        this.punktestand = assetManager.getTexture("hud_punktestand");
        this.uhr = assetManager.getTexture("hud_uhr");
        this.schrott = assetManager.getTexture("hud_schrott");
        font = assetManager.getFont("quartz_40");
        this.miniSatellite = assetManager.getTexture("mini_satellite");
        this.gegnerPunktO = assetManager.getTexture("gegner_punkt_orange");
        this.gegnerPunktB = assetManager.getTexture("gegner_punkt_blau");
    }

    public HudSystem(Family family, int priority) {
        super(family);
        this.priority = priority;
        this.assetManager = Main.getInstance().getAssetManager();
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.Simple, this);
        NetworkReceivedNewPacketClientEvent.registerListener(PacketIds.Health, this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DrawUtil.safeEnd();
        DrawUtil.setCustomBatch(batch);
        DrawUtil.safeBegin();
        Main.getInstance().screenCamera.bind();
        if (entity.getComponent(PlayerComponent.class) != null) {

            PlayerComponent player = entity.getComponent(PlayerComponent.class);
            if (player.isLocalPlayer) {
                if (localPlayer == null) {
                    this.localPlayer = entity;
                }
                if (player.teamID == 0) {
                    this.hudoverlay = hudoverlay_orange;
                } else {
                    this.hudoverlay = hudoverlay_blue;
                }
                drawCrosshair(entity);
                lebensBalken();
                showHudOverlay();
                schrottAnzeige();
                timer();
                punktestand();
            }
            radar(entity);
        }
        if (entity.getComponent(SpawnSatelliteComponent.class) != null) {
            if (TestSatelliteSystem.satellite) {
                lineToSatellite(entity);
            }
        }
        DrawUtil.safeEnd();
        DrawUtil.setCustomBatch(null);
        DrawUtil.safeBegin();
    }

    private void drawCrosshair(Entity entity) {

        mouseScreenPos.x = entity.getComponent(InputComponent.class).posX;
        mouseScreenPos.y = entity.getComponent(InputComponent.class).posY;

        DrawUtil.batch.draw(crosshairTex, mouseScreenPos.x - crosshairTex.getWidth() / 4,
                mouseScreenPos.y - crosshairTex.getHeight() / 4, crosshairTex.getWidth() / 2, crosshairTex.getHeight() / 2);
    }

    private void radar(Entity entity) {

        if(entity.getComponent(PositionComponent.class) == null){
            return;
        }
        lineToPlayer.x = entity.getComponent(PositionComponent.class).x - localPlayer.getComponent(PositionComponent.class).x;
        lineToPlayer.y = entity.getComponent(PositionComponent.class).y - localPlayer.getComponent(PositionComponent.class).y;

        lineToPlayer.nor();
        lineToPlayer.scl(Gdx.graphics.getWidth()/90);

        if (localPlayer.getComponent(PlayerComponent.class).teamID == 0) { // orange
            DrawUtil.batch.draw(gegnerPunktB, Gdx.graphics.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 8.3f, 10, 10);
        } else {                                                           // blau
            DrawUtil.batch.draw(gegnerPunktO, Gdx.graphics.getWidth() / 2,
                    Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 8.3f,
                    Gdx.graphics.getWidth() / 80, Gdx.graphics.getWidth() / 80);
        }

    }

    private void lineToSatellite(Entity entity) {

        if (entity == null || localPlayer == null) {
            return;
        }

        lineToSatellite.x = entity.getComponent(PositionComponent.class).x - localPlayer.getComponent(PositionComponent.class).x;
        lineToSatellite.y = entity.getComponent(PositionComponent.class).y - localPlayer.getComponent(PositionComponent.class).y;

        //lineToSatellite = camera.project(lineToSatellite);

        lineToSatellite.nor();
        lineToSatellite.scl(Gdx.graphics.getWidth() / 10);
        DrawUtil.batch.draw(miniSatellite, Gdx.graphics.getWidth() / 2 + lineToSatellite.x,
                Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2 + lineToSatellite.y,
                camera.viewportWidth / 80, camera.viewportWidth / 80);

    }

    private void lebensBalken() {
        Color healthColor;
        int health = healthpoints;

        if (health >= 50 && health <= 100)
            healthColor = Color.GREEN;
        else if (health < 50 && health >= 25)
            healthColor = Color.YELLOW;
        else
            healthColor = Color.RED;


        float healthSizeFactor = (float) health / 100.0f;
        float relativeXPosLeft = 736.0f / hudoverlay.getWidth() * Gdx.graphics.getWidth();
        float relativeYPosLeft = Gdx.graphics.getHeight() - 58.0f / hudoverlay.getHeight() * Gdx.graphics.getHeight();
        float relativeXPosRight = 1023.0f / hudoverlay.getWidth() * Gdx.graphics.getWidth();
        float relativeYPosRight = Gdx.graphics.getHeight() - 58.0f / hudoverlay.getHeight() * Gdx.graphics.getHeight();
        float barHeight = 36.0f / hudoverlay.getHeight() * Gdx.graphics.getHeight();
        float barWidth = 164.0f / hudoverlay.getWidth() * Gdx.graphics.getWidth();

        //linkeBox
        DrawUtil.fillRect(relativeXPosLeft - (barWidth * (healthSizeFactor - 1)), relativeYPosLeft, barWidth * healthSizeFactor, barHeight, healthColor);
        //rechteBox
        DrawUtil.fillRect(relativeXPosRight, relativeYPosRight, barWidth * healthSizeFactor, barHeight, healthColor);
    }

    private void schrottAnzeige() {

        DrawUtil.batch.draw(schrott, Gdx.graphics.getWidth() / 2 - 450, Gdx.graphics.getHeight() + 3, schrott.getWidth() / 2, schrott.getHeight() / -2);
        DrawUtil.batch.draw(schrott, Gdx.graphics.getWidth() / 2 - 450, Gdx.graphics.getHeight() + 3, schrott.getWidth() / 2, schrott.getHeight() / -2);
        font.draw(DrawUtil.batch, "" + schrottcount, Gdx.graphics.getWidth() / 2 - 366, Gdx.graphics.getHeight() - 45);

    }

    private void timer() {
        DrawUtil.batch.draw(uhr, Gdx.graphics.getWidth() / 2 + 250, Gdx.graphics.getHeight() + 3, uhr.getWidth() / 2, uhr.getHeight() / -2);
        int timerMin = timcounter / 60;
        String timerSec = "";
        if ((timcounter % 60) >= 10) {
            timerSec += timcounter % 60;
        } else {
            timerSec += ("0" + timcounter % 60);
        }
        font.draw(DrawUtil.batch, "" + timerMin + ":" + timerSec, Gdx.graphics.getWidth() / 2 + 316, Gdx.graphics.getHeight() - 45);

    }

    private void punktestand() {
        DrawUtil.batch.draw(punktestand, Gdx.graphics.getWidth() / 2 - punktestand.getWidth() / 6, punktestand.getHeight() / 3, punktestand.getWidth() / 3, punktestand.getHeight() / -3);

        font.draw(DrawUtil.batch, ""+teamA, Gdx.graphics.getWidth()/2 - 75, 15);
        font.draw(DrawUtil.batch, ""+teamB, Gdx.graphics.getWidth()/2 + 25, 15);
    }

    private void showHudOverlay() {
        DrawUtil.batch.draw(hudoverlay, 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
    }

    @Override
    public void onReceivedNewPacket(Packet pack, Entity ent) {
        if (pack.getPacketId() == PacketIds.Simple.getValue()) {
            SimplePacket sPack = (SimplePacket) pack;
            
            //METALSHARDS
            if (SimplePacket.SimplePacketId.MetalShardsUpdate.getValue() == sPack.m_SimplePacketId) {
                schrottcount = (int) sPack.m_Moredata;
                
            //TIMER
            } else if (sPack.m_SimplePacketId == SimplePacket.SimplePacketId.GameCounter.getValue()) {
                timcounter = (int) sPack.m_Moredata;

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        timcounter--;
                    }
                }, 1000, 1000);
            }
            
            //PUNKTE
            else if(sPack.m_SimplePacketId == SimplePacket.SimplePacketId.HighscorePacket.getValue())
            {
            	int points = (int) sPack.m_Moredata;
            	
            	//TEAM A
            	if(points < 0)
            	{
            		teamA = -points;
            		
            	}
            	//TEAM B
            	if(points > 0)
            	{
            		teamB = points;
            	}
            }
        }
        if(pack.getPacketId() == PacketIds.Health.getValue())
        {

            HealthPacket hPack = (HealthPacket)pack;
            healthpoints = hPack.health;
            //System.out.println(hPack.health);
        }
    }


}
