package de.hochschuletrier.gdw.ss14.menu;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.states.GameplayState;

public class MenuPageEnterIP extends MenuPage {

	ArrayList<Actor> horizontalGroupeContent = new ArrayList<>();
	TextField textFieldIP = new TextField("Enter IP:", skin);
	TextArea textAreaIP = new TextArea("", skin);
	TextButton textButtonConnectToServer = new TextButton("Server suchen", skin);
	TextButton textButtonStartGame = new TextButton("Spiel starten", skin);
	String ServerIP=null;
	
	ScrollPane scrollPaneMembers= new ScrollPane(null);

	public MenuPageEnterIP(Skin skin, MenuManager menuManager, String background) {
		super(skin, background);
		textButtonStartGame.setVisible(false);
		textButtonStartGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				startGame();

			}
		});
		textButtonConnectToServer.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				connectToServer();

			}

		});

		TextButton textButtonAbort = new TextButton("Abbrechen", skin);
		textButtonAbort.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				menuManager.popPage();

			}

		});
		
		
		horizontalGroupeContent.add(textFieldIP);
		horizontalGroupeContent.add(textAreaIP);
		horizontalGroupeContent.add(textButtonConnectToServer);
		horizontalGroupeContent.add(textButtonAbort);
		horizontalGroupeContent.add(textButtonStartGame);
		
		addHorizontalGroupe(horizontalGroupeContent, 100, 100);
		
		
		//scrollPaneMembers.add
		//remove
		scrollPaneMembers.debug();
		addUIActor(scrollPaneMembers, 100, 500);
	}

	// TODO: Connect to gameServer 
	protected void connectToServer() {
		String iP = textAreaIP.getText();
		// Connected?
		if (true) {
			textButtonStartGame.setVisible(true);
			ServerIP=iP;
		}

	}

	private void startGame() {
		if(ServerIP!=null)
		{
		if (!main.isTransitioning()) {
			Game game = new Game();
			game.init(assetManager);
			main.changeState(new GameplayState(assetManager, game), new SplitHorizontalTransition(500), null);
		}
		}
	}
}
