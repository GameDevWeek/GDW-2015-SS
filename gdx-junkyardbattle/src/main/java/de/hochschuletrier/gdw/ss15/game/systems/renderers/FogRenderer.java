package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import org.lwjgl.opengl.GL11;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class FogRenderer extends SortedSubIteratingSystem.SubSystem {
    private ShapeRenderer shapes = new ShapeRenderer();
    private SpriteBatch batch = new SpriteBatch();
    private OrthographicCamera cam = new OrthographicCamera();
    
    public static final ShaderProgram FOG_SHADER = new ShaderProgram(
            Gdx.files.internal("data/shaders/fog.vert"), Gdx.files.internal("data/shaders/fog.frag"));
    
    public FogRenderer() {
        super(Family.all().get());
    }

    private Texture maskTex = new Texture(Gdx.files.internal("data/images/light.png"));
    private Texture fogTex = new Texture(Gdx.files.internal("data/images/smoke.png"));
    
    public void preRender() {
        DrawUtil.safeEnd();
        
        //2. clear our depth buffer with 1.0
        Gdx.gl.glClearDepthf(1f);
        Gdx.gl.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        //3. set the function to LESS
        Gdx.gl.glDepthFunc(GL11.GL_LESS);
        
        //4. enable depth writing
        Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
        
        //5. Enable depth writing, disable RGBA color writing 
//        Gdx.gl.glDepthMask(true);
        Gdx.gl.glColorMask(false, false, false, false);
        
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        
        
        
        batch.begin();
        batch.draw(fogTex, 0, 0, 1000, 1000);
        batch.setShader(FOG_SHADER);
        Gdx.gl.glDepthMask(true);
        batch.draw(maskTex, 0, 50);
//        shapes.begin(ShapeType.Filled);
//
//        shapes.setColor(1f, 0f, 0f, 0.5f);
//        shapes.triangle(0, 0, 0, 300, 150, 150);
//        
//        shapes.end();
        batch.end();
        
        DrawUtil.safeBegin();
        
        Gdx.gl.glColorMask(true, true, true, true);
        
//        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
//        Gdx.gl.glBlendFunc(Gdx.gl10.GL_SRC_ALPHA, Gdx.gl10.GL_ONE_MINUS_SRC_ALPHA);
        
        //9. Make sure testing is enabled.
        Gdx.gl.glEnable(GL11.GL_DEPTH_TEST);
        
        //10. Now depth discards pixels outside our masked shapes
        Gdx.gl.glDepthFunc(GL11.GL_LESS); 
        
        batch.begin();
        batch.draw(fogTex, 0, 0, 1000, 1000);
        batch.setShader(FOG_SHADER);
        batch.draw(fogTex, 50, 50);
        batch.end();
        
        Gdx.gl.glDisable(GL11.GL_DEPTH_TEST);
    }
    
    @Override
    public void processEntity(Entity entity, float deltaTime) {
        // TODO Auto-generated method stub
        
    }

}
