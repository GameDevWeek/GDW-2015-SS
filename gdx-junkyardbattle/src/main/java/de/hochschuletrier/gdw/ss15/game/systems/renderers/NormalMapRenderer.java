package de.hochschuletrier.gdw.ss15.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.ashley.SortedSubIteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Settings;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.NormalMapComponent;

public class NormalMapRenderer extends SortedSubIteratingSystem.SubSystem {
	
	private Vector2 lightPos = new Vector2();
	private Entity player = null;
	
	private Texture currentNormalMap = null;
	
    @SuppressWarnings("unchecked")
	public NormalMapRenderer() {
    	super(Family.one(NormalMapComponent.class/*, PlayerComponent.class*/).get());
    	
    	NORMAL_MAP_SHADER.begin();
    	NORMAL_MAP_SHADER.setUniformi("u_texture", 0);
    	NORMAL_MAP_SHADER.setUniformi("u_normals", 1);
    	NORMAL_MAP_SHADER.end();
	}
    
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		if(!Settings.NORMAL_MAPPING.get())
			return;
		
//		if(ComponentMappers.player.has(entity)) {
//			player = entity;
//			return;
//		}
		
		// activate normal mapping
		DrawUtil.setShader(NORMAL_MAP_SHADER);
		
		if(player != null && ComponentMappers.position.has(player)) {
			PositionComponent playerPos = ComponentMappers.position.get(player);
			lightPos.set(playerPos.x, playerPos.y);
		}
		
		NormalMapComponent normalMapComponent = ComponentMappers.normalMap.get(entity);
		
		if(currentNormalMap != normalMapComponent.normalMap)
			onNormalMapChanged(normalMapComponent.normalMap); 
	}
	
	private void onNormalMapChanged(Texture newNormalMap) {
		currentNormalMap = newNormalMap;
		
		DrawUtil.batch.flush(); // necessary to set new shader values
		
		NORMAL_MAP_SHADER.setUniformf("u_lightPos", lightPos.x, lightPos.y, 1.f);
		
		currentNormalMap.bind(1); 
		
		// batch needs to know about the active texture to bind() the correct one
		Gdx.graphics.getGL20().glActiveTexture(GL20.GL_TEXTURE0);
	}

    public static final ShaderProgram NORMAL_MAP_SHADER = new ShaderProgram(
    		Gdx.files.internal("data/shaders/normalMapVertex.glsl"), Gdx.files.internal("data/shaders/normalMapFrag.glsl"));
}
