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

public class MenuPageStart extends MenuPage implements ConnectTryFinishEvent.Listener , DoNotTouchPacketEvent.Listener {
	private final DecoImage imageJoin = new DecoImage(assetManager.getTexture("join_button"));
	private final DecoImage imageHost = new DecoImage(assetManager.getTexture("host_button"));
	private MenuManager menuManager;
	
	private int buttonImageWidth=100;
	private int buttonImageHeight=30;
	
	private String ip = "localhost";
	private int port = 12345;
	
	Runnable connectToServer = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(Main.getInstance().getClientConnection().connect(ip, port)==false)
			{
				//fehler anzeigen
			}
		}
	};
	public MenuPageStart(Skin skin, MenuManager menuManager, String background) {
		super(skin, background);
		this.menuManager= menuManager;
		imageJoin.setWidth(buttonImageWidth);
		imageJoin.setHeight(buttonImageHeight);
		imageHost.setWidth(buttonImageWidth);
		imageHost.setHeight(buttonImageHeight);
		addCenteredImage(460, 315, buttonImageWidth,(int)imageJoin.getHeight(), imageJoin,connectToServer);/**/;
		addCenteredImage(460, 215, buttonImageWidth,buttonImageHeight, imageHost,()->{/*Change Screen*/});
		
		ConnectTryFinishEvent.registerListener(this);
		DoNotTouchPacketEvent.registerListener(this);
	}
	@Override
	public void onConnectFinishPacket(boolean status)
	{
		if(!status)
		{
			
		}
		System.out.println(status);
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
				MenuPage page= new MenuPageJoinGame(skin, menuManager, "join_bg");
					menuManager.addLayer(page);
					menuManager.pushPage(page);
				}
			}
		}
	}

	
	
	
}
