package de.hochschuletrier.gdw.ss15.game;

public class GameConstants {

    // Priorities for entity systems
    public static final int PRIORITY_PHYSIX = 0;
    public static final int PRIORITY_UPDATE_PHYSIX = 5;
    public static final int PRIORITY_UPDATE_POSITION = 6;
    public static final int PRIORITY_ENTITIES = 10;
    public static final int PRIORITY_ANIMATIONS = 20;
    public static final int PRIORITY_RENDER_SYSTEM = 29;
    public static final int PRIORITY_DEBUG_WORLD = 30;
    public static final int PRIORITY_HUD = 40;
    public static final int PRIORITY_REMOVE_ENTITIES = 1000;

    // PooledEngine parameters
    public static final int ENTITY_POOL_INITIAL_SIZE = 32;
    public static final int ENTITY_POOL_MAX_SIZE = 256;
    public static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    public static final int COMPONENT_POOL_MAX_SIZE = 256;

    // Physix parameters
    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int BOX2D_SCALE = 40;
    public static final float GATHERING_RANGE = 10000f;
    
    // Light parameters
    public static final int LIGHT_RAYS = 360;
    public static final float LIGHT_AMBIENT = 0.4f;
    public static final boolean LIGHT_BLUR = true;
    public static final int LIGHT_BLURNUM = 2;
    public static final boolean LIGHT_SHADOW = true;
    public static final boolean LIGHT_DIFFUSE = false;
    
    // Music parameters
    public static float MUSIC_FADE_TIME = 2;

    // PlayerParameters
    public static final int START_HEALTH = 100;
    public static final int MAX_METALSHARDS = 100000;
    public static final int MIN_METALSHARDS = 0;
    public static final float START_X_POSITION = 0;
    public static final float START_Y_POSITION = 0;
    public static final float DYING_TIMER = 1000;
    public static final float MINING_PER_SECOND = 20;
    public static final float RESPAWN_TIMER = 30000;
    public static final float PROTECTION_DURATION = 4000;
}
