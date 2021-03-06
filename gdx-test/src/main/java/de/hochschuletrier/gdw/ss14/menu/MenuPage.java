package de.hochschuletrier.gdw.ss14.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ss14.Main;

public class MenuPage extends Group {

    protected Main main = Main.getInstance();
    protected AssetManagerX assetManager = main.getAssetManager();
    protected final Skin skin;
    protected final int WIDTH_BUTTON=400;
    protected final int HEIGHT_BUTTON=50;
    protected final int YSTEP_BUTTON = 55;
    
    public MenuPage(Skin skin, String background) {
        super();
        this.skin = skin;
        
        Gdx.input.setCursorImage(new Pixmap(Gdx.files.internal("data/ui/menu/MagnetMouse.png")), 0, 0);
        
        addActor(new DecoImage(assetManager.getTexture(background)));

        setVisible(false);
    }

    @Override
    public void act(float delta) {
        if (isVisible()) {
            super.act(delta);
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        if (clipBegin(0, 0, getWidth(), getHeight())) {
            super.drawChildren(batch, parentAlpha);
            clipEnd();
        }
    }
    protected final void addHorizontalGroupe(ArrayList<Actor> list,int x, int y)
    {
    	HorizontalGroup hg= new HorizontalGroup();
    	hg.setPosition(x, y);
    	for (Actor tempActor : list) {
			hg.addActor(tempActor);
		}
    	addActor(hg);
    }
    protected final void addLeftAlignedButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x, y, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.left);
    }
    protected final VerticalGroup addVerticalGroup(VerticalGroup group)
    {
    	addActor(group);
    	return group;
    }

    protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.center);
    }

    protected final HorizontalGroup addSlider(String text, Runnable runnable) {
       
    	HorizontalGroup hg = new HorizontalGroup();
    	Label option = new Label(text, skin);  	
    	
    	
    	Slider sl = new Slider(0, 100, 1, false, skin);
        hg.addActor(option);
        hg.addActor(sl);
        addActor(hg);
    	return hg;
    	
    }
    

    protected final Actor addUIActor(Actor actor,int x, int y)

    {
    	actor.setPosition(x, y);
    	addActor(actor);
    	return actor;
    }
    
    protected final TextButton addButton(int x, int y, int width, int height, String text, Runnable runnable, String style) {
        TextButton button = new TextButton(text, skin, style);
        button.setBounds(x, y, width, height);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
        addActor(button);
        return button;
    }
}
