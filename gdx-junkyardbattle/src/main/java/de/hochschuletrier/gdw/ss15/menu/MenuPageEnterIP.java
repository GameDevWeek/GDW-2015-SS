package de.hochschuletrier.gdw.ss15.menu;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.components.network.server.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.basic.SocketConnectListener;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;
import de.hochschuletrier.gdw.ss15.states.GameplayState;

public class MenuPageEnterIP extends MenuPage implements SocketConnectListener {

	ArrayList<Actor> horizontalGroupeContent = new ArrayList<>();
	Label labelIP = new Label("IP: ", skin);
	Label labelPort= new Label("Port: ", skin);
	
	TextArea textAreaIP = new TextArea("", skin);
	TextArea textAreaPort= new TextArea("", skin);
	
	TextButton textButtonConnectToServer = new TextButton("Server suchen", skin);
	TextButton textButtonStartGame = new TextButton("Spiel starten", skin);
	
	//Unneeded
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
		
		
		VerticalGroup vgTemp1= new VerticalGroup();
		VerticalGroup vgTemp2= new VerticalGroup();
		
		vgTemp1.addActor(labelIP);
		vgTemp1.addActor(labelPort);
		
		vgTemp2.addActor(textAreaIP);
		vgTemp2.addActor(textAreaPort);
		
		horizontalGroupeContent.add(vgTemp1);
		horizontalGroupeContent.add(vgTemp2);
		horizontalGroupeContent.add(textButtonConnectToServer);
		horizontalGroupeContent.add(textButtonAbort);
		horizontalGroupeContent.add(textButtonStartGame);
		
		addHorizontalGroupe(horizontalGroupeContent, 100, 100);
		
		
		//scrollPaneMembers.add
		//remove
		scrollPaneMembers.debug();
		addUIActor(scrollPaneMembers, 100, 500,null);
	}

	// TODO: Connect to gameServer 
	protected void connectToServer() {
		String iP = textAreaIP.getText();
		int port;
		try {
			port = Integer.getInteger(textAreaPort.getText());
		} catch (Exception e) {
			System.out.println("LOG: UI : Wrong Port");
			port=-1;
		}
		// Connected?
	
		if (!Main.getInstance().getClientConnection().connect(iP, port)) {
			
			
		}

		Main.getInstance().getClientConnection().getSocket().registerConnectListner(this);
		
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

	//Called when connected
	@Override
	public void loginFinished(ConnectStatus status) {
		// TODO Auto-generated method stub
		if(status==ConnectStatus.Succes)
		{
		textButtonStartGame.setVisible(true);
		}
		
		
	}
}
