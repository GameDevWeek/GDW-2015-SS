package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.commons.gdx.ashley.EntityFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.components.factories.EntityFactoryParam;
import de.hochschuletrier.gdw.ss15.game.systems.*;

import java.util.function.Consumer;

public class Game extends InputAdapter {

    //private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    //private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

   // private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
           // GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
   // );
   // private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final AnimationRenderSystem animationRenderSystem = new AnimationRenderSystem(GameConstants.PRIORITY_ANIMATIONS);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);
    private final NetworkClientSystem networksystem = new NetworkClientSystem(this,GameConstants.PRIORITY_PHYSIX+2);

    private final EntityFactoryParam factoryParam = new EntityFactoryParam();
    private final EntityFactory<EntityFactoryParam> entityFactory = new EntityFactory("data/json/entities.json", Game.class);

    private final WeaponSystem weaponSystem = new WeaponSystem();

    private final InputSystem inputSystem = new InputSystem();

    public Game() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            //togglePhysixDebug.register();
        }
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
    }

    private void addSystems() {
        //engine.addSystem(physixSystem);
        //engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(animationRenderSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(networksystem);
        engine.addSystem(inputSystem);
        engine.addSystem(weaponSystem);
    }

    private void addContactListeners() {
        //PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        //physixSystem.getWorld().setContactListener(contactListener);
        //contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
       // contactListener.addListener(TriggerComponent.class, new TriggerListener());
    }

    private void setupPhysixWorld() {
        //physixSystem.setGravity(0, 24);
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


}
