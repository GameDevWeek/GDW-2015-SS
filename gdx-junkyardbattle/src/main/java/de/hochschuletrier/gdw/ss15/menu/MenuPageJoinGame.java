package de.hochschuletrier.gdw.ss15.menu;

import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.game.utils.Timer;
import de.hochschuletrier.gdw.ss15.states.GameplayState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ss15.events.network.Base.ConnectTryFinishEvent;
import de.hochschuletrier.gdw.ss15.events.network.Base.DoNotTouchPacketEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.NetworkReceivedNewPacketClientEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket.SimplePacketId;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.ChangeNamePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.MenuePlayerChangedPacket;
import de.hochschuletrier.gdw.ss15.menu.Actors.HorizontalGroupID;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;

import java.util.TimerTask;

public class MenuPageJoinGame extends MenuPage implements DoNotTouchPacketEvent.Listener {
	private Table scrollTableBlue = new Table();
	private Table scrollTableRed = new Table();
	private float height = 40;
	private float width = 130;
	private final DecoImage change = new DecoImage(assetManager.getTexture("change_button"));

	public int timetoStart = 0;

	private Label labelTimer;
	private Label labelMapName;
	private HorizontalGroupID hgBlue1 = createHGroup(1, -1, "");

	private HorizontalGroupID hgBlue2 = createHGroup(2, -1, "");
	private HorizontalGroupID hgBlue3 = createHGroup(3, -1, "");
	private HorizontalGroupID hgBlue4 = createHGroup(4, -1, "");

	private HorizontalGroupID hgRed1 = createHGroup(5, -1, "");
	private HorizontalGroupID hgRed2 = createHGroup(6, -1, "");
	private HorizontalGroupID hgRed3 = createHGroup(7, -1, "");
	private HorizontalGroupID hgRed4 = createHGroup(8, -1, "");

	private java.util.Timer timer = new java.util.Timer();

