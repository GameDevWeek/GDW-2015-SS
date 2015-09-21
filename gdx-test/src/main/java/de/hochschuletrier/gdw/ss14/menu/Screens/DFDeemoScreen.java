package de.hochschuletrier.gdw.ss14.menu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.menuExample.GameProject;
import com.mygdx.Assets.*;
import com.mygdx.Assets.Assets.DistanceFieldShader;

public class DFDeemoScreen implements Screen {

	private GameProject game;
	private DistanceFieldShader dfShader;
	private Texture textureDF = new Texture(Gdx.files.internal(Assets.PATH_TO_FONT), true);// true enables mipmaps
	private Texture textureDefault= new Texture(Gdx.files.internal(Assets.Path_TO_DEFAULTFONT),true);
	private Texture textureArial= new Texture(Gdx.files.internal(Assets.PATH_TO_FONTARIAL),true);
	
	private BitmapFont dFFont;
	private BitmapFont defaultFont;
	private BitmapFont arialFont;
	
	
	public DFDeemoScreen(GameProject game) {
		this.game = game;
		// create DistanceField_Shader and Stuff
		this.dfShader = new DistanceFieldShader();
		//DISTANCEfield Font
		textureDF.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear); // linear filtering in nearest mipmap image
		dFFont = new BitmapFont(Gdx.files.internal(Assets.PATH_TO_FONTFILE), new TextureRegion(textureDF), false);
		//Arial DF Font
		textureArial.setFilter(TextureFilter.Linear,TextureFilter.Linear);
		arialFont= new BitmapFont(Gdx.files.internal(Assets.PATH_TO_FONTARIALFILE),new TextureRegion(textureArial), false);
		//DefaultFont without DistanceField
		textureDefault.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Linear);
		defaultFont= new BitmapFont(Gdx.files.internal(Assets.Path_TO_DEFAULTFONTFILE), new TextureRegion(textureDefault), false);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}




	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		
		defaultFont.draw(game.batch, "Normal Font ", 0,game.HEIGHT-20);
		
		dFFont.draw(game.batch, "without distancefield", 0, game.HEIGHT-20-defaultFont.getLineHeight());
		arialFont.draw(game.batch, "ARIAL: without distanceField", 0,game.HEIGHT-20-defaultFont.getLineHeight()-dFFont.getLineHeight());
		
		game.batch.setShader(this.dfShader);
		
		dFFont.draw(game.batch, "with distancefield",0, game.HEIGHT-20-defaultFont.getLineHeight()-dFFont.getLineHeight()-arialFont.getLineHeight());
		arialFont.draw(game.batch, "ARIAL: with distanceField", 0,game.HEIGHT-20-defaultFont.getLineHeight()-dFFont.getLineHeight()-arialFont.getLineHeight()-dFFont.getLineHeight());
		
		
		//setting DefaultShader
		game.batch.setShader(null);
		
		//Handle Scale
		if (Gdx.input.isKeyPressed(Keys.LEFT))
		{
			System.out.println("Scale : minus 1,2");
			defaultFont.getData().setScale(defaultFont.getData().scaleX/2f);
			dFFont.getData().setScale(dFFont.getData().scaleX/2f);
			arialFont.getData().setScale(arialFont.getData().scaleX/2f);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			System.out.println("Scale : plus 1,2");
			defaultFont.getData().setScale(defaultFont.getData().scaleX*2);
			dFFont.getData().setScale(dFFont.getData().scaleX*2);
			arialFont.getData().setScale(arialFont.getData().scaleX*2);
		}
		if(Gdx.input.isKeyPressed(Keys.BACKSPACE))
		{game.setScreen( new MainMenu(game));}
		game.batch.end();
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
