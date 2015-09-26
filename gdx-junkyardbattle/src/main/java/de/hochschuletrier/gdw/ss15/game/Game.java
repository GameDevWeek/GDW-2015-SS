package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.CollisionEvent;
import de.hochschuletrier.gdw.ss15.events.ComeToBaseEvent;
import de.hochschuletrier.gdw.ss15.events.MiningEvent;
import de.hochschuletrier.gdw.ss15.events.PickupEvent;
import de.hochschuletrier.gdw.ss15.events.PlayerDiedEvent;
import de.hochschuletrier.gdw.ss15.events.PlayerHurtEvent;
import de.hochschuletrier.gdw.ss15.events.SatelliteColliding;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.events.WeaponCharging;
import de.hochschuletrier.gdw.ss15.events.WeaponUncharged;
import de.hochschuletrier.gdw.ss15.events.network.Base.ConnectTryFinishEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.DoNotTouchServerPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkNewPlayerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.NetworkReceivedNewPacketServerEvent;
import de.hochschuletrier.gdw.ss15.events.network.server.SendPacketServerEvent;
import de.hochschuletrier.gdw.ss15.events.rendering.ChangeAnimationEvent;
import de.hochschuletrier.gdw.ss15.events.rendering.ChangeModeOnEffectEvent;
import de.hochschuletrier.gdw.ss15.events.rendering.ChangePositionOnEffectEvent;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.PickupListenerClient;
import de.hochschuletrier.gdw.ss15.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ss15.game.systems.DeathSystem;
import de.hochschuletrier.gdw.ss15.game.systems.HealthUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.systems.HudSystem;
import de.hochschuletrier.gdw.ss15.game.systems.RenderStateUpdateSystem;
import de.hochschuletrier.gdw.ss15.game.systems.SoundSystem;
import de.hochschuletrier.gdw.ss15.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss15.game.systems.WeaponSystem;
import de.hochschuletrier.gdw.ss15.game.systems.RealNetwork.NetworkClientSystem;
import de.hochschuletrier.gdw.ss15.game.systems.RealNetwork.TestListenerClient;
import de.hochschuletrier.gdw.ss15.game.systems.input.InputSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.FireClientListener;
import de.hochschuletrier.gdw.ss15.game.systems.network.HighscoreSyncListener;
import de.hochschuletrier.gdw.ss15.game.systems.network.TestMovementSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.UpdatePhysixSystem;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.ChangeAnimationStateSystem;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.EffectAddSystem;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.ParticleSpawnSystem;
import de.hochschuletrier.gdw.ss15.game.systems.renderers.RenderSystem;
import de.hochschuletrier.gdw.ss15.game.utils.TimerSystem;

public class Game extends InputAdapter {

    //private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    private boolean debugDraw;
    private final Hotkey togglePhysixDebug = new Hotkey(this::togglePhysixDebug, Input.Keys.F7, HotkeyModifier.CTRL);

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
    
    private final FireClientListener fireClientListener = new FireClientListener(this);
//    private final BulletClientSystem bulletClientSystem = new BulletClientSystem(engine);

    private final ParticleSpawnSystem particleSpawner = new ParticleSpawnSystem(this);
    private final DeathSystem deathSystem = new DeathSystem();
    private final ChangeAnimationStateSystem changeAnimSystem = new ChangeAnimationStateSystem();
    
    private final TestListenerClient TestoutputSystem = new TestListenerClient();
    private final EffectAddSystem effectAddSystem = new EffectAddSystem(engine);
    private final RenderStateUpdateSystem renderStateUpdateSystem = new RenderStateUpdateSystem(engine);
    
    private final HealthUpdateSystem healthUpdateSystem = new HealthUpdateSystem();
    

    private final HudSystem hudSystem = new HudSystem(cameraSystem.getCamera().getOrthographicCamera());
    private HighscoreSyncListener highscoreSyncListener = new HighscoreSyncListener();

