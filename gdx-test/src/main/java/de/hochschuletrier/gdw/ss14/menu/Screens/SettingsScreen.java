package de.hochschuletrier.gdw.ss14.menu.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.Actors.MyActor;
import com.mygdx.Assets.Assets;

public class SettingsScreen implements Screen {

	private Game game;
	private Stage stage;
	private Table table;
	private ScrollPane scrollPane;
	private TextButton buttonBack;
	private Slider sliderVolume;
	private Slider sliderSpeed;
	
	
	public SettingsScreen(Game game) {
		this.game=game;
	}

	
	@Override
	public void show() {
	     table= new Table(Assets.defaultSkin);
         buttonBack= new TextButton("Back", Assets.defaultSkin);
         sliderVolume= new Slider(0, 100, 10, false,Assets.defaultSkin);
         
         sliderSpeed= new Slider(0, 10, 1, false,Assets.defaultSkin);
         scrollPane = new ScrollPane(sliderSpeed, Assets.defaultSkin);
         scrollPane.setWidget(sliderVolume);
         
         table.setFillParent(true);
         
         table.add(scrollPane).expandX().row();
         table.add(buttonBack).expandX().row();
         table.add(new MouseCursorActor());
         table.setDebug(true);
         stage= new Stage();
         stage.addActor(table);
         Gdx.input.setInputProcessor(stage);
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

