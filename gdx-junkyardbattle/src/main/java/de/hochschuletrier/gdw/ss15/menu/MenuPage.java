package de.hochschuletrier.gdw.ss15.menu;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ss15.Main;

public class MenuPage extends Group {

	protected Main main = Main.getInstance();
	protected AssetManagerX assetManager = main.getAssetManager();
	protected final Skin skin;
	protected final int WIDTH_BUTTON = 400;
	protected final int HEIGHT_BUTTON = 50;
	protected final int YSTEP_BUTTON = 55;

	public MenuPage(Skin skin, String background) {
		super();
		this.skin = skin;

		Gdx.input.setCursorImage(new Pixmap(Gdx.files.internal("data/ui/menu/Assets/mouse_ready.png")), 0, 0);


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

	protected final void addHorizontalGroupe(ArrayList<Actor> list, int x, int y) {
		HorizontalGroup hg = new HorizontalGroup();
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

	protected final VerticalGroup addVerticalGroup(VerticalGroup group) {
		addActor(group);
		return group;
	}

	protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable) {
		TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "default");
		button.getLabel().setAlignment(Align.center);
	}

	protected final HorizontalGroup addSlider(String stylename, String text, Runnable runnable) {

		HorizontalGroup hg = new HorizontalGroup();
		Label option = new Label(text, skin, stylename);
		Slider sl = new Slider(0, 100, 1, false, skin, stylename);
		hg.addActor(option);
		hg.addActor(sl);
		addActor(hg);
		return hg;

	}

	protected final Actor addUIActor(Actor actor, int x, int y, Runnable runnable) {
		actor.setPosition(x, y);
		addActor(actor);
		if (runnable != null)
			runnable.run();
		return actor;
	}

	protected final void addCenteredImage(int x, int y, int width, int height, DecoImage image, Runnable runnable) {
		image.setPosition(x, y);
		image.setTouchable(Touchable.enabled);

		image.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float width, float height) {
				if (runnable != null)
					runnable.run();

			}

		});

		image.addListener(new InputListener() {

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

				/*
				 * if(abspielen==true){ SoundEmitter.updateGlobal();
				 * SoundEmitter.playGlobal(assetManager.getSound("pmHover"),
				 * false);}
				 * 
				 */
				changeTextureActive(image);
			}

		});
		image.addListener(new InputListener() {

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				/*
				 * abspielen=true;
				 */
				changeTextureNotActive(image);
			}

		});

		addActor(image);
	}

	protected final TextButton addButton(int x, int y, int width, int height, String text, Runnable runnable,
			String style) {
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

	protected final Label addLabel(String text) {
		Label label = new Label(text, skin, "highscore");
		return label;
	}

	protected final ImageButton addImageButton(int x, int y, int width, int height, Runnable runnable) {
		ImageButton button = new ImageButton(skin);

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
	
	
	protected final void changeTextureActive(DecoImage image) {

		if (image.getRegion().getTexture() == assetManager.getTexture("start_button")) {
			image.setTexture(assetManager.getTexture("start_button_over"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("join_button")) {
			image.setTexture(assetManager.getTexture("join_button_over"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("host_button")) {
			image.setTexture(assetManager.getTexture("host_button_over"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("option_button")) {
			image.setTexture(assetManager.getTexture("option_button_over"));
			return;
		}

		if (image.getRegion().getTexture() == assetManager.getTexture("quit_button")) {
			image.setTexture(assetManager.getTexture("quit_button_over"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("changeMap_button_left")) {
			image.setTexture(assetManager.getTexture("changeMap_button_left_over"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("changeMap_button_right")) {
			image.setTexture(assetManager.getTexture("changeMap_button_right_over"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("back_button")) {
			image.setTexture(assetManager.getTexture("back_button_over"));
			return;
		}


	}

	protected final void changeTextureNotActive(DecoImage image) {

		if (image.getRegion().getTexture() == assetManager.getTexture("start_button_over")) {
			image.setTexture(assetManager.getTexture("start_button"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("join_button_over")) {
			image.setTexture(assetManager.getTexture("join_button"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("host_button_over")) {
			image.setTexture(assetManager.getTexture("host_button"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("option_button_over")) {
			image.setTexture(assetManager.getTexture("option_button"));
			return;
		}

		if (image.getRegion().getTexture() == assetManager.getTexture("quit_button_over")) {
			image.setTexture(assetManager.getTexture("quit_button"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("changeMap_button_left_over")) {
			image.setTexture(assetManager.getTexture("changeMap_button_left"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("changeMap_button_right_over")) {
			image.setTexture(assetManager.getTexture("changeMap_button_right"));
			return;
		}
		if (image.getRegion().getTexture() == assetManager.getTexture("back_button_over")) {
			image.setTexture(assetManager.getTexture("back_button"));
			return;
		}

	}

}
