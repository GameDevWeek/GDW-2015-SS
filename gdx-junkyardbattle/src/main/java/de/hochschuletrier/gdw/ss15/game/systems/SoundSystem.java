package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.SoundEvent;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SoundEmitterComponent;

public class SoundSystem extends IteratingSystem implements SoundEvent.Listener {

    private final AssetManagerX assetManager;
    private final SmoothCamera camera;

    public SoundSystem(SmoothCamera camera) {
        this(camera, 0);
    }

    public SoundSystem(SmoothCamera camera, int priority) {
        super(Family.all(SoundEmitterComponent.class, PositionComponent.class).get(), priority);
        this.assetManager = Main.getInstance().getAssetManager();
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        SoundEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        SoundEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Use Camera position as listener position
        Vector3 pos = camera.getPosition();
        SoundEmitter.setListenerPosition(pos.x, pos.y, 10, SoundEmitter.Mode.STEREO);
        SoundEmitter.updateGlobal();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        soundEmitter.emitter.setPosition(position.x, position.y, 0);
        soundEmitter.emitter.update();
    }

    public void onSoundEvent(String sound, Entity entity, boolean b) {
        SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(entity);
        System.out.println(soundEmitter.emitter);
        soundEmitter.emitter.play(assetManager.getSound(sound), false);
        
    }

    @Override
    public void onSoundEvent(String sound, Entity entity)
    {
        onSoundEvent(sound, entity, false);
    }

}