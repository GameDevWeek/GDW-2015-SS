package de.hochschuletrier.gdw.ss15.menu;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss15.Main;

public class MenuPageHostGame extends MenuPage{
	ArrayList<Actor> horizontalGroupeContent = new ArrayList<>();
	Label labelIP = new Label("IP: ", skin);
	Label labelPort= new Label("Port: ", skin);
	
	TextArea textAreaIP = new TextArea("", skin);
	TextArea textAreaPort= new TextArea("", skin);
	
	TextButton textButtonHostGame = new TextButton("Server hosten", skin);
	
	//Unneeded
	String ServerIP=null;
	
	
	ScrollPane scrollPaneMembers= new ScrollPane(null);

	public MenuPageHostGame(Skin skin, MenuManager menuManager, String background) {
super(skin, background);
		
		
		textButtonHostGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				hostGame();

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
		horizontalGroupeContent.add(textButtonHostGame);
		horizontalGroupeContent.add(textButtonAbort);
		
		
		addHorizontalGroupe(horizontalGroupeContent, 100, 100);
		
		
		//scrollPaneMembers.add
		//remove
		scrollPaneMembers.debug();
		addUIActor(scrollPaneMembers, 100, 500,null);
	}

	//TODO
	protected void hostGame() {
		String iP = textAreaIP.getText();
		int port;
		try {
			port = Integer.getInteger(textAreaPort.getText());
		} catch (Exception e) {
			System.out.println("LOG: UI : Wrong Port");
			port=-1;
		}
		// Connected?
	
		

		
	}

	
}
