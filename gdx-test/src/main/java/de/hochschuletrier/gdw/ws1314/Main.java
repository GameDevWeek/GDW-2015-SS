package de.hochschuletrier.gdw.ws1314;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.SleepDummyLoader;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitVerticalTransition;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * 
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {

	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 512;

	private final AssetManagerX assetManager = new AssetManagerX();
	private OrthographicCamera camera;
	private static Main instance;

	public final DevConsole console = new DevConsole(16);
	private final DevConsoleView consoleView = new DevConsoleView(console);
	private Skin skin;
	public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();

	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}

	private Texture crosshair;
	private final Vector2 cursor = new Vector2();

	private final InputProcessor inputProcessor = new InputAdapter() {
		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			cursor.set(screenX, screenY);
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			cursor.set(screenX, screenY);
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			cursor.set(screenX, screenY);
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			cursor.set(screenX, screenY);
			return false;
		}
	};

	private void setupDummyLoader() {
		// Just adding some sleep dummies for a progress bar test
		InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
		assetManager.setLoader(SleepDummyLoader.SleepDummy.class, new SleepDummyLoader(
				fileHandleResolver));
		SleepDummyLoader.SleepDummyParameter dummyParam = new SleepDummyLoader.SleepDummyParameter(
				100);
		for (int i = 0; i < 50; i++) {
			assetManager.load("dummy" + i, SleepDummyLoader.SleepDummy.class, dummyParam);
		}
	}

	private void packImages() {
		TexturePacker.process("src/main/resources/pipeline/images",
				"src/main/resources/data/images", "atlas");
	}

	private void loadAssetLists() {
		TextureParameter param = new TextureParameter();
		param.minFilter = param.magFilter = Texture.TextureFilter.Linear;
		assetManager.loadAssetList("data/json/images.json", Texture.class, param);

		assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);
		assetManager.loadAssetList("data/json/music.json", Music.class, null);

		assetManager.loadAssetListWithParam("data/json/animations.json", Animation.class,
				AnimationLoader.AnimationParameter.class);
	}

	private void setupGdx() {
		KeyUtil.init();
		// Texture.setEnforcePotImages(false);
		// Gdx.graphics.setTitle("LibGDX Test");
		// Gdx.graphics.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
		Gdx.graphics.setContinuousRendering(true);
		// Disable VSync for the loading state, to speed things up
		// This will be enabled when loading is done
		Gdx.graphics.setVSync(false);

		Gdx.input.setCatchMenuKey(true);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(inputMultiplexer);

		inputMultiplexer.addProcessor(inputProcessor);

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void create() {
		CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Internal));
		packImages();
		setupDummyLoader();
		loadAssetLists();
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		setupGdx();
		skin = new Skin(Gdx.files.internal("data/skins/basic.json"));
		consoleView.init(assetManager, skin);
		addScreenListener(consoleView);
		inputMultiplexer.addProcessor(consoleView.getInputProcessor());

		GameStates.LOADING.init(assetManager);
		GameStates.LOADING.activate();
	}

	public void onLoadComplete() {
		crosshair = assetManager.getTexture("crosshair");

		GameStates.MAINMENU.init(assetManager);
		GameStates.GAMEPLAY.init(assetManager);
		GameStates.MAINMENU.activate(new SplitVerticalTransition(500).reverse(), null);
	}

	@Override
	public void dispose() {
		DrawUtil.batch.dispose();
		GameStates.dispose();
		consoleView.dispose();
		skin.dispose();
	}

	@Override
	protected void preRender() {
		DrawUtil.clearColor(Color.BLACK);
		DrawUtil.clear();
		DrawUtil.resetColor();

		DrawUtil.updateCamera(camera);
		DrawUtil.batch.begin();
	}

	@Override
	protected void postRender() {
		if (crosshair != null) {
			DrawUtil.batch.draw(crosshair, cursor.x - crosshair.getWidth() * 0.5f,
					cursor.y - crosshair.getHeight() * 0.5f);
		}
		DrawUtil.batch.end();
		if (consoleView.isVisible()) {
			consoleView.render();
		}
	}

	@Override
	protected void preUpdate(float delta) {
		if (consoleView.isVisible()) {
			consoleView.update(delta);
		}
		console.executeCmdQueue();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		DrawUtil.init(width, height);

		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(true, width, height);
		camera.position.set(width / 2, height / 2, 0);
		camera.update();
		DrawUtil.updateCamera(camera);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "LibGDX Test";
		cfg.width = WINDOW_WIDTH;
		cfg.height = WINDOW_HEIGHT;
		cfg.useGL30 = true;

		new LwjglApplication(getInstance(), cfg);
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
