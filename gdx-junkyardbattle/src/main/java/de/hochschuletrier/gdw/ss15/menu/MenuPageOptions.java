package de.hochschuletrier.gdw.ss15.menu;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ss15.menu.Actors.Bar;
import de.hochschuletrier.gdw.ss15.menu.Actors.TextureActor;
import de.hochschuletrier.gdw.ss15.menu.MenuPageRoot.Type;

public class MenuPageOptions extends MenuPage {

	private final DecoImage imageMinusMusic = new DecoImage(assetManager.getTexture("minus_ui"));
	private final DecoImage imagePlusMusic = new DecoImage(assetManager.getTexture("plus_ui"));
	private final DecoImage imageMinusSound = new DecoImage(assetManager.getTexture("minus_ui"));
	private final DecoImage imagePlusSound = new DecoImage(assetManager.getTexture("plus_ui"));
	
	private final int iconWidth = 40;
	private final int iconHeight = 40;
	private ArrayList<Actor> hg = new ArrayList<>();
	private int peding = 2;

	private Bar barMusic = new Bar(25, 200, 410, 255);
	private Bar barSound = new Bar(25, 200, 410, 125);

	ClickListener plusClickedMusic = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick Bar");

			barMusic.increaseMaxValue(5);
			imagePlusMusic.rotateBy(5);
			// or System.exit(0);
		}
	};
	ClickListener minusClickedMusic = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			System.out.println("LOG: onclick Bar");

			barMusic.decreaseMaxValue(5);
			imageMinusMusic.rotateBy(-5);
			// or System.exit(0);
		}
	};
	Runnable actionPlusMusic = new Runnable() {

		@Override
		public void run() {
			barMusic.increaseMaxValue(5);
			// SOund Plus
			imagePlusMusic.rotateBy(5);
		}
	};
	Runnable actionMinusMusic = new Runnable() {

		@Override
		public void run() {
			barMusic.decreaseMaxValue(5);
			// SOund minus
			imageMinusMusic.rotateBy(-5);
		}
	};
	Runnable actionPlusSound = new Runnable() {

		@Override
		public void run() {
			barSound.increaseMaxValue(5);
			// SOund Plus
			imagePlusMusic.rotateBy(5);
		}
	};
	Runnable actionMinusSound = new Runnable() {

		@Override
		public void run() {
			barSound.decreaseMaxValue(5);
			// SOund minus
			imageMinusSound.rotateBy(-5);
		}
	};

	public MenuPageOptions(Skin skin, MenuManager menuManager, String background) {
		// Skin für die Optionsseite wird übergeben
		super(skin, background);

		imageMinusMusic.setWidth(iconWidth);
		imageMinusMusic.setHeight(iconHeight);
		// imageMinus.addListener(plusClicked);
		imagePlusMusic.setWidth(iconWidth);
		imagePlusMusic.setHeight(iconHeight);// imagePlus.addListener(minusClicked);

		imageMinusSound.setWidth(iconWidth);
		imageMinusSound.setHeight(iconHeight);
		// imageMinus.addListener(plusClicked);
		imagePlusSound.setWidth(iconWidth);
		imagePlusSound.setHeight(iconHeight);
		
		addCenteredImage(350, 250/*-iconHeight*/, iconWidth, iconHeight, imageMinusMusic, actionMinusMusic);
		addCenteredImage(620, 250/*-iconHeight*/, iconWidth, iconHeight, imagePlusMusic, actionPlusMusic);
		addCenteredImage(350, 120/*-iconHeight*/, iconWidth, iconHeight, imageMinusSound, actionMinusSound);
		addCenteredImage(620, 120/*-iconHeight*/, iconWidth, iconHeight, imagePlusSound, actionPlusSound);
		//ImageButton imgb= new ImageButton(new );

		addUIActor(barMusic, 0, (int) barMusic.getHeight(), null);
		addUIActor(barSound, 0, 0, null);
		// textureMinus.addListener(minusClicked);
		// addUIActor(textureMinus, 0, 0,null);
		// addImageButton(x, y, width, height, runnable)(630-iconWidth,
		// 345-iconHeight, imagePlus, actionPlus);
		// hg.add(imageMinus);
		// hg.add(bar);
		// hg.add(imagePlus);
		// addHorizontalGroupe(hg, 380, 245);

		// addUIActor(bar, 555, 380,null);

	}

	protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
		menuManager.addLayer(page);
		addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
	}

}
