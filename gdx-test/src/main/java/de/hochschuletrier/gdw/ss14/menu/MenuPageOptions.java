package de.hochschuletrier.gdw.ss14.menu;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss14.menu.MenuPageRoot.Type;

public class MenuPageOptions extends MenuPage {

	public MenuPageOptions(Skin skin, MenuManager manager, Type type) {
		//Skin für die Optionsseite wird übergeben
		super(skin, "menu_bg");
		
		VerticalGroup vg = new VerticalGroup();
		
		HorizontalGroup option = addSlider(stylename, text, runnable);
		vg.addActor(option);
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