	private float widthChange = 60;
	private float heightChange = 30;
	private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);

	public MenuPageJoinGame(Skin skin, MenuManager menuManager, String background, String name) {
		super(skin, background);

		DoNotTouchPacketEvent.registerListener(this);

		labelTimer = new Label("60", skin);
		labelTimer.setWidth(widthChange);
		labelTimer.setHeight(heightChange);
		labelMapName=new Label("4 x 4", skin);
		labelMapName.setWidth(115);
		labelMapName.setHeight(30);
		addUIActor(labelTimer, 492, (int) (155 - heightChange), null);
		addCenteredImage(450, 85 - 30, 115, 30, change, () -> SendPacketClientEvent
				.emit(new SimplePacket(SimplePacketId.ChangeTeamPacket.getValue(), 0), true));
		addUIActor(labelMapName, 475, (int) (60-labelMapName.getHeight()), null);
		hgBlue1.setWidth(width);
		hgBlue1.setHeight(height);
		hgRed1.setWidth(width);
		hgRed1.setHeight(height);
		addUIActor(hgBlue1, 90, (int) (375 - height), null);
		addUIActor(hgBlue2, 90, (int) (310 - height), null);
		addUIActor(hgBlue3, 90, (int) (230 - height), null);
		addUIActor(hgBlue4, 90, (int) (150 - height), null);
		addUIActor(hgRed1, 700, (int) (375 - height), null);
		addUIActor(hgRed2, 700, (int) (310 - height), null);
		addUIActor(hgRed3, 700, (int) (230 - height), null);
		addUIActor(hgRed4, 700, (int) (150 - height), null);

		// netzwerk
		
		ChangeNamePacket pack = new ChangeNamePacket(name);
		SendPacketClientEvent.emit(pack, true);

		//SimplePacket spack = new SimplePacket(SimplePacketId.ChangeTeamPacket.getValue(), 0);
		//SendPacketClientEvent.emit(spack, true);



		timer.schedule(new TimerTask()
		{
			@Override
			public void run ()
			{
				// Your database code here
				if(timetoStart!=0)
				{
					timetoStart--;
					labelTimer.setText(""+timetoStart);
					if(labelTimer.getText().length>=3)
					{
						labelTimer.setPosition(488,(int) (155 - heightChange));
					}
					else
					{
						labelTimer.setPosition(492,(int) (155 - heightChange));
					}
				}
			}
		},1000,1000);
	}

	@Override
	public void onDoNotTouchPacket(Packet pack) {
		// TODO Auto-generated method stub
		//System.out.println("test");
		//System.out.print(pack.getPacketId());
		if (pack.getPacketId() == PacketIds.Simple.getValue())
		{// einafche
																// nachricht

			//System.out.println("received simpile packet");
			SimplePacket sPack = (SimplePacket) pack;
			if(sPack.m_SimplePacketId == SimplePacketId.TimeMenuePacket.getValue())
			{//timer ist gespawned
				//System.out.println("Received time: " + sPack.m_Moredata);
				timetoStart=(int)sPack.m_Moredata;
			}
			/*if (sPack.m_SimplePacketId == SimplePacket.SimplePacketId.TimeToStartPacket.getValue()) {
				// team wechseln
				long time = sPack.m_Moredata;
			}*/
		} else if (pack.getPacketId() == PacketIds.MenuInfo.getValue()) {

			
			MenuePlayerChangedPacket pPack = (MenuePlayerChangedPacket) pack;
			int idPlayer = pPack.id;
			String name = pPack.name;
			boolean team = pPack.team; // false RED
			//System.out.println("team:" + team);
			//System.out.println("ID: " + idPlayer);
			deletePlayer(idPlayer, team);
			addPlayer(idPlayer, name, team);

		}
	}

	private void addPlayer(int idPlayer, String name, boolean team) {
		if (team) {
			if (hgBlue1.idPlayer == -1) {
				hgBlue1.idPlayer = idPlayer;

				((Label) (hgBlue1.getChildren().get(0))).setText( name);
				return;
			}

			if (hgBlue2.idPlayer == -1) {
				hgBlue2.idPlayer = idPlayer;

				((Label) (hgBlue2.getChildren().get(0))).setText( name);
				return;
			}
			if (hgBlue3.idPlayer == -1) {
				hgBlue3.idPlayer = idPlayer;

				((Label) (hgBlue3.getChildren().get(0))).setText( name);
				return;
			}
			if (hgBlue4.idPlayer == -1) {
				hgBlue4.idPlayer = idPlayer;

				((Label) (hgBlue4.getChildren().get(0))).setText( name);
				return;
			}

		} else {
			if (hgRed1.idPlayer == -1) {
				hgRed1.idPlayer = idPlayer;

				((Label) (hgRed1.getChildren().get(0))).setText(name);
				return;
			}
			if (hgRed2.idPlayer == -1) {
				hgRed2.idPlayer = idPlayer;

				((Label) (hgRed2.getChildren().get(0))).setText(name);
				return;
			}
			if (hgRed3.idPlayer == -1) {
				hgRed3.idPlayer = idPlayer;
				((Label) (hgRed3.getChildren().get(0))).setText(name);
				return;
			}
			if (hgRed4.idPlayer == -1) {
				hgRed4.idPlayer = idPlayer;
				((Label) (hgRed4.getChildren().get(0))).setText(name);
				return;
			}
		}

	}

	private void deletePlayer(int idPlayer, boolean team) {

			if (hgBlue1.idPlayer == idPlayer) {
				
				hgBlue1.idPlayer = -1;

				((Label) (hgBlue1.getChildren().get(0))).setText("");
				return;
			}
			if (hgBlue2.idPlayer == idPlayer) {
				hgBlue2.idPlayer = idPlayer;
				hgBlue2.idPlayer = -1;

				((Label) (hgBlue2.getChildren().get(0))).setText("");
				return;
			}
			if (hgBlue3.idPlayer == idPlayer) {
				hgBlue3.idPlayer = idPlayer;
				hgBlue3.idPlayer = -1;

				((Label) (hgBlue3.getChildren().get(0))).setText("");
				return;
			}
			if (hgBlue4.idPlayer == idPlayer) {
				hgBlue4.idPlayer = idPlayer;
				hgBlue4.idPlayer = -1;

				((Label) (hgBlue4.getChildren().get(0))).setText("");
				return;
			}

		
			if (hgRed1.idPlayer == idPlayer) {
				hgRed1.idPlayer = -1;
				((Label) (hgRed1.getChildren().get(0))).setText("");
				return;
			}
			if (hgRed2.idPlayer == idPlayer) {
				hgRed2.idPlayer = -1;
				((Label) (hgRed2.getChildren().get(0))).setText("");
				return;
			}
			if (hgRed3.idPlayer == idPlayer) {
				hgRed3.idPlayer = -1;
				((Label) (hgRed3.getChildren().get(0))).setText("");
				return;
			}
			if (hgRed4.idPlayer == idPlayer) {
				hgRed4.idPlayer = -1;
				((Label) (hgRed4.getChildren().get(0))).setText("");
				
			}

		
	}

	private HorizontalGroupID createHGroup(int id, int idPlayer, String name) {
		HorizontalGroupID hg = new HorizontalGroupID(id, idPlayer);
		hg.idPlayer = idPlayer;
		hg.addActor(new Label(name, skin));
		return hg;
	}


	private void changeTeam(boolean team, int id) {
		if (team) {
			Array<Actor> hglist = scrollTableBlue.getChildren();
			for (Actor hg : hglist) {

				if (((HorizontalGroupID) hg).id == id) {
					scrollTableRed.add(hg);
					scrollTableBlue.removeActor(hg);

				}

			}

		} else {
			Array<Actor> hglist = scrollTableRed.getChildren();
			for (Actor hg : hglist) {

				if (((HorizontalGroupID) hg).id == id) {
					scrollTableBlue.add(hg);
					scrollTableRed.removeActor(hg);

				}

			}
		}
	}

}
