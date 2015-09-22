package de.hochschuletrier.gdw.ss14.menu.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.Actors.MyActor;
import com.mygdx.Assets.Assets;

public class HighscoreScreen implements Screen {

	private Game game;
	private Stage stage;
	private Table table;
	private List<Integer> listHighscore;
	private TextButton buttonBack;
	private TextButton buttonDelete;
	private ClickListener onClickBackButton = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick BackButton");
			game.setScreen(new MainMenu(game));
			// ((Game) Gdx.app.getApplicationListener()).setScreen( new
			// GameScreen());

		}
	};
	
	public HighscoreScreen(Game game) {
		this.game=game;
	}

	
	@Override
	public void show() {
		
         table= new Table(Assets.defaultSkin);
         buttonBack= new TextButton("Back", Assets.defaultSkin);
         buttonBack.addListener(onClickBackButton);
         buttonDelete= new TextButton("Delete",Assets.defaultSkin);
         listHighscore= new List<Integer>(Assets.defaultSkin);
        
         // Assets.vectorHigscore.sort(c);
         for(int i=0;i<Assets.vectorHigscore.size();i++)
         {
        	 listHighscore.setItems(Assets.vectorHigscore.get(i));
        	 System.out.println(Assets.vectorHigscore.get(i));
        	 
         }
         
         
         table.setFillParent(true);
         table.add(listHighscore).expand().row();
         table.add(buttonBack).row();
         table.add(new MouseCursorActor());
         table.setDebug(false);
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
