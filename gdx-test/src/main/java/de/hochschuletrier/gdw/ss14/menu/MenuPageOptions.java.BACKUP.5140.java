package de.hochschuletrier.gdw.ss14.menu;

<<<<<<< HEAD


import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
=======
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
>>>>>>> 1f1fa4cf36594d37057b8ff4192b209a5eb41eb0
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss14.menu.MenuPageRoot.Type;

<<<<<<< HEAD

=======
>>>>>>> 1f1fa4cf36594d37057b8ff4192b209a5eb41eb0
public class MenuPageOptions extends MenuPage {

	public MenuPageOptions(Skin skin, MenuManager manager, Type type) {
		//Skin für die Optionsseite wird übergeben
		super(skin, "menu_bg");
		
<<<<<<< HEAD
		VerticalGroup vg = new VerticalGroup();
		vg.addActor(addSlider(30, 30, 60, 30, "Option1", null));
		vg.addActor(addCheckBox(30, 60, 60, 30, "Option2", null));
		
		addActor(vg);
=======
>>>>>>> 1f1fa4cf36594d37057b8ff4192b209a5eb41eb0
	}


}
