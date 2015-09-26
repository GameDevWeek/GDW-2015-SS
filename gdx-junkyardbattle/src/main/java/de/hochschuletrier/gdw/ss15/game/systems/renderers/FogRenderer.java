package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import org.lwjgl.opengl.GL11;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameGlobals;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.SmokeComponent;
import de.hochschuletrier.gdw.ss15.game.rendering.FogMaskEffect;
import de.hochschuletrier.gdw.ss15.game.rendering.FrustumUtil;

/**
 * Renders fog/smoke around the player.
 * 
 * @author compie
 *
 */
public class FogRenderer extends SortedSubIteratingSystem.SubSystem implements EntityListener {
    private SpriteBatch batch = new SpriteBatch();
    private OrthographicCamera cam = new OrthographicCamera();

    private Entity player = null;
    private OrthographicCamera gameCamera;
    private ParticleEffectRenderer particleRenderer;
    private ImmutableArray<Entity> smokeEffects;
    private FrameBuffer frameBuffer = new FrameBuffer(Format.RGBA8888, 
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    public static final ShaderProgram ALPHA_MASK_SHADER = new ShaderProgram(
            Gdx.files.internal("data/shaders/alphaMask.vert"), Gdx.files.internal("data/shaders/alphaMask.frag"));
    
    private FogMaskEffect maskEffect;
    private float alphaModifier = 2.f; // in [0, infinity], 0 = no alpha influence, > 0 less smoke
    
    private final BoundingBox fogBBox = new BoundingBox();
    private final float fogWidth = 800.f;
    private final float fogHeight = 800.f;
    
    @SuppressWarnings("unchecked")
    public FogRenderer(Engine engine, OrthographicCamera gameCamera, ParticleEffectRenderer particleRenderer) {
        super(Family.all().get());
        engine.addEntityListener(Family.all(PlayerComponent.class).get(), this);
        smokeEffects = engine.getEntitiesFor(Family.all(SmokeComponent.class, ParticleEffectComponent.class).get());
        this.gameCamera = gameCamera;
        this.particleRenderer = particleRenderer;
        
        ALPHA_MASK_SHADER.begin();
        ALPHA_MASK_SHADER.setUniformi("u_texture", 0);
        ALPHA_MASK_SHADER.setUniformi("u_alphaMask", 1);
        ALPHA_MASK_SHADER.end();
        
        System.out.println(ALPHA_MASK_SHADER.getLog());
        
        maskEffect = new FogMaskEffect(new ParticleEffect(GameGlobals.assetManager.getParticleEffect("smoke")));
    }
    
    private void renderFog(float deltaTime) {
        maskEffect.apply(player, gameCamera, deltaTime);  
        
        if(Gdx.graphics.getWidth() != frameBuffer.getWidth() || Gdx.graphics.getHeight() != frameBuffer.getHeight())
            frameBuffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            
        frameBuffer.begin();
        {
            Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            DrawUtil.setCustomBatch(batch);
            DrawUtil.batch.setProjectionMatrix(gameCamera.combined);
            DrawUtil.safeBegin();
            {
                for(Entity smoke : smokeEffects) {
                    PositionComponent posComp = ComponentMappers.position.get(smoke);
                    
                    fogBBox.min.set(posComp.x - fogWidth * 0.5f, posComp.y - fogHeight * 0.5f, 0.f);
                    fogBBox.max.set(posComp.x + fogWidth * 0.5f, posComp.y + fogHeight * 0.5f, 0.f);
                    
                    if(!FrustumUtil.inFrustum(fogBBox)) {
                        continue;
                    }
                    
                    particleRenderer.processEntity(smoke, deltaTime);
                }
            }
            DrawUtil.safeEnd();
            DrawUtil.setCustomBatch(null);
        }
        frameBuffer.end();
    }
    
    public void render(float deltaTime) {
        if(player == null)
            return;
        
        DrawUtil.safeEnd();

        renderFog(deltaTime);
        
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.update();
        DrawUtil.setCustomBatch(batch);
        DrawUtil.batch.setProjectionMatrix(cam.combined);
        
        DrawUtil.safeBegin();
        {
            DrawUtil.setShader(ALPHA_MASK_SHADER);
            
            DrawUtil.batch.flush(); // necessary to set new shader values
            
            ALPHA_MASK_SHADER.setUniformf("u_alphaModifier", alphaModifier);
            
            maskEffect.getTexture().bind(1);
            
            // batch needs to know about the active texture to bind() the correct one
            Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
            
            DrawUtil.draw(frameBuffer.getColorBufferTexture());
        }
        DrawUtil.safeEnd();
        
        // Restore to default
        DrawUtil.setShader(null);
        DrawUtil.setCustomBatch(null);
        DrawUtil.safeBegin();
    }
    
    @Override
    public void processEntity(Entity entity, float deltaTime) {
    }

    @Override
    public void entityAdded(Entity entity) {
        player = entity;
    }

    @Override
    public void entityRemoved(Entity entity) {
        if(entity == player)
            player = null;
    }

}
