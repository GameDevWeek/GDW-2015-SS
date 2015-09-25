package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.MetalShardSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.BulletListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.MetalShardSpawnListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.PickupListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.systems.BulletSystem;
import de.hochschuletrier.gdw.ss15.game.systems.LineOfSightSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MetalShardSpawnSystem;
import de.hochschuletrier.gdw.ss15.game.systems.PlayerLifeSystem;
import de.hochschuletrier.gdw.ss15.game.systems.SpawnSystem;
import de.hochschuletrier.gdw.ss15.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetworkServerSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.PositionSynchSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.TestSatelliteSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.UpdatePhysixServer;
import de.hochschuletrier.gdw.ss15.game.systems.network.*;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serversocket;

/**
 * Created by lukas on 21.09.15.
 */
public class ServerGame{

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem();//todo magic numbers (von santo)
    private final NetworkServerSystem networkSystem = new NetworkServerSystem(this,GameConstants.PRIORITY_PHYSIX + 2);//todo magic numbers (santo hats vorgemacht)
    private final PositionSynchSystem syncPositionSystem = new PositionSynchSystem(this,GameConstants.PRIORITY_PHYSIX + 3);//todo magic numbers (boa ist das geil kann nicht mehr aufhoeren)
    private final LineOfSightSystem lineOfSightSystem = new LineOfSightSystem(physixSystem); // hier müssen noch Team-Listen übergeben werden
    private final TestSatelliteSystem testSatelliteSystem = new TestSatelliteSystem(this, engine);
    private final PlayerLifeSystem playerLifeSystem = new PlayerLifeSystem();
                                                                                 // (+ LineOfSightSystem-Konstruktor anpassen!)
    //private final BulletSystem bulletSystem = new BulletSystem();
    private final MetalShardSpawnSystem metalShardSpawnSystem = new MetalShardSpawnSystem(this);
    private final BulletSystem bulletSystem = new BulletSystem(engine, this);
    private final PickupSystem pickupSystem = new PickupSystem(engine);
    
    
    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", ServerGame.class);

    private Serversocket serverSocket;
    
    private final MapLoader mapLoader = new MapLoader(); /// @author tobidot
    private UpdatePhysixServer updatePhysixServer;
    private FireServerListener fireServerListener;
    private GatherServerListener gatherServerListener;
    
    private final SpawnSystem spawnSystem = new SpawnSystem();

    public ServerGame(Serversocket socket)
    {
        serverSocket = socket;
    }

    public PooledEngine get_Engine(){return engine;}

    public void init(AssetManagerX assetManager) {
        // Main.getInstance().console.register(physixDebug);

        updatePhysixServer = new UpdatePhysixServer();
        fireServerListener = new FireServerListener(this);
        gatherServerListener = new GatherServerListener(physixSystem);

        addSystems();
        addContactListeners();
        setupPhysixWorld();
        networkSystem.init(serverSocket);
        entityFactory.init(engine, assetManager);

        mapLoader.listen(spawnSystem);
        mapLoader.run( ( String name, float x, float y ) -> { return this.createEntity(name,  x, y); }, "data/maps/3v3Alpha.tmx",physixSystem,entityFactory,assetManager );
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(networkSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(lineOfSightSystem);
        engine.addSystem(syncPositionSystem);
        engine.addSystem(testSatelliteSystem);
        engine.addSystem(bulletSystem);
        engine.addSystem(metalShardSpawnSystem);
        engine.addSystem(pickupSystem);
        engine.addSystem(playerLifeSystem);
        engine.addSystem(spawnSystem);

        //// ---- add listener to engine, to get an autoremove
        engine.addSystem(fireServerListener);
        engine.addSystem(updatePhysixServer);
        engine.addSystem(gatherServerListener);
        engine.addSystem(miningSystem);
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PickableComponent.class, new PickupListener(engine));
        contactListener.addListener(MetalShardSpawnComponent.class, new MetalShardSpawnListener());
        contactListener.addListener(BulletComponent.class, new BulletListener(engine));
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);
    }

    public void update(float delta) {
        //Main.getInstance().screenCamera.bind();
        engine.update(delta);
    }

    public Entity createEntity(String name, float x, float y)
    {
        //factoryParam.game = this;
        factoryParam.x = x;
        factoryParam.y = y;
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

}
