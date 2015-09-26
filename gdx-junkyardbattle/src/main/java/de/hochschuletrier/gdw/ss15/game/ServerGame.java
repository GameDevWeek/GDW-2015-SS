package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.*;
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
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.BaseMetalShardDeliverListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.BulletListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.MetalShardSpawnListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.PickupListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.systems.BulletSystem;
import de.hochschuletrier.gdw.ss15.game.systems.DeathSystem;
import de.hochschuletrier.gdw.ss15.game.systems.HealthSystem;
import de.hochschuletrier.gdw.ss15.game.systems.InventorySystem;
import de.hochschuletrier.gdw.ss15.game.systems.MetalShardDropSystem;
import de.hochschuletrier.gdw.ss15.game.systems.LineOfSightSystem;
import de.hochschuletrier.gdw.ss15.game.systems.MetalShardSpawnSystem;
import de.hochschuletrier.gdw.ss15.game.systems.SpawnSystem;
import de.hochschuletrier.gdw.ss15.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss15.game.systems.RealNetwork.NetworkServerSystem;
import de.hochschuletrier.gdw.ss15.game.systems.RealNetwork.PositionSynchSystem;
import de.hochschuletrier.gdw.ss15.game.systems.SyncHighscoreSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.*;
import de.hochschuletrier.gdw.ss15.game.utils.TimerSystem;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.Serverclientsocket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.tools.Tools;

/**
 * Created by lukas on 21.09.15.
 *
 *
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
//    private final PlayerLifeSystem playerLifeSystem = new PlayerLifeSystem();
                                                                                 // (+ LineOfSightSystem-Konstruktor anpassen!)
    //private final BulletSystem bulletSystem = new BulletSystem();
    private final MetalShardSpawnSystem metalShardSpawnSystem = new MetalShardSpawnSystem(this);
    private final BulletSystem bulletSystem = new BulletSystem(engine, this);
    private final PickupSystem pickupSystem = new PickupSystem(engine);
    private final MiningSystem miningSystem = new MiningSystem();
    private final InventorySystem inventorySystem = new InventorySystem();
    private final BringHomeSystem bringHomeSystem = new BringHomeSystem();
    
    private final EntityFactoryParam factoryParam = new EntityFactoryParam();

    private final TimerSystem timerSystem = new TimerSystem();
    private final SyncHighscoreSystem syncHighscoreSystem = new SyncHighscoreSystem(timerSystem);
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", ServerGame.class);

    
    private final MapLoader mapLoader = new MapLoader(); /// @author tobidot
    private UpdatePhysixServer updatePhysixServer = new UpdatePhysixServer();
    private FireServerListener fireServerListener = new FireServerListener(this);
    private GatherServerListener gatherServerListener = new GatherServerListener(physixSystem);
    
    private final SpawnSystem spawnSystem = new SpawnSystem();
    private final MetalShardDropSystem metalShardDropSystem = new MetalShardDropSystem(this);
    
    private final PlayerHurtSystem playerHurtSystem = new PlayerHurtSystem();
    private final HealthSystem healthSystem = new HealthSystem(engine);
    private final DeathSystem deathSystem = new DeathSystem();

    private float maxTimeGameIsRunning = 10*60;
    private float timeGameRunns = 0;
    
    public ServerGame()
    {

    }

    public PooledEngine get_Engine(){return engine;}

    public void InsertPlayerInGame(Serverclientsocket sock,String name, boolean team)
    {
        Main.getInstance().getServer().LastConnectedClient = sock;

        Entity ent = createEntity("player",0,0);
        //ClientComponent comp = new ClientComponent();
        //comp.client = sock;
        //ent.add(comp);
        Main.getInstance().getServer().LastConnectedClient = null;

        //ComponentMappers.client.get(ent).client = sock;
        ComponentMappers.player.get(ent).name = name;
        ComponentMappers.player.get(ent).playerID = name.hashCode();
        ComponentMappers.player.get(ent).teamID = Tools.BoolToInt(team);

        //Highscore.Get().addPlayer(name.hashCode());
        //Highscore.Get().setPlayerStat(name.hashCode(), "team", ComponentMappers.player.get(ent).teamID);

        NetworkNewPlayerEvent.emit(ent);
    }

    public void init(int mapid) {
        // Main.getInstance().console.register(physixDebug);

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
        networkSystem.init();
        entityFactory.init(engine, Main.getInstance().getAssetManager());

        mapLoader.listen(spawnSystem);

        /*
        mapLoader.run((String name, float x, float y) -> {
            return this.createEntity(name, x, y);
        }, "data/maps/"+mapname+".tmx", physixSystem, entityFactory, Main.getInstance().getAssetManager());
    */
        mapLoader.run(this::createEntity, "data/maps/royalrubble_v2.tmx", physixSystem, entityFactory, Main.getInstance().getAssetManager());


        Highscore.reset();
        Highscore.Get().addPlayerCategory("team");
        Highscore.Get().addPlayerCategory("kills");
        Highscore.Get().addPlayerCategory("deaths");
        Highscore.Get().addPlayerCategory("shards");
        Highscore.Get().addTeamCategory("points");
        Highscore.Get().addTeam(0);
        Highscore.Get().addTeam(1);
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
//        engine.addSystem(playerLifeSystem);
        engine.addSystem(spawnSystem);
        engine.addSystem(metalShardDropSystem);
        engine.addSystem(playerHurtSystem);
        engine.addSystem(inventorySystem);
        engine.addSystem(healthSystem);
        engine.addSystem(deathSystem);

        //// ---- add listener to engine, to get an autoremove
        engine.addSystem(fireServerListener);
        engine.addSystem(updatePhysixServer);
        engine.addSystem(gatherServerListener);
        engine.addSystem(miningSystem);
        
        PlayerDiedEvent.register(spawnSystem);
        engine.addSystem(bringHomeSystem);
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PickableComponent.class, new PickupListener(engine));
        contactListener.addListener(MetalShardSpawnComponent.class, new MetalShardSpawnListener());
        contactListener.addListener(BulletComponent.class, new BulletListener(engine));
        contactListener.addListener(BasePointComponent.class, new BaseMetalShardDeliverListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);
    }

    public boolean update(float delta) {
        //Main.getInstance().screenCamera.bind();
        engine.update(delta);
        timerSystem.update(delta);

        //System.out.println(timeGameRunns);
        timeGameRunns += delta;
        if(timeGameRunns>maxTimeGameIsRunning)
        {//game is ending
            return false;
        }
        return true;
    }

    public Entity createEntity(String name, float x, float y)
    {
        //factoryParam.game = this;
        factoryParam.x = x;
        factoryParam.y = y;
        //System.out.println("Spawned entit with name: " + name);
        Entity entity = entityFactory.createEntity(name, factoryParam);

        engine.addEntity(entity);
        return entity;
    }

    public void remove()
    {
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

}
