package de.hochschuletrier.gdw.ss15.sandbox;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.MapLoader;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ss15.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.RenderSystem;

/**
 *
 * @author Santo Pfingsten
 */
public class RenderSystemTest extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(RenderSystemTest.class);

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
    private final RenderSystem renderSystem = new RenderSystem(physixSystem,
            cameraSystem.getCamera().getOrthographicCamera(), engine);
    private final UpdatePositionSystem updatePosSystem = new UpdatePositionSystem();
    private float totalMapWidth, totalMapHeight;

    private TiledMap map;
    private MapLoader mapLoader = new MapLoader();
    private PhysixBodyComponent playerBody;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap();

    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", Game.class);
    
    private Entity player;
    
    public RenderSystemTest() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(updatePosSystem);
    }

    @Override
    public void init(AssetManagerX assetManager) {
        entityFactory.init(engine, assetManager);
        mapLoader.listen(renderSystem.getTileMapCreator());
        mapLoader.run((String name, float x, float y) -> createEntity(name, x, y), 
                "data/maps/prototypeV2.tmx", physixSystem,entityFactory,assetManager);

        map = mapLoader.getTiledMap();
        

        // create a simple player ball
        player = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);

        PositionComponent posComponent = engine.createComponent(PositionComponent.class);
        player.add(posComponent);
        
        player.add(engine.createComponent(PlayerComponent.class));
        PointLightComponent pointLightComponent = engine.createComponent(PointLightComponent.class);
        pointLightComponent.set(new Color(1, 0, 0, 0.7f), 10.f);
        
        ConeLightComponent coneLightComponent = engine.createComponent(ConeLightComponent.class);
        coneLightComponent.set(Color.WHITE, 10.f, 0.f, 30.f);
        player.add(coneLightComponent);
        player.add(pointLightComponent);
        
        ComponentMappers.player.get(player).isLocalPlayer = true;
        
        modifyComponent.schedule(() -> {
            playerBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(500, 500).fixedRotation(true);
            playerBody.init(bodyDef, physixSystem, player);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(30);
            playerBody.createFixture(fixtureDef);
            player.add(playerBody);
        });
        engine.addEntity(player);

        createEntity("greenPointLight", 50.f, 50.f);
        createEntity("blueConeLight", 500.f, 100.f);
        createEntity("ball", 256.f, 256.f);
        
        /*
        for(int x = 0; x < 25; ++x){
            for(int y = 0; y < 25; ++y){
                createEntity("smokescreen", 128.f * x, 128.f * y);
            }
        }
                */
        
        // Setup camera
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        cameraSystem.setBounds(0, 0, totalMapWidth, totalMapHeight);
        Main.getInstance().addScreenListener(cameraSystem.getCamera());
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
        Main.getInstance().removeScreenListener(cameraSystem.getCamera());
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
    
    public Entity createEntity(String name, float x, float y) {
        //factoryParam.game = null;
        factoryParam.x = x;
        factoryParam.y = y;
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    @Override
    public void update(float delta) {
        cameraSystem.getCamera().bind();
        engine.update(delta);

        if(playerBody != null) {
            float speed = 10000.0f;
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

            playerBody.setLinearVelocity(velX, velY);
            
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();
            
            Ray ray = cameraSystem.getCamera().getOrthographicCamera().getPickRay(mouseX, mouseY);
            PositionComponent posComp = ComponentMappers.position.get(player);
            //cameraSystem.getCamera().getOrthographicCamera()
            Vector3 target = new Vector3(mouseX, mouseY, 0.f);
            target.mul(cameraSystem.getCamera().getOrthographicCamera().combined);
            
            target.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cameraSystem.getCamera().getOrthographicCamera().unproject(target);
            

            
            float angle = getAngle(posComp.x, posComp.y, target.x, target.y);
            ConeLightComponent coneLightComp = ComponentMappers.coneLight.get(player);
            
            coneLightComp.coneLight.setDirection(angle);
            
            coneLightComp.coneLight.setActive(Gdx.input.isButtonPressed(Input.Buttons.LEFT));
        }
    }
    
    public float getAngle(float srcX, float srcY, float targetX, float targetY) {
        float angle = (float) Math.toDegrees(Math.atan2(targetY - srcY, targetX - srcX));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
}
