package de.hochschuletrier.gdw.ss14.menu.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.mygdx.Assets.Assets;
import com.mygdx.menuExample.GameProject;

public class MainMenu implements Screen {
	private static final Skin PATH_TO_SKIN = new Skin(Gdx.files.internal("data/ui/menu/skins/menu.json "));

	private final Game game;

	// Actors
	private Stage stage = new Stage();
	
	
	private TextButton tButtonPlay = new TextButton("Play !", PATH_TO_SKIN);
	private TextButton tButtonHigscore = new TextButton("Higscore", PATH_TO_SKIN);
	private TextButton tButtonExit = new TextButton("Exit", PATH_TO_SKIN);
	private TextButton tButtonSettings = new TextButton("Settings", PATH_TO_SKIN);
	private TextButton tButtonDF = new TextButton("Distance-Field-Demo", PATH_TO_SKIN);
	
	private Table table = new Table(PATH_TO_SKIN);

	private ClickListener onClickPlayButton = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick PlayButton");
			GameProject.life=100;
			game.setScreen(new GameScreen((GameProject) game));
			

		}
	};
	private ClickListener onClickExitButton = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick ExitButton");
			Gdx.app.exit();
			// or System.exit(0);
		}
	};
	private ClickListener onCLickHigscoreButton = new ClickListener()

	{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick HighscoreButton");
			game.setScreen(new HighscoreScreen(game));
			// or System.exit(0);
		}
	};
	private ClickListener onClickSettingsButton = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick SettingsButton");
			game.setScreen(new SettingsScreen((GameProject) game));
			// ((Game) Gdx.app.getApplicationListener()).setScreen( new
			// GameScreen());

		}
	};
	private ClickListener onClickDFButton = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick PlayButton");
			game.setScreen(new DFDeemoScreen((GameProject) game));
			// ((Game) Gdx.app.getApplicationListener()).setScreen( new
			// GameScreen());

		}
	};

	public MainMenu(final Game game2) {
		this.game = game2;

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

		tButtonPlay.addListener(onClickPlayButton);

		tButtonExit.addListener(onClickExitButton);

		tButtonHigscore.addListener(onCLickHigscoreButton);
		tButtonDF.addListener(onClickDFButton);

		tButtonSettings.addListener(onClickSettingsButton);
		// same for a VerticalGroup
		/*
		 * vGroup.addActor(tButtonPlay); vGroup.addActor(tButtonExit);
		 * vGroup.setFillParent(true); stage.addActor(vGroup);
		 */

		table.add("GameProject").padBottom(40).row();
		table.add(tButtonPlay).size(150, 60).padBottom(20).row();
		table.add(tButtonHigscore).size(150, 60).padBottom(20).row();
		table.add(tButtonSettings).size(150, 60).padBottom(20).row();
		table.add(tButtonDF).size(180, 60).padBottom(20).row();
		;
		table.add(tButtonExit).size(150, 60).padBottom(20).row();

		table.setFillParent(true);
		table.setDebug(false);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);// nicht vergessen ohne gehts nicht

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		// PATH_TO_SKIN.dispose();

	}

}
