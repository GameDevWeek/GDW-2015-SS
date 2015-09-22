package de.hochschuletrier.gdw.ss14.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Button;


import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss14.menu.MenuPageRoot.Type;

public class MenuPageOptions extends MenuPage {

	public MenuPageOptions(Skin skin, MenuManager manager, Type type) {
		//Skin für die Optionsseite wird übergeben
		super(skin, "menu_bg");
		
		VerticalGroup vg = new VerticalGroup();
		/*
		vg.addActor(addSlider(30, 30, 600, 30, "Option1", null));
		vg.addActor(addCheckBox(30, 60, 600, 30, "Option2", null));
		*/
	}


}
