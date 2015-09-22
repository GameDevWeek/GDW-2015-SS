package de.hochschuletrier.gdw.ss14.menu;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss14.menu.MenuPageRoot.Type;



public class MenuPageOptions extends MenuPage {
	
	ArrayList<HorizontalGroup> optionen = new ArrayList<HorizontalGroup>();

	public MenuPageOptions(Skin skin, MenuManager manager, Type type) {
		//Skin für die Optionsseite wird übergeben
		super(skin, "menu_bg");
		

    	HorizontalGroup hg = new HorizontalGroup();    	
    	Label option = new Label("Option1", skin);
    	Slider sl = new Slider(0, 100, 1, false, skin);
    	
    	
    	
        hg.addActor(option);
        hg.addActor(sl);     
        hg.setBounds(0,0, 600, 600);
    	optionen.add(hg);
    	
    	CheckBox cb = new CheckBox("Option2", skin);
    	option.setText("Option2");
    	hg.addActor(cb);
    	hg.addActor(option);
    	optionen.add(hg);
    	
    	
        VerticalGroup vg = new VerticalGroup();
		vg.setBounds(0,0, 600,600);
		for(HorizontalGroup bla: optionen)
		{
			vg.addActor(bla);
			
		}
    	
		addVerticalGroup(vg);    	
		
	}

	

}
