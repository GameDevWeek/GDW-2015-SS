package de.hochschuletrier.gdw.ss15.game;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.systems.input.InputSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetworkClientSystem;
import de.hochschuletrier.gdw.ss15.game.systems.*;
import de.hochschuletrier.gdw.ss15.game.systems.network.TestMovementSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.UpdatePhysixSystem;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.RenderSystem;
import de.hochschuletrier.gdw.ss15.game.utils.TimerSystem;

import java.util.function.Consumer;

public class Game extends InputAdapter {

    //private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    //private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

   private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
           GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
   );
   private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem();
    private final NetworkClientSystem networksystem = new NetworkClientSystem(this,GameConstants.PRIORITY_PHYSIX+2);
    private final MoveSystem moveSystem = new MoveSystem();
    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", Game.class);

    private final CameraSystem cameraSystem = new CameraSystem();

    private final RenderSystem renderSystem = new RenderSystem(physixSystem, cameraSystem.getCamera().getOrthographicCamera(), engine);
    private final TestMovementSystem testMovementSystem = new TestMovementSystem(this, cameraSystem.getCamera().getOrthographicCamera());
    private final TimerSystem timerSystem = new TimerSystem();
    private final WeaponSystem weaponSystem = new WeaponSystem();
    private final UpdatePhysixSystem updatePhysixSystem = new UpdatePhysixSystem(timerSystem);
    private final InputSystem inputSystem = new InputSystem(0,cameraSystem.getCamera().getOrthographicCamera());
    private final SoundSystem soundSystem = new SoundSystem(cameraSystem.getCamera());
    private final MapLoader mapLoader = new MapLoader();

    public Game() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            //togglePhysixDebug.register();
        }
    }

    public PooledEngine getEngine()
    {
        return engine;
    }

    public void dispose() {
        //togglePhysixDebug.unregister();
    }

    public void init(AssetManagerX assetManager) {
        //Main.getInstance().console.register(physixDebug);
        //physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));

        addSystems();
        addContactListeners();
        setupPhysixWorld();
        entityFactory.init(engine, assetManager);
        mapLoader.listen(renderSystem.getTileMapCreator());
        mapLoader.run((String name, float x, float y) -> createEntity(name, x, y),
                "data/maps/PrototypeV2.tmx", physixSystem,entityFactory);
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(networksystem);
        engine.addSystem(inputSystem);
        //engine.addSystem(moveSystem);
        engine.addSystem(weaponSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(testMovementSystem);
        engine.addSystem(updatePhysixSystem);
        engine.addSystem(soundSystem);
    }

    private void addContactListeners() {
        //PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        //physixSystem.getWorld().setContactListener(contactListener);
        //contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
       // contactListener.addListener(TriggerComponent.class, new TriggerListener());
    	ContactListener cl = new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
                contact.setEnabled(false);
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
                contact.setEnabled(false);
			}
			
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				contact.setEnabled(false);
			}
			
			@Override
			public void beginContact(Contact contact) {
				contact.setEnabled(false);
				
			}
		}; 
    	physixSystem.getWorld().setContactListener(cl);
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);
        //PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(410, 500).fixedRotation(false);
        //Body body = physixSystem.getWorld().createBody(bodyDef);
       // body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(800, 20));
        //PhysixUtil.createHollowCircle(physixSystem, 180, 180, 150, 30, 6);

        //createTrigger(410, 600, 3200, 40, (Entity entity) -> {
          //  engine.removeEntity(entity);
       // });
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        timerSystem.update(delta);
        engine.update(delta);
    }

    public void createTrigger(float x, float y, float width, float height, Consumer<Entity> consumer) {
        /*Entity entity = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        entity.add(modifyComponent);

        TriggerComponent triggerComponent = engine.createComponent(TriggerComponent.class);
        triggerComponent.consumer = consumer;
        entity.add(triggerComponent);

        modifyComponent.schedule(() -> {
            PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.StaticBody, physixSystem).position(x, y);
            bodyComponent.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).sensor(true).shapeBox(width, height);
            bodyComponent.createFixture(fixtureDef);
            entity.add(bodyComponent);
        });
        engine.addEntity(entity);*/
    }

    public Entity createEntity(String name, float x, float y) {
        //factoryParam.game = this;
        factoryParam.x = x;
        factoryParam.y = y;
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    /*@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == 0)
            createEntity("ball", screenX, screenY);
        else
            createEntity("box", screenX, screenY);
        return true;
    }*/

    public InputProcessor getInputProcessor() {
        return this;
    }

    public InputSystem getInputSystem(){
        return inputSystem;
    }

}
