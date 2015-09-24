package de.hochschuletrier.gdw.ss15.game;

import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.BulletListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.PickupListener;
import de.hochschuletrier.gdw.ss15.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss15.game.systems.LineOfSightSystem;
import de.hochschuletrier.gdw.ss15.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.NetworkServerSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.PositionSynchSystem;
import de.hochschuletrier.gdw.ss15.game.systems.network.UpdatePhysixServer;
import de.hochschuletrier.gdw.ss15.game.systems.network.UpdatePhysixSystem;
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
                                                                                 // (+ LineOfSightSystem-Konstruktor anpassen!)

    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", ServerGame.class);

    private Serversocket serverSocket;
    
    private final MapLoader mapLoader = new MapLoader(); /// @author tobidot

    public ServerGame()
    {
    }

    public PooledEngine get_Engine(){return engine;}

    public void init(AssetManagerX assetManager) {
        // Main.getInstance().console.register(physixDebug);

        addSystems();
        addContactListeners();
        setupPhysixWorld();
        networkSystem.init(serverSocket);
        entityFactory.init(engine, assetManager);

        new UpdatePhysixServer(); // magic → registers itself as listener for network packets

        mapLoader.run( ( String name, float x, float y ) -> { return this.createEntity(name,  x, y); }, "data/maps/prototype.tmx",physixSystem );
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(networkSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(lineOfSightSystem);
        engine.addSystem(syncPositionSystem);
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PickableComponent.class, new PickupListener(engine));
        contactListener.addListener(BulletComponent.class, new BulletListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 0);

        /*PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(410, 500).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(800, 20));
        PhysixUtil.createHollowCircle(physixSystem, 180, 180, 150, 30, 6);

        createTrigger(410, 600, 3200, 40, (Entity entity) -> {
            engine.removeEntity(entity);
        });*/
    }

    public void update(float delta) {
        //Main.getInstance().screenCamera.bind();
        engine.update(delta);
        //System.out.println("rennt");
    }

    public void createTrigger(float x, float y, float width, float height, Consumer<Entity> consumer) {
       /* Entity entity = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        entity.add(modifyComponent);

        TriggerComponent triggerComponent = engine.createComponent(TriggerComponent.class);
        triggerComponent.consumer = consumer;
        entity.add(triggerComponent);

        modifyComponent.schedule(() -> {
            PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y);
            bodyComponent.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).sensor(true).shapeBox(width, height);
            bodyComponent.createFixture(fixtureDef);
            entity.add(bodyComponent);
        });
        engine.addEntity(entity);*/
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
