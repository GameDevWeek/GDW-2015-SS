package  de.hochschuletrier.gdw.ss14.menu.Screens;
import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.Assets.Assets;
import com.mygdx.menuExample.GameProject;

public class GameScreen implements Screen {

	private static final String PATH_TO_JOSHI = "img/joshi.png";
	private static final String PATH_TO_BUCKET = "img/bucket.png";
	private final float objectWidthHeigh = 64;

	final GameProject game;
	Texture joshiImage;
	Texture bucketImage;

	OrthographicCamera camera;
	Rectangle bucket;

	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;

	public GameScreen(final GameProject game) {
		this.game = game;// load the images for the droplet and the bucket,
							// OBJECT_WIDTH_HEIGHTxOBJECT_WIDTH_HEIGHT pixels
							// each
		joshiImage = new Texture(Gdx.files.internal(PATH_TO_JOSHI));
		bucketImage = new Texture(Gdx.files.internal(PATH_TO_BUCKET));

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - objectWidthHeigh / 2; // center the bucket
													// horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge
		bucket.width = objectWidthHeigh;
		bucket.height = objectWidthHeigh;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
		
		

	}

	private void spawnRaindrop() {
		Rectangle joshi = new Rectangle();
		joshi.x = MathUtils.random(0, 800 - objectWidthHeigh);
		joshi.y = 480;
		joshi.width = objectWidthHeigh;
		joshi.height = objectWidthHeigh;
		raindrops.add(joshi);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		// game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops

		game.batch.begin();

		// game.lifeLine.draw(game.batch, 1);
		// game.actor.draw(game.batch, 1);
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(joshiImage, raindrop.x, raindrop.y);
		}

		game.batch.end();
		
		
		// Hud-Element
		game.hudBatch.begin();
		game.lifeLine.draw(game.hudBatch, 1);
		game.hudBatch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - objectWidthHeigh / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 800 - objectWidthHeigh)
			bucket.x = 800 - objectWidthHeigh;

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we increase the
		// value our drops counter and add a sound effect.
		Iterator<Rectangle> iter = raindrops.iterator();
		
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + objectWidthHeigh < 0) {
				iter.remove();
				if(GameProject.life > 0)
				{
				GameProject.life = GameProject.life-10;
				System.out.println("Leben = " + GameProject.life);

				}
				else
				{  Assets.insertScore(dropsGathered);
					game.setScreen(new EndScreen((Game)game));}
					
				
				
			}
			if (raindrop.overlaps(bucket)) {
				dropsGathered++;

				iter.remove();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
