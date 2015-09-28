package de.hochschuletrier.gdw.ss15.menu.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class TextureActor extends Actor{
	
	private  float heightTexture = 0;
	private  float widthTexture = 0;
	
	private int x,y;
	
	
	private TextureRegion region;
	private ShapeRenderer drawer= new ShapeRenderer();
	public  TextureActor(Texture texture,int x, int y,int width,int height) {
		region= new TextureRegion(texture);
		region.setRegionWidth(width);
		region.setRegionHeight(height);
		this.x=x;
		this.y=y;
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.draw(region, x,y);
		}
	
	
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled)
			return null;
		return x >= this.x && x < this.x+widthTexture && y >= this.y && y < this.y + heightTexture ? this : null;
	}
}
