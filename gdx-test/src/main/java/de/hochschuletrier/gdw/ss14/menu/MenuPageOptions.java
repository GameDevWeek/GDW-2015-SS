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
		//Skin f?r die Optionsseite wird ?bergeben
		super(skin, "menu_bg");
		
		VerticalGroup vg = new VerticalGroup();
		vg.setPosition(30, 30);
		vg.setBounds(0, 0, 60, 30);
		vg.addActor(addSlider("Option1", null));
		//vg.addActor(addCheckBox("Option2", null));
		
	}


}
