package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.Base.ConnectTryFinishEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket.SimplePacketId;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.enums.ConnectStatus;

public class MenuPageStart extends MenuPage /*implements ConnectTryFinishEvent.Listener , DoNotTouchPacketEvent.Listener*/ {
	private final DecoImage imageJoin = new DecoImage(assetManager.getTexture("join_button"));
	private final DecoImage imageHost = new DecoImage(assetManager.getTexture("host_button"));
	
	private final DecoImage imageBack= new DecoImage(assetManager.getTexture("back_button"));
	
	private MenuManager menuManager;
	
	private int buttonImageWidth=100;
	private int buttonImageHeight=30;

	
	Runnable runnablejoin = new Runnable() {
		
		@Override
		public void run() {
			MenuPageEnterIP page= new MenuPageEnterIP(skin, menuManager, "enterip_bg");
			menuManager.addLayer(page);
			menuManager.pushPage(page);
		}
	};
	Runnable runnablehost= new Runnable() {
		
		@Override
		public void run() {
			MenuPageHostGame page= new MenuPageHostGame(skin, menuManager, "host_bg");
			menuManager.addLayer(page);
			menuManager.pushPage(page);
			
		}
	};
	public MenuPageStart(Skin skin, MenuManager menuManager, String background) {
		super(skin, background);
		this.menuManager= menuManager;
		imageJoin.setWidth(buttonImageWidth);
		imageJoin.setHeight(buttonImageHeight);
		imageHost.setWidth(buttonImageWidth);
		imageHost.setHeight(buttonImageHeight);
		
		imageBack.setWidth(60);
		imageBack.setHeight(30);
		addCenteredImage(460, 315, buttonImageWidth,(int)imageJoin.getHeight(), imageJoin,runnablejoin);
		addCenteredImage(460, 215, buttonImageWidth,buttonImageHeight, imageHost,runnablehost);
		addCenteredImage(355, 40, 60,30, imageBack, ()-> menuManager.popPage());
		/*
		ConnectTryFinishEvent.registerListener(this);
		DoNotTouchPacketEvent.registerListener(this);
		*/
	}
	/*
	@Override
	public void onConnectFinishPacket(boolean status)
	{
		if(!status)
		{
			
		}
	}
	@Override
	public void onDoNotTouchPacket(Packet pack) {
		// TODO Auto-generated method stub
		if(pack.getPacketId()==PacketIds.Simple.getValue())
		{
			SimplePacket sPack = (SimplePacket) pack;
			if(sPack.m_SimplePacketId==SimplePacket.SimplePacketId.ConnectInitPacket.getValue())
			{
				if(sPack.m_Moredata==1)
				{	
				//MenuPage page= new MenuPageJoinGame(skin, menuManager, "join_bg");
					MenuPage page = new MenuPageEnterIP(skin, menuManager, "menu_bg");
					menuManager.addLayer(page);
					menuManager.pushPage(page);
				}
			}
		}
	}
	*/

	
	
	
}
