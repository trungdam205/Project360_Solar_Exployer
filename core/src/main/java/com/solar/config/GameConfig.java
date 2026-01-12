package com. solar.config;

public class GameConfig {

    // Screen dimensions
    public static final float WORLD_WIDTH = 1920f;
    public static final float WORLD_HEIGHT = 1200f;

    // Player constants
    public static final float PLAYER_WIDTH = 100f;
    public static final float PLAYER_HEIGHT = 100f;
    public static final float WALK_SPEED = 280f;
    public static final float RUN_SPEED = 420f;
    public static final float BASE_JUMP_VELOCITY = 750f;
    public static final float GRAVITY_FORCE = 2000f;
    public static final float EARTH_GRAVITY = 1.0f;

    // Camera
    public static final float CAMERA_LERP = 5f;

    // World
    public static final float WORLD_EXTENT = 5000f;
    public static final float TERRAIN_HEIGHT_RATIO = 0.28f;

    // Asset paths
    public static final String SKIN_PATH = "uiskin.json";
    public static final String PLANET_ATLAS_PATH = "planets/planets.atlas";
    public static final String PLAYER_ATLAS_PATH = "player.atlas";
    public static final String BACKGROUND_PATH = "background/background.png";

    // Obstacle paths
    public static final String OBSTACLE_PATH = "Obstacle/";

    private GameConfig() {
        // Prevent instantiation
    }
}
