package de.hochschuletrier.gdw.ss15.game.rendering;

import org.lwjgl.opengl.GL11;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

public class FogMaskEffect implements ScreenListener {
    private Vector3 target = new Vector3();
    private Vector3 pos = new Vector3();
    private ParticleEffect effect;
    private FrameBuffer frameBuffer = new FrameBuffer(Format.RGBA8888, 
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    private OrthographicCamera cam = new OrthographicCamera();
    private SpriteBatch batch = new SpriteBatch();
    private float scale = 4.f;
    
    // Following attributes
    private float distSpringConstant = 30.0f;
    private float dampingCoefficient = (float) (2.0f * Math.sqrt(distSpringConstant));
    private Vector2 acceleration = new Vector2(0.f, 0.f);
    private Vector2 velocity = new Vector2(0.f, 0.f);
    
    private Entity entityToFollow;
    
    public FogMaskEffect(ParticleEffect effect) {
        this.effect = effect;
        initEffect();
        Main.getInstance().addScreenListener(this);
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }
    
    void initEffect() {
        effect.reset();
        effect.flipY();
        effect.start();
        effect.scaleEffect(scale);
        effect.setDuration(1000000);
        for(ParticleEmitter emitter : effect.getEmitters()) {
            emitter.setMinParticleCount(0);
            emitter.setMaxParticleCount(15);
            emitter.getLife().setHigh(2000, 2000);
            emitter.getSpawnWidth().setHigh(0, 0);
            emitter.getSpawnHeight().setHigh(0, 0);
            emitter.getEmission().setHigh(1, 1);
            emitter.setAttached(true);
        }
    }
    
    public Texture getTexture() {
        return frameBuffer.getColorBufferTexture();
    }
    
    public void apply(Entity entityToFollow, OrthographicCamera gameCamera, float deltaTime) {
        this.entityToFollow = entityToFollow;
        follow(entityToFollow, gameCamera, deltaTime);    
        
        // Drawing in custom frame buffer and using custom batch
        DrawUtil.safeEnd();
        
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        frameBuffer.begin();
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        effect.update(deltaTime);
        effect.draw(batch);
        
        if(effect.isComplete())
            effect.reset();
        
        batch.end();
        frameBuffer.end();
        
        // Restore to default batch
        DrawUtil.safeBegin();
    }
    
    private void follow(Entity entity, OrthographicCamera gameCamera, float deltaTime) {
        PositionComponent entityPos = ComponentMappers.position.get(entity);
        target.x = entityPos.x;
        target.y = entityPos.y;
        gameCamera.project(target);
        computeAcceleration(target.x, target.y);
        eulerStep(deltaTime);
        effect.setPosition(pos.x, pos.y);
    }
    
    /**
     * Computes the acceleration with a spring model using critical damping.
     */
    private void computeAcceleration(float targetX, float targetY) {
        float deltaAccelX = (targetX - pos.x) * distSpringConstant;
        float deltaAccelY = (targetY - pos.y) * distSpringConstant;
        
        float dampingAccelX = - velocity.x * dampingCoefficient;
        float dampingAccelY = - velocity.y * dampingCoefficient;
        
        acceleration.x = deltaAccelX + dampingAccelX;
        acceleration.y = deltaAccelY + dampingAccelY;
    }
    
    private void eulerStep(float deltaTime) {
        velocity.add(acceleration.scl(deltaTime));
        pos.x += velocity.x * deltaTime;
        pos.y += velocity.y * deltaTime;
    }

    @Override
    public void resize(int width, int height) {
        if(entityToFollow != null && !entityToFollow.isScheduledForRemoval()) {
            PositionComponent entityPos = ComponentMappers.position.get(entityToFollow);
            target.x = entityPos.x;
            target.y = entityPos.y;
            MainCamera.getOrthographicCamera().project(target);
            effect.setPosition(target.x, target.y);
        }
    }
    
    @Override
    public void finalize() {
        Main.getInstance().removeScreenListener(this);
    }
}
