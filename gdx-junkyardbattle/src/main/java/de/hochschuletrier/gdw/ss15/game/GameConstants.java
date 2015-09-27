package de.hochschuletrier.gdw.ss15.game;

import de.hochschuletrier.gdw.ss15.game.rendering.ZoomingModes.modes;

public class GameConstants {

    // Priorities for entity systems
    public static final int PRIORITY_PHYSIX = 0;
    public static final int PRIORITY_UPDATE_PHYSIX = 5;
    public static final int PRIORITY_UPDATE_POSITION = 6;
    public static final int PRIORITY_ENTITIES = 10;
    public static final int PRIORITY_ANIMATIONS = 20;
    public static final int PRIORITY_RENDER_SYSTEM = 29;
    public static final int PRIORITY_DEBUG_WORLD = 30;
    public static final int PRIORITY_ADD_EFFECT_SYSTEM = 33;
    public static final int PRIORITY_HUD = 40;
    public static final int PRIORITY_METAL_SHARD_DROP_SYSTEM = 50;
    public static final int PRIORITY_CLIENT_SHOOTING_SYSTEM = 60;
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
    public static final float GATHERING_RANGE = 180f;
    
    // Light parameters
    public static final int LIGHT_RAYS = 360;
    public static final float LIGHT_AMBIENT = 0.9f;
    public static final boolean LIGHT_BLUR = true;
    public static final int LIGHT_BLURNUM = 2;
    public static final boolean LIGHT_SHADOW = true;
    public static final boolean LIGHT_DIFFUSE = false;
    
    // Satellite
    public static final float SATELLITE_SPAWN_TIME = 30.f;
    
    // Music parameters
    public static float MUSIC_FADE_TIME = 2;

    // PlayerParameters
    public static final int START_HEALTH = 100;
    public static final float START_X_POSITION = 0;
    public static final float START_Y_POSITION = 0;
    public static final float DYING_TIMER = 2;
    
    public static final float MINING_PER_SECOND_STAGE_1 = 0f;
    public static final float MINING_PER_SECOND_STAGE_2 = 2.f;
    public static final float MINING_PER_SECOND_STAGE_3 = 4.0f;
    public static final float MINING_PER_SECOND_STAGE_4 = 13.0f;
    public static final float MINING_TIME_NEEDED_1 = 1.5f;
    public static final float MINING_TIME_NEEDED_2 = 2.5f;
    public static final float MINING_TIME_NEEDED_3 = 4.0f;
    public static final float MINING_TIME_NEEDED_4 = 10.0f;
    
    public static final float PLAYER_POINT_LIGHT_DISTANCE = 8.f;
    public static final float PLAYER_POINT_LIGHT_DISTANCE_CHARGED = 4.f;
    public static final float PLAYER_POINT_LIGHT_ALPHA = 0.7f;
    public static final float RESPAWN_TIMER = 5;
    public static final float PROTECTION_DURATION = 4;
    public static final float DEFAULT_LAYER = 2.f;
    
    // Camera constants
    public static final modes ZOOM_MODE = modes.pow4;
}
