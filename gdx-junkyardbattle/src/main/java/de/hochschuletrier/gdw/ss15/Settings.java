package de.hochschuletrier.gdw.ss15;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.hochschuletrier.gdw.commons.gdx.settings.BooleanSetting;
import de.hochschuletrier.gdw.commons.gdx.settings.SettingsUtil;

public class Settings {
    private static final Preferences prefs = Gdx.app.getPreferences(Settings.class.getName() + ".xml");

    public static final BooleanSetting FULLSCREEN = new BooleanSetting(prefs, "fullscreen", false);
    public static final BooleanSetting LIGHTS = new BooleanSetting(prefs, "lights", true);
    public static final BooleanSetting NORMAL_MAPPING = new BooleanSetting(prefs, "normal_mapping", true); 

    public static void flush() {
        prefs.flush();
    }

    public static void reset() {
        SettingsUtil.reset(Settings.class);
    }
}
