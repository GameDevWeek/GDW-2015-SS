package de.hochschuletrier.gdw.ss14.menu.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SplashScreen implements Screen {
	
	private Texture texture = new Texture(Gdx.files.internal("img/SplashScreen.jpg"));
    private Image splashImage = new Image(texture);
	private Stage stage = new Stage();
	private final float delay=2f;
	private Game game;
	
	public SplashScreen(Game game)
	{	super();
		this.game=game;
		
	}

	@Override
	public void show() {
		// The first method to be called, you need to initialize everything in
		// here. It�s only called when the screen is set.
		/*If your image is not the same size as your screen
         *splashImage.setX(?);
         *splashImage.setY(?);
         */
		
		//Adding a sequence of actions
		splashImage.addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(0.5f),Actions.delay(delay),Actions.run(new Runnable() {
            @Override
            public void run() {
            	game.setScreen(new MainMenu(game));// call the GameProject class
                //((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));// call the GameProject class
            }
        })));
		stage.addActor(splashImage); //adds the image as an actor to the stage
	}

	@Override
	public void render(float delta) {
		// The game class will keep calling render while the screen is set, with
		// delta as an interval between the current and the previous screen
		
		Gdx.gl.glClearColor(0, 0, 0, 1); // sets clear color to black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear the batch
		
		stage.act(); // update all actors
		stage.draw(); // draw all actors on the Stage.getBatch()
	}

	@Override
	public void resize(int width, int height) {
		// his method gets called when you resize the window, so everything that
		// needs to be changed on a resize must be placed in here

	}

	@Override
	public void pause() {
		// When the Game is paused, the code here should be executed first.

	}

	@Override
	public void resume() {
		// When we resume our Game, the following code should be executed first.

	}

	@Override
	public void hide() {
		// When we set another screen, this function is called on the old
		// screen. So everything you want to do when you leave the screen should
		// be put here.
		dispose();
	}

	@Override
	public void dispose() {
		// This function should be called when there is no need anymore for the
		// screen, so the resources it uses are released. You should call every
		// object�s dispose method in here.
		texture.dispose();
        stage.dispose();

	}

}
