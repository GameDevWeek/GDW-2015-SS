package de.hochschuletrier.gdw.ss15;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundDistanceModel;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter.Mode;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyManager;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.ScreenUtil;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.ss15.game.GameGlobals;
import de.hochschuletrier.gdw.ss15.game.Server;
import de.hochschuletrier.gdw.ss15.game.network.ClientConnection;
import de.hochschuletrier.gdw.ss15.game.network.PacketIds;
import de.hochschuletrier.gdw.ss15.game.utils.LoadedMaps;
import de.hochschuletrier.gdw.ss15.sandbox.SandboxCommand;
import de.hochschuletrier.gdw.ss15.states.LoadGameState;
import de.hochschuletrier.gdw.ss15.states.MainMenuState;

/**
 *
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {

    //-----------------------------------------server on off-------------------
    private static final boolean m_StartServerByGameStart = false;
    //-------------------------------------------------------------------------

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static CommandLine cmdLine;

    public static final boolean IS_RELEASE = ClassUtils.getClassUrl(Main.class).getProtocol().equals("jar");

    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1024;

    private final AssetManagerX assetManager = new AssetManagerX();
    private static Main instance;

    public final DevConsole console = new DevConsole(16);
    private final DevConsoleView consoleView = new DevConsoleView(console);
    private Skin consoleSkin;
    public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private final CVarEnum<SoundDistanceModel> distanceModel = new CVarEnum<SoundDistanceModel>("snd_distanceModel", SoundDistanceModel.INVERSE, SoundDistanceModel.class, 0, "sound distance model");
    private final CVarEnum<SoundEmitter.Mode> emitterMode = new CVarEnum<Mode>("snd_mode", SoundEmitter.Mode.STEREO, SoundEmitter.Mode.class, 0, "sound mode");
    private final Hotkey toggleFullscreen = new Hotkey(()->ScreenUtil.toggleFullscreen(), Input.Keys.ENTER, HotkeyModifier.ALT);
    
    public static HashMap<String,LoadedMaps> maps;

    //------------netowrk------------
    private Server server = null;
    private final ClientConnection clientConnection = new ClientConnection();
    public ClientConnection getClientConnection(){return clientConnection;}

    public Main() {
        super(new BaseGameState());
        GameGlobals.assetManager = assetManager;
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    /**
     * @return the assetManager
     */
    public AssetManagerX getAssetManager() {
        return assetManager;
    }

    private void setupDummyLoader() {
    }

    private void loadAssetLists() {
        TextureParameter param = new TextureParameter();
        param.minFilter = param.magFilter = Texture.TextureFilter.Linear;
        param.genMipMaps = true;

        assetManager.loadAssetList("data/json/images.json", Texture.class, param);
        assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);
        assetManager.loadAssetList("data/json/music.json", Music.class, null);
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationExtended.class,
                AnimationExtendedLoader.AnimationExtendedParameter.class);
        BitmapFontParameter fontParam = new BitmapFontParameter();
        fontParam.flip = true;
        assetManager.loadAssetList("data/json/fonts.json", BitmapFont.class, fontParam);
        assetManager.loadAssetList("data/json/particles.json", ParticleEffect.class, null);
    }

    private void setupGdx() {
        KeyUtil.init();
        Gdx.graphics.setContinuousRendering(true);

        Gdx.input.setCatchMenuKey(true);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void create() {
        CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Local));
        DrawUtil.init();
        setupDummyLoader();
        loadAssetLists();
        setupGdx();
        SoundInstance.init();
        clientConnection.init();

        consoleSkin = new Skin(Gdx.files.internal("data/skins/basic.json"));
        consoleView.init(consoleSkin);
        addScreenListener(consoleView);
        inputMultiplexer.addProcessor(consoleView.getInputProcessor());
        inputMultiplexer.addProcessor(HotkeyManager.getInputProcessor());

        changeState(new LoadGameState(assetManager, this::onLoadComplete), null, null);

        this.console.register(distanceModel);
        distanceModel.addListener((CVar) -> distanceModel.get().activate());

        this.console.register(emitterMode);
        emitterMode.addListener(this::onEmitterModeChanged);
        toggleFullscreen.register();
    }

    private void onLoadComplete() {
        final MainMenuState mainMenuState = new MainMenuState(assetManager);
        addPersistentState(mainMenuState);
        changeState(mainMenuState, null, null);
        SandboxCommand.init(assetManager);
        
        if (cmdLine.hasOption("sandbox")) {
            SandboxCommand.runSandbox(cmdLine.getOptionValue("sandbox"));
        }

        Main.getInstance().console.register(serverCommand);

        LoadMaps();

        if(m_StartServerByGameStart) {
            server = new Server(12345);
            server.start();
            logger.info("Server wurde gestartet");
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        DrawUtil.batch.dispose();
        consoleView.dispose();
        consoleSkin.dispose();
        SoundEmitter.disposeGlobal();

        if(server!=null)
        {
            logger.debug("Server stoped by closing");
            server.stop();
        }
    }

    protected void preRender() {
        DrawUtil.clearColor(Color.BLACK);
        DrawUtil.clear();
        DrawUtil.resetColor();

        DrawUtil.batch.begin();
    }

    protected void postRender() {
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
        MusicManager.update(delta);
        SoundEmitter.updateGlobal();

        preRender();
    }

    @Override
    protected void postUpdate(float delta) {
        postRender();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        SoundEmitter.setListenerPosition(width / 2, height / 2, 10, emitterMode.get());
    }

    public void onEmitterModeChanged(CVar cvar) {
        int x = Gdx.graphics.getWidth() / 2;
        int y = Gdx.graphics.getHeight() / 2;
        SoundEmitter.setListenerPosition(x, y, 10, emitterMode.get());
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Battle For Metal";
        cfg.width = WINDOW_WIDTH;
        cfg.height = WINDOW_HEIGHT;
        cfg.useGL30 = false;
        cfg.vSyncEnabled = false;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;

        parseOptions(args);
        new LwjglApplication(getInstance(), cfg);
        PacketIds.RegisterPackets();
    }

    @SuppressWarnings("static-access")
    private static void parseOptions(String[] args) throws IllegalArgumentException {
        CommandLineParser cmdLineParser = new PosixParser();

        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("sandbox")
                .withDescription("Start a Sandbox Game")
                .withType(String.class)
                .hasArg()
                .withArgName("Sandbox Classname")
                .create());

        try {
            cmdLine = cmdLineParser.parse(options, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //----------------------------------------server stuff---------------------------------------


    ConsoleCmd serverCommand = new ConsoleCmd("server", 0, "startet oder beendet server", 1) {
        @Override
        public void execute(List<String> list) {

            String info = list.get(1);
            if(info.equals("start")) {
                if (list.size() >= 3) {
                    startServer(Integer.parseInt(list.get(2)));
                } else {
                    startServer(12345);
                }
            }
            else if(info.equals("stop"))
            {
                stopServer();
            }
            else
            {
                logger.error(info+" falsches parameter für command server");
            }
        }
    };


    public static void LoadMaps()
    {
        try {
            maps = JacksonReader.readMap("data/json/maps.json", LoadedMaps.class);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // Get Server

    public Server getServer(){
        return server;
    }



    public boolean startServer(int port)
    {
        if(server == null){
            server = new Server(port);
            if(server.start())
            {
                logger.info("Server gestartet");
            }
            else
            {
                logger.error("Server konnte nicht gestartet werden");
                return false;
            }
        }
        else {
            logger.error("Server läuft bereits");
        }
        return true;
    }

    public void stopServer()
    {
       if(server==null)
        {
            //logger.error("Server läuft nicht");
        }
        else
        {
            logger.info("Server wird beendet ...");
            server.stop();
            logger.info("Server wurde beendet");
            server=null;
        }
    }
}
