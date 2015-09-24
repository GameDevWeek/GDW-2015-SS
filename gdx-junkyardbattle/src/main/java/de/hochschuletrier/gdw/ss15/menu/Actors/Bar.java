package de.hochschuletrier.gdw.ss15.menu.Actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Bar extends Actor {
	private  float heightBar = 0;
	private  float widthBar = 0;
	private int currentValue=0;
	private int maxValue=0;
	private int x,y;
	
	private TextureRegion region;
	private ShapeRenderer drawer= new ShapeRenderer();
	
	private Color SoundbarColorLeft= Color.GREEN;
	private Color SoundbarColorRight= Color.GREEN;

	public Bar(float height,float width,int x,int y)
	{
		this.heightBar=height;
		this.widthBar=width;
		this.x=x;
		this.y=y;
		setBounds(x, y, width, height);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		drawer.setAutoShapeType(true);
		drawer.begin();
		drawer.setColor(Color.BLACK);
		drawer.set(ShapeType.Line);
		
		
		drawer.rect(x, y, 0, 0, widthBar , heightBar, 1, 1, 0,Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK);
		drawer.set(ShapeType.Filled);
		drawer.rect(x, y, 0, 0, maxValue, heightBar, 1, 1, 0, SoundbarColorLeft, SoundbarColorRight ,SoundbarColorRight,SoundbarColorLeft);
		drawer.end();
		batch.begin();
	}
	public void increaseMaxValue(int increase)
	{
		if(maxValue<=widthBar)
		maxValue+=increase;
	}
	public void decreaseMaxValue(int decrease)
	{
		if(maxValue>0)
		maxValue-=decrease;
	}
	
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled)
			return null;
		return x >= this.x && x < this.x+widthBar && y >= this.y && this.y-heightBar < heightBar ? this : null;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public float getHeightBar() {
		return heightBar;
	}

	public void setHeightBar(int maxValue) {
		this.heightBar = maxValue;
	}
	public float getWidthtBar() {
		return widthBar;
	}

	public void setWidthtBar(int maxValue) {
		this.heightBar = maxValue;
	}
	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
}
/*

width=100%


*/