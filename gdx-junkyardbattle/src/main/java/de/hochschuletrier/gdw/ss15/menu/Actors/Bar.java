package de.hochschuletrier.gdw.ss15.menu.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class Bar extends Actor {
	private  float heightBar = 0;
	private  float widthBar = 0;
	private float currentValue=50;
	private int x,y;
	
	
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
		DrawUtil.setColor(Color.RED);
		DrawUtil.fillRect(x, y, currentValue, heightBar);
	}
	public boolean increaseMaxValue(int increase)
	{
		int plus=(int) (widthBar/increase);
		if(currentValue<=widthBar)
		{
		currentValue+=plus;
		return true;
		}
		return false;
	}

	public boolean decreaseMaxValue(int decrease)
	{
		int minus=(int) (widthBar/decrease);
		if(currentValue>0)
		{
		currentValue-=minus;return true;}
		return false;
	}
	
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled)
			return null;
		return x >= this.x && x < this.x+widthBar && y >= this.y && this.y-heightBar < heightBar ? this : null;
	}

	public int getCurrentValue() {
		return (int) currentValue;
	}

	public void setCurrentValue(float percent) {
		this.currentValue = widthBar*percent;
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
	
}
/*

width=100%


*/