    public Game() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            togglePhysixDebug.register();
        }
    }

    public PooledEngine getEngine()
    {
        return engine;
    }

    public void dispose() {
        togglePhysixDebug.unregister();
        clearAllListeners();
    }

    public void clearAllListeners()
    {
    	
    	//Networkpackage
    	//Base
    	ConnectTryFinishEvent.unregisterAll();
        DisconnectEvent.unregisterAll();
        DoNotTouchPacketEvent.unregisterAll();
        //Client
        NetworkReceivedNewPacketClientEvent.unregisterAll();
        SendPacketClientEvent.unregisterAll();
        //Server
        DoNotTouchServerPacketEvent.unregisterAll();
        NetworkNewPlayerEvent.unregisterAll();
        NetworkReceivedNewPacketServerEvent.unregisterAll();
        SendPacketServerEvent.unregisterAll();
        
        
        //Rendering Package
        ChangeAnimationEvent.unregisterAll();
        ChangeModeOnEffectEvent.unregisterAll();
        ChangePositionOnEffectEvent.unregisterAll();
        
        //Rest
        CollisionEvent.unregisterAll();
        ComeToBaseEvent.unregisterAll();
        MiningEvent.unregisterAll();
        PickupEvent.unregisterAll();
        PlayerDiedEvent.unregisterAll();
        PlayerHurtEvent.unregisterAll();
        SatelliteColliding.unregisterAll();
        SoundEvent.unregisterAll();
        WeaponCharging.unregisterAll();
        WeaponUncharged.unregisterAll();
    }

    public void init(AssetManagerX assetManager,int mapid) {

        String mapname = new String("");
        if(mapid == 1)
        {
            mapname = "prototype_v2";
        }
        else
        {
            mapname = "alpha_three_on_three";
        }

        addSystems();
        addContactListeners();
        setupPhysixWorld();
        entityFactory.init(engine, assetManager);
        mapLoader.listen(renderSystem.getTileMapCreator());
        mapLoader.run((String name, float x, float y) -> createEntity(name, x, y),
                "data/maps/royalrubble_v2.tmx", physixSystem, entityFactory, assetManager );


        renderSystem.init(mapLoader.getTiledMap(), this);
        debugDraw = false;
        physixDebugRenderSystem.setProcessing(debugDraw);
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(networksystem);
        engine.addSystem(inputSystem);
        engine.addSystem(deathSystem);
        engine.addSystem(weaponSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(testMovementSystem);
        engine.addSystem(updatePhysixSystem);
        engine.addSystem(soundSystem);
        engine.addSystem(hudSystem);
//        engine.addSystem(bulletClientSystem);
        engine.addSystem(effectAddSystem);
        engine.addSystem(renderStateUpdateSystem);

        // add to engine to get removed from listeners:
        engine.addSystem(fireClientListener);
        engine.addSystem(highscoreSyncListener);
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        //contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
       // contactListener.addListener(TriggerComponent.class, new TriggerListener());
    	contactListener.addListener(PickableComponent.class, new PickupListenerClient());
    	contactListener.addListener(BulletComponent.class, new PickupListenerClient());
    	/*ContactListener cl = new ContactListener() {
			
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
    	physixSystem.getWorld().setContactListener(cl); */
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        timerSystem.update(delta);
        engine.update(delta);
        particleSpawner.update(delta);
    }

    public Entity createEntity(String name, float x, float y) {
        //factoryParam.game = this;
        factoryParam.x = x;
        factoryParam.y = y;
        Entity entity = entityFactory.createEntity(name, factoryParam);
                
        engine.addEntity(entity);
        return entity;
    }


    public InputProcessor getInputProcessor() {
        return this;
    }
    
    private void togglePhysixDebug(){
        debugDraw = !debugDraw;
        physixDebugRenderSystem.setProcessing(debugDraw);
    }
}
