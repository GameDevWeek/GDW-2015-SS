package de.hochschuletrier.gdw.ss14.menu;


import com.badlogic.gdx.graphics.g2d.Batch;
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

    protected final void addLeftAlignedButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x, y, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.left);
    }
    protected final VerticalGroup addVerticalGroup(VerticalGroup group)
    {
    	addActor(group);
    	return group;
    }

    protected final HorizontalGroup addSlider(int x, int y, int width, int height, String text, Runnable runnable) {
    	Slider sl = new Slider(0, 100, 1, false, skin);
    	Label l = new Label(text, skin);
    	
    	HorizontalGroup hg = new HorizontalGroup();
    	hg.setBounds(0,0, 600, 600);
    	
    	hg.addActor(l);
    	hg.addActor(sl);
    	
    	//addActor(hg);
    	return hg;
    	
    }
    
    protected final HorizontalGroup addCheckBox(int x, int y, int width, int height, String text, Runnable runnable) {
    	CheckBox cb = new CheckBox(text, skin);
    	Label l = new Label(text, skin);
    	
    	
    	HorizontalGroup hg = new HorizontalGroup();
    	hg.setBounds(0,0, 600, 600);
    	
    	hg.addActor(cb);
    	hg.addActor(l);
    	
    	//addActor(hg);
    	return hg;
    }
    
    
    protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "default");
        button.getLabel().setAlignment(Align.center);
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
