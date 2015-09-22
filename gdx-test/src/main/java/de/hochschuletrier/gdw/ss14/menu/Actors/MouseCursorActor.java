package de.hochschuletrier.gdw.ss14.menu.Actors;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class MouseCursorActor extends Actor {
	private static final String PATH_TO_SKIN = "img/actor.png";
	TextureRegion region;// Defines a rectangular area of a texture. The
	private int actorX=0;
							// coordinate system used has its origin in the
							// upper left corner

	public MouseCursorActor() {
		Texture texture= new Texture(PATH_TO_SKIN);
		region = new TextureRegion(texture);
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
	}
	//Aktion of an actor (Aktion1)
    @Override
    public void act(float delta){
       
            setActorX(getActorX() + 5);
            
        
    }

	/*
	 * The Actor hit method receives a point and returns the deepest actor at
	 * that point, or null if no actor was hit. Here is the default hit method:
	 */
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled)
			return null;
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this : null;
	}

	public int getActorX() {
		return actorX;
	}

	public void setActorX(int actorX) {
		this.actorX = actorX;
	}
}
/*
 * Node:
 * 
 * -Each actor has a list of listeners that are notified for events on that
 * actor.
 * 
 * -f setVisible(false) is called on an actor, its draw method will not be
 * called. It will also not receive input events.
 * 
 * - If an actor needs to perform drawing differently, such as with a
 * ShapeRenderer, the Batch should be ended and then begun again at the end of
 * the method.
 */