package de.hochschuletrier.gdw.ss15.sandbox.camera;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter.Mode;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.ScreenUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.WeaponCharging;
import de.hochschuletrier.gdw.ss15.events.WeaponUncharged;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ss15.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss15.sandbox.maptest.MapTest;

import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sebastian Schalow
 */

public class CameraTest extends SandboxGame {

    private float camZoom = 1.0f;
    
    private static final Logger logger = LoggerFactory.getLogger(MapTest.class);

    private final Hotkey hkey = new Hotkey(this::addEntity, Input.Keys.E, HotkeyModifier.SHIFT);
    private final Hotkey camOut = new Hotkey(() -> {camZoom += 1f; zoomCam();}, Input.Keys.Q);
    private final Hotkey camIn = new Hotkey(() -> {camZoom -= 1f; zoomCam();}, Input.Keys.A);
    private CVarFloat followFactor = new CVarFloat("camFollow", 1.0f, 0.25f, 30.f, 0, "Camera spring dist factor");
    
    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );
    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final CameraSystem cameraSystem = new CameraSystem();
    private float totalMapWidth, totalMapHeight;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private PhysixBodyComponent playerBody;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap();

    public CameraTest() {
        engine.addSystem(cameraSystem);
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
    }

    @Override
    public void init(AssetManagerX assetManager) {
        map = loadMap("data/maps/demo.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            Texture tex = new Texture(filename);
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            tilesetImages.put(tileset, tex);
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);

        // Generate static world
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blocked", false),
                (Rectangle rect) -> addShape(rect, tileWidth, tileHeight));
        
        // create a simple player ball
        Entity player = engine.createEntity();
        Entity temp = engine.createEntity();
        
        // Registering Entity components
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        PlayerComponent tempComp = engine.createComponent(PlayerComponent.class);
        playerComponent.isLocalPlayer = true;
        tempComp.isLocalPlayer = false;
        temp.add(tempComp);
        player.add(playerComponent);

        
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        player.add(positionComponent);
        temp.add(positionComponent);
         
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);
        temp.add(modifyComponent);

        modifyComponent.schedule(() -> {
            playerBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(100, 100).fixedRotation(true);
            playerBody.init(bodyDef, physixSystem, player);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(30);
            playerBody.createFixture(fixtureDef);
            player.add(playerBody);
        });
        engine.addEntity(player);
        hkey.register();
        camOut.register();
        camIn.register();
        
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        //cameraSystem.setCameraBounds(0, 0, totalMapWidth, totalMapHeight);
        
        followFactor.addListener((CVar cvar)-> {
            cameraSystem.getCamera().setSpringFollowFactor(followFactor.get());
        });
        
        Main.getInstance().console.register(followFactor);
        
    }

    private void addShape(Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(width, height));
    }

    @Override
    public void dispose() {
        hkey.unregister();
        camOut.unregister();
        camIn.unregister();
        tilesetImages.values().forEach(Texture::dispose);
    }

    public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    @Override
    public void update(float delta) {
    
        // TODO: andere Implementierung? Bind woander aufrufen? z.B. innerhalb der CameraSystem-Klasse?        
        cameraSystem.getCamera().bind();
        
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }
        
        mapRenderer.update(delta);
        engine.update(delta);    

        if(playerBody != null) {
            float speed = 15000.0f;
            float velX = 0, velY = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velX -= delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velX += delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                velY -= delta * speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                velY += delta * speed;
            }

            PositionComponent posComp = playerBody.getEntity().getComponent(PositionComponent.class);
            posComp.x = playerBody.getX();
            posComp.y = playerBody.getY();
            
            if(Gdx.input.isButtonPressed(Buttons.LEFT)){
                WeaponCharging.emit();
            } else if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
                WeaponUncharged.emit();
            }
            
            playerBody.setLinearVelocity(velX, velY);
            
        }
    }
    
    private void addEntity(){
        Entity newEnt = new Entity();
        engine.addEntity(newEnt);
        newEnt.add(new PlayerComponent());
        logger.debug("added new entity");
    }
    
    private void zoomCam(){
        logger.debug("zooming");
        //TODO: cameraSystem.zoom(camZoom);
    }
    
}