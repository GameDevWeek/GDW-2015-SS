package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.network.Base.ConnectTryFinishEvent;
import de.hochschuletrier.gdw.ss15.events.network.client.SendPacketClientEvent;
import de.hochschuletrier.gdw.ss15.game.Game;
import de.hochschuletrier.gdw.ss15.menu.Actors.Bar;
import de.hochschuletrier.gdw.ss15.network.gdwNetwork.data.Packet;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.network.Packets.SimplePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.ChangeNamePacket;
import de.hochschuletrier.gdw.ss15.game.network.Packets.Menu.MenuePlayerChangedPacket;
import de.hochschuletrier.gdw.ss15.states.GameplayState;
import de.hochschuletrier.gdw.ss15.states.MainMenuState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ss15.events.network.Base.*;

public class MenuPageRoot extends MenuPage {

    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);
    private int width=235,height=45;
	
	private final DecoImage imageStart = new DecoImage(assetManager.getTexture("start_button"));
	private final DecoImage imageOption = new DecoImage(assetManager.getTexture("option_button"));
	private final DecoImage imageCredits = new DecoImage(assetManager.getTexture("credits_button"));
	private final DecoImage imageChange= new DecoImage(assetManager.getTexture("change_button"));
	MenuManager menuManager;
    public enum Type {

        MAINMENU,
        INGAME
    }

    Runnable runnableStart= new Runnable() {
		
		@Override
		public void run() {
			
			MenuPage page= new MenuPageStart(skin,menuManager,"start_bg");
			menuManager.addLayer(page);
			menuManager.pushPage(page);
			
		}
	};
	private Runnable runnableOption= new Runnable() {
		
		@Override
		public void run() {
			MenuPage page= new MenuPageOptions(skin,menuManager,"sound_bg");
			menuManager.addLayer(page);
			menuManager.pushPage(page);
			
		}
	};
	private Runnable runnableCredits= new Runnable() {
		
		@Override
		public void run() {
			System.out.println("check");
			MenuPage page= new MenuPageCredits(skin, menuManager);
			menuManager.addLayer(page);
			menuManager.pushPage(page);
			
		}
	};
    public MenuPageRoot(Skin skin, MenuManager menuManager, Type type) {
        super(skin, "menu_bg");
        this.menuManager=menuManager;
        
        imageStart.setWidth(width);
        imageStart.setHeight(height);
        imageOption.setWidth(width);
        imageOption.setHeight(height);
        imageCredits.setWidth(width);
        imageCredits.setHeight(height);
        
        int x = 100;
        int i = 0;
        int y = 370;
   
     
        if (type == Type.MAINMENU) {
        	
            addCenteredImage(390, 350-height, width, height, imageStart, runnableStart);
            addCenteredImage(390, 250-height, width, height,imageOption,runnableOption);
            addCenteredImage(390, 150-height, width, height, imageCredits, runnableCredits);
         
           
            
        } else {
            addLeftAlignedButton(x, y - YSTEP_BUTTON * (i++), WIDTH_BUTTON, HEIGHT_BUTTON, "Fortsetzen", () -> menuManager.popPage());
            addLeftAlignedButton(x, y - YSTEP_BUTTON * (i++), WIDTH_BUTTON, HEIGHT_BUTTON, "Spiel verlassen", this::stopGame);
        }
       
        addCenteredButton(menuManager.getWidth() - 80, 54, 100, 40, "Exit", () -> System.exit(-1));
    }

    private void StartButtonClicked()
    {
        if(!Main.getInstance().getClientConnection().connect("localhost", 12345))
        {
            logger.warn("Connect hat  nicht gelapt");
        }
    }

    private void startGame() {
        if (!main.isTransitioning()) {
            Game game = new Game();
            game.init(assetManager);
            main.changeState(new GameplayState(assetManager, game), new SplitHorizontalTransition(500), null);
        }
    }

    private void stopGame() {
        if (!main.isTransitioning()) {
            main.changeState(main.getPersistentState(MainMenuState.class));
        }
    }

    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
       
    }


}
