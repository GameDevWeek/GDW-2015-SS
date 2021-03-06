package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.Base.ConnectTryFinishEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

public class MenuPageEnterIP extends MenuPage
		implements ConnectTryFinishEvent.Listener, DoNotTouchPacketEvent.Listener {

	//ArrayList<Actor> horizontalGroupeContent = new ArrayList<>();

	MenuManager menuManager;

	TextArea textAreaPort = new TextArea("12345", skin);
	TextArea textAreaName = new TextArea("Noob", skin);
	TextArea textAreaIP = new TextArea("localhost", skin);


	Label labelError= new Label("", skin);
	private int width = 235, height = 45;

	private final DecoImage imageStart = new DecoImage(assetManager.getTexture("start_button"));
	/*
	 * addCenteredImage(390, 350-height, width, height, imageStart,
	 * runnableStart); imageStart.setWidth(width); imageStart.setHeight(height);
	 */

	ScrollPane scrollPaneMembers = new ScrollPane(null);
	private Runnable runnableStart = new Runnable() {

		@Override
		public void run() {
			try {

				String temp = textAreaPort.getText();
				String ip = "localhost";
				int port = Integer.parseInt((temp.trim()));
				if (textAreaIP.getText().trim().length() > 0);
				{
					ip = textAreaIP.getText();
				}



				if (Main.getInstance().getClientConnection().connect(ip, port) == false) {
					System.out.println("Could not Connect to Server");
					labelError.setText("Error");
				}
				else{
					labelError.setText("Connecting");
				}
			} catch (Exception e) {
				System.out.println(e);
				labelError.setText("Wrong Port");
			}

			// TODO Auto-generated method stub

		}
	};

	public MenuPageEnterIP(Skin skin, MenuManager menuManager, String background) {
		super(skin, background);
		this.menuManager = menuManager;
		imageStart.setWidth(width);
		imageStart.setHeight(height);
		textAreaName.setWidth(234);
		textAreaPort.setWidth(234);
		textAreaIP.setWidth(234);
		addCenteredImage(390, 375 - height, width, height, imageStart, runnableStart);
		addUIActor(textAreaName, 385, (int) (297 - textAreaName.getHeight()), null);
		addUIActor(textAreaPort, 385, (int) (221 - textAreaPort.getHeight()), null);
		addUIActor(textAreaIP, 385, (int) (142 - textAreaIP.getHeight()), null);
		addUIActor(labelError, 786, (int) (101-labelError.getHeight()), null);

		ConnectTryFinishEvent.unregisterAll();
		ConnectTryFinishEvent.unregisterAll();

		ConnectTryFinishEvent.registerListener(this);
		DoNotTouchPacketEvent.registerListener(this);
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
					MenuPage page = new MenuPageJoinGame(skin, menuManager, "join_bg", textAreaName.getText());
					menuManager.addLayer(page);
					menuManager.pushPage(page);
				}
				else if(sPack.m_Moredata == -1)
				{
					labelError.setText("Spiel voll");
				}
				else if(sPack.m_Moredata == -2)
				{
					labelError.setText("Spiel läuft schon");
				}
				else
				{
					labelError.setText("Interner Fehler");
				}
			}
		}
	}

	@Override
	public void onConnectFinishPacket(boolean status) {
		if (!status) {
			labelError.setText("Verbindungsfehler");
		}
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if(visible == true)
		{
			Main.getInstance().getClientConnection().disconnect();
		}
	}

}
