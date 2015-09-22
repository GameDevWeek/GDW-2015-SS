package de.hochschuletrier.gdw.ss14.menu.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.Assets.Assets;
import com.mygdx.menuExample.GameProject;

public class EndScreen implements Screen {
	Stage stage;// Stage base off every Scene2d Interface import. Method draw
				// act//calls the methods draw act in every child
	Table table;//hast to be set fillparent to center the stage
	
	//To call the different screens
	TextButton buttonRestart;
	TextButton buttonMainmenu;
	TextButton buttonHighscore;
	private Game game;
	private ClickListener onClickRestartButton = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick RestartButton");
			game.setScreen(new GameScreen((GameProject) game));
			GameProject.life=100;
			// ((Game) Gdx.app.getApplicationListener()).setScreen( new
			// GameScreen());

		}
	};
	private ClickListener onClickMainMenuButton = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick MainMenuButton");
			game.setScreen(new MainMenu(game));
			// ((Game) Gdx.app.getApplicationListener()).setScreen( new
			// GameScreen());

		}
	};
	private ClickListener onClickHigscoreButton = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick HighscoreButton");
			game.setScreen(new HighscoreScreen(game));
			// ((Game) Gdx.app.getApplicationListener()).setScreen( new
			// GameScreen());

		}
	};

	public EndScreen(Game game)
	{this.game=game;}
	
	

	@Override
	public void show() {
		
		stage= new Stage();
		table= new Table(Assets.defaultSkin);
		buttonRestart= new TextButton("Restart ",Assets.defaultSkin);
		
		buttonRestart.addListener(onClickRestartButton);
		buttonMainmenu=new TextButton("Main Menu ",Assets.defaultSkin);
		buttonMainmenu.addListener(onClickMainMenuButton);
		buttonHighscore=new TextButton("Highscore",Assets.defaultSkin);
		buttonHighscore.addListener(onClickHigscoreButton);
		
		table.setFillParent(true);
		table.setDebug(true);
		table.add(buttonRestart).size(150, 60).padBottom(20);
		table.row();
		table.add(buttonMainmenu).size(150, 60).padBottom(20);
		table.row();
		table.add(buttonHighscore).size(150, 60).padBottom(20);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);// nicht vergessen ohne gehts nicht

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);//clear the screen	
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//clears the ciolor buffer
		stage.act();//calls every action of the child Actors
		stage.draw();//draws every child Actor
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
		dispose();

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();

	}

}
