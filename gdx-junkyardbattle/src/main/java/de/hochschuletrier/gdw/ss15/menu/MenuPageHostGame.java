package de.hochschuletrier.gdw.ss15.menu;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
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
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.menu.Actors.Bar;

public class MenuPageHostGame extends MenuPage{
	ArrayList<Texture> imageArray= new ArrayList<>();
	int indexImageArray=0;
	
	
	private int buttonImageWidth=100;
	private int buttonImageHeight=30;
	
	Texture textureMap1=assetManager.getTexture("map1");
	Texture textureMap2=assetManager.getTexture("map2");
	Texture textureMap3=assetManager.getTexture("map3");
	Texture textureMap4=assetManager.getTexture("map4");
	
	private final DecoImage imageMap = new DecoImage(textureMap1);
	private final DecoImage imageHost = new DecoImage(assetManager.getTexture("host_button"));

	private final DecoImage imageChangeMapRight = new DecoImage(assetManager.getTexture("changeMap_button_right"));
	private final DecoImage imageChangeMapLeft = new DecoImage(assetManager.getTexture("changeMap_button_left"));
	
	
	Label labelIP = new Label("IP: ", skin);
	TextArea textAreaPort= new TextArea("", skin);
	
	TextButton textButtonHostGame = new TextButton("Server hosten", skin);
	
	//Unneeded
	String ServerIP=null;
	
	Runnable runnableHost= new Runnable() {
		
		@Override
		public void run() {
			try {
				String temp = textAreaPort.getText();
				
				int port = Integer.parseInt((temp.trim()));
				
				hostGame(port,indexImageArray);
				
			} catch (Exception e) {
				
			}
			
			
		}
	};
	ScrollPane scrollPaneMembers= new ScrollPane(null);
	private Runnable runnableChangeMapRight= new Runnable() {
		
		@Override
		public void run() {
			System.out.println("right"+ indexImageArray);
			
			if(indexImageArray<imageArray.size()-1)
			{
			indexImageArray++;
			imageMap.setTexture(imageArray.get(indexImageArray));
			}
			
		}
	};
	private Runnable runnableChangeMapLeft= new Runnable() {
		
		@Override
		public void run() {
			System.out.println("left" + indexImageArray);
			
			if(indexImageArray>0)
			{
			indexImageArray--;
			imageMap.setTexture(imageArray.get(indexImageArray));
			}
			
		}
	};

	public MenuPageHostGame(Skin skin, MenuManager menuManager, String background) {
		super(skin, background);
		textAreaPort.setWidth(235);
		
		imageArray.add(textureMap1);
		imageArray.add(textureMap2);
		imageArray.add(textureMap3);
		imageArray.add(textureMap4);
		
		
		imageHost.setWidth(135);
		imageHost.setHeight(20);
		
		imageChangeMapLeft.setWidth(55);
		imageChangeMapLeft.setHeight(30);
		imageChangeMapRight.setWidth(55);
		imageChangeMapRight.setHeight(30);
		imageMap.setWidth(150);
		imageMap.setHeight(145);
		addCenteredImage(633, 141,(int) imageChangeMapRight.getWidth(),(int) imageChangeMapRight.getHeight(), imageChangeMapRight, runnableChangeMapRight);
		addCenteredImage(331, 141,(int) imageChangeMapLeft.getWidth(),(int) imageChangeMapLeft.getHeight(), imageChangeMapLeft, runnableChangeMapLeft);
		addCenteredImage(420, 365, buttonImageWidth, buttonImageHeight, imageHost, runnableHost);
		addUIActor(textAreaPort,  390, (int) (290-textAreaPort.getHeight()), null);
		addUIActor(imageMap, 420, (int) (215-imageMap.getHeight()), null);
		addUIActor(labelIP, 783, (int) (120-labelIP.getHeight()), null);
		

		
		
	}

	//TODO
	protected void hostGame(int port, int map) {
	
		Main.getInstance().startServer();
		

		
	}

	
}
