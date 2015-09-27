package de.hochschuletrier.gdw.ss15.menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
import de.hochschuletrier.gdw.ss15.events.network.Base.ConnectTryFinishEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.utils.Dataholder;
import de.hochschuletrier.gdw.ss15.game.utils.LoadedMaps;
import de.hochschuletrier.gdw.ss15.menu.Actors.Bar;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class MenuPageHostGame extends MenuPage implements ConnectTryFinishEvent.Listener, DoNotTouchPacketEvent.Listener {
	ArrayList<Texture> imageArray= new ArrayList<>();
	int indexImageArray=0;
	
	
	private int buttonImageWidth=100;
	private int buttonImageHeight=30;

	int actualMap = 1;

	
	private final DecoImage imageMap = new DecoImage(assetManager.getTexture("map1"));
	private final DecoImage imageHost = new DecoImage(assetManager.getTexture("host_button"));

	private final DecoImage imageChangeMapRight = new DecoImage(assetManager.getTexture("changeMap_button_right"));
	private final DecoImage imageChangeMapLeft = new DecoImage(assetManager.getTexture("changeMap_button_left"));

	MenuManager menuManager;
	
	Label labelIP = new Label("", skin);
	TextArea textAreaPort= new TextArea("12345", skin);
	
	TextButton textButtonHostGame = new TextButton("Server hosten", skin);
	
	//Unneeded
	String ServerIP=null;
	
	Runnable runnableHost= new Runnable() {
		
		@Override
		public void run() {
			try {
				String temp = textAreaPort.getText();

				int port = Integer.parseInt((temp.trim()));

				hostGame(port);
				
			} catch (Exception e) {
				
			}


		}
	};
	ScrollPane scrollPaneMembers= new ScrollPane(null);
	private Runnable runnableChangeMapRight= new Runnable() {
		
		@Override
		public void run() {

			actualMap++;
			if (actualMap > Main.getInstance().maps.size()) {
				actualMap = 1;
			}

			imageMap.setTexture(assetManager.getTexture("map" + actualMap));

			Dataholder.MapId.set(actualMap);
		}
	};
	private Runnable runnableChangeMapLeft= new Runnable() {

		@Override
		public void run() {

			actualMap--;
			if (actualMap < 1 ) {
				actualMap = Main.maps.size();
			}

			imageMap.setTexture(assetManager.getTexture("map" + actualMap));

			Dataholder.MapId.set(actualMap);
		}
	};

	public MenuPageHostGame(Skin skin, MenuManager menuManager, String background) {
		super(skin, background);

		this.menuManager = menuManager;

		textAreaPort.setWidth(235);

		imageHost.setWidth(135);
		imageHost.setHeight(20);
		
		imageChangeMapLeft.setWidth(55);
		imageChangeMapLeft.setHeight(30);
		imageChangeMapRight.setWidth(55);
		imageChangeMapRight.setHeight(30);
		imageMap.setWidth(150);
		imageMap.setHeight(145);
		addCenteredImage(633, 141, (int) imageChangeMapRight.getWidth(), (int) imageChangeMapRight.getHeight(), imageChangeMapRight, runnableChangeMapRight);
		addCenteredImage(331, 141, (int) imageChangeMapLeft.getWidth(), (int) imageChangeMapLeft.getHeight(), imageChangeMapLeft, runnableChangeMapLeft);
		addCenteredImage(420, 365, buttonImageWidth, buttonImageHeight, imageHost, runnableHost);
		addUIActor(textAreaPort, 390, (int) (290 - textAreaPort.getHeight()), null);
		addUIActor(imageMap, 420, (int) (215 - imageMap.getHeight()), null);
		addUIActor(labelIP, 783, (int) (120 - labelIP.getHeight()), null);

		Dataholder.MapId.set(actualMap);

		try {
			String adresse = InetAddress.getLocalHost().toString();
			adresse = adresse.substring(0,adresse.indexOf('/'));
			labelIP.setText(adresse);
		}
		catch (UnknownHostException ex)
		{
			labelIP.setText("Keine Ip");
		}

		ConnectTryFinishEvent.unregisterAll();
		ConnectTryFinishEvent.unregisterAll();

		ConnectTryFinishEvent.registerListener(this);
		DoNotTouchPacketEvent.registerListener(this);
	}

	//TODO
	protected void hostGame(int port) {

		if(Main.getInstance().startServer(port))
		{
			if(!Main.getInstance().getClientConnection().connect("localhost",port))
			{
				Main.getInstance().stopServer();
				labelIP.setText("Fehler");
			}
		}
		else
		{
			labelIP.setText("Fehler");
		}
	}

	@Override
	public void onDoNotTouchPacket(Packet pack) {
		// TODO Auto-generated method stub
		if (pack.getPacketId() == PacketIds.Simple.getValue()) {
			SimplePacket sPack = (SimplePacket) pack;
			if (sPack.m_SimplePacketId == SimplePacket.SimplePacketId.ConnectInitPacket.getValue()) {
				if (sPack.m_Moredata == 1) {
					// MenuPage page= new MenuPageJoinGame(skin, menuManager,
					// "join_bg");
					MenuPage page = new MenuPageJoinGame(skin, menuManager, "join_bg", "Host");
					menuManager.addLayer(page);
					menuManager.pushPage(page);
				}
				else if(sPack.m_Moredata == -1)
				{
					labelIP.setText("Fehler");
				}
				else if(sPack.m_Moredata == -2)
				{
					labelIP.setText("Fehler");
				}
				else
				{
					labelIP.setText("Fehler");
				}
			}
		}
	}

	@Override
	public void onConnectFinishPacket(boolean status) {
		if (!status) {
			labelIP.setText("Verbindungsfehler");
			Main.getInstance().stopServer();
		}
	}
	
}
