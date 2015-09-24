package de.hochschuletrier.gdw.ss15.menu;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;


import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ss15.menu.Actors.Bar;
import de.hochschuletrier.gdw.ss15.menu.MenuPageRoot.Type;


public class MenuPageOptions extends MenuPage {

	private final DecoImage imageMinus = new DecoImage(assetManager.getTexture("minus_ui"));
	private final DecoImage imagePlus = new DecoImage(assetManager.getTexture("plus_ui"));
	private ArrayList<Actor> hg= new ArrayList<>();  
	
	private Bar bar= new Bar(0, 0, 100,0);
    
    ClickListener plusClicked= new ClickListener()
    {
    	@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick Bar");
			
			bar.increaseMaxValue(1);
			// or System.exit(0);
		}
    };
    ClickListener minusClicked= new ClickListener()
    {
    	@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick Bar");
			
			bar.decreaseMaxValue(1);
			// or System.exit(0);
		}
    };
	
	public MenuPageOptions(Skin skin, MenuManager menuManager, Type type) {
		//Skin für die Optionsseite wird übergeben
		super(skin, "menu_bg");

        addPageEntry(menuManager, 0, 500 , "Spiel beitreten", new MenuPageEnterIP(skin,menuManager,"menu_bg"));
		imageMinus.setWidth(20);
		imageMinus.addListener(plusClicked);
		imagePlus.setHeight(20);
		imagePlus.addListener(minusClicked);
		
		hg.add(imageMinus);
		hg.add(bar);
		hg.add(imagePlus);
		
		addHorizontalGroupe(hg, 380, 245);
		bar.setY(380);
		addUIActor(bar, 555, 380,null);
		
		
	}
	protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
    }


}
