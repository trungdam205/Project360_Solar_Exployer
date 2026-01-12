package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.solar.MainGame;
import com.solar.config.GameConfig;
import com.solar.data.CelestialData;
import com.solar.planet.*;

import static com.solar.config.GameConfig.CAMERA_LERP;

/**
 * Planet exploration screen - refactored to use modular components
 */
public class PlanetScreen extends BaseScreen {

    // References
    private final CelestialData planet;
    private final BaseScreen previousScreen;

    // Camera
    private OrthographicCamera gameCamera;
    private float cameraX;

    // Components
    private PlanetPhysics physics;
    private PlanetPlayer player;
    private PlanetTerrain terrain;
    private PlanetObstacles obstacles;
    private PlanetUI ui;

    // World
    private float groundY;
    private float worldWidth;

    // State
    private boolean reachedGoal;

    public PlanetScreen(MainGame game, CelestialData planet, BaseScreen previousScreen) {
        super(game);
        this.planet = planet;
        this.previousScreen = previousScreen;
    }

    @Override
    public void show() {
        super.show();

        // Initialize camera
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // World setup
        worldWidth = GameConfig.WORLD_EXTENT;
        groundY = WORLD_HEIGHT * GameConfig.TERRAIN_HEIGHT_RATIO;
        cameraX = WORLD_WIDTH / 2f;

        // Initialize physics (only once)
        physics = new PlanetPhysics(planet);
        Gdx.app.log("PlanetScreen", "=== INITIALIZING ===");
        Gdx.app.log("PlanetScreen", "Planet: " + planet.name);
        Gdx.app.log("PlanetScreen", "Exploration: " + (planet.exploration != null ? "OK" : "NULL"));
        Gdx.app.log("PlanetScreen", "Physics created: " + physics.getPlanetType());
        // Initialize components with physics
        player = new PlanetPlayer();
        player.init(150f, groundY, worldWidth, physics);

        terrain = new PlanetTerrain(planet, groundY, worldWidth, WORLD_HEIGHT);
        obstacles = new PlanetObstacles(planet, groundY, worldWidth, physics);

        ui = new PlanetUI(stage, skin, planet, physics, WORLD_WIDTH, WORLD_HEIGHT);
        ui.init(
                () -> setScreenWithFade(previousScreen),  // onBack
                this::toggleGravity,                       // onToggleGravity
                this::resetLevel                           // onRetry
        );

        // Initial state
        reachedGoal = false;

        Gdx.app.log("PlanetScreen", "Initialized for " + planet.name +
                " (" + physics.getPlanetType() + ", " + physics.getPlanetGravity() + "g)");
    }

    private void toggleGravity() {
        try {
            if (physics != null) {
                physics.toggleGravity();
                Gdx.app. log("PlanetScreen", "Gravity toggled: " + physics.isPlanetGravityEnabled() +
                        ", Current:  " + physics.getCurrentGravity());
            }

            if (ui != null) {
                ui.updateGravityDisplay();
            }
        } catch (Exception e) {
            Gdx.app.error("PlanetScreen", "Error toggling gravity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void resetLevel() {
        physics.reset();
        player.reset();
        ui.resetGravityUI();
        reachedGoal = false;
        cameraX = WORLD_WIDTH / 2f;
    }

    @Override
    public void render(float delta) {
        // Update
        if (!player.isDead() && !reachedGoal) {
            handleInput();
            update(delta);
        }

        // Clear screen with dark space color
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Update camera
        gameCamera.position.x = cameraX;
        gameCamera.position.y = WORLD_HEIGHT / 2f;
        gameCamera.update();

        // Render starfield background (uses game camera for parallax)
        renderStarfield();

        // Render terrain
        game.shapeRenderer.setProjectionMatrix(gameCamera.combined);
        terrain.render(game. shapeRenderer, cameraX);

        // Render obstacles
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.shapeRenderer. setProjectionMatrix(gameCamera.combined);
        obstacles.render(game.batch, game.shapeRenderer, cameraX, WORLD_WIDTH);
        obstacles.renderGoal(game.batch, game.shapeRenderer);


        // Render player
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        player.render(game.batch);
        game.batch.end();

        // Render UI
        stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    private void handleInput() {
        player.handleInput();

        if (Gdx.input. isKeyJustPressed(Input. Keys.G)) {
            toggleGravity();
        }

        if (Gdx. input.isKeyJustPressed(Input. Keys.ESCAPE)) {
            setScreenWithFade(previousScreen);
        }
    }

    private void update(float delta) {
        // Update player
        player. update(delta);

        // Check collisions
        checkObstacleCollisions();
        checkGoalReached();

        // Update camera
        float targetCameraX = player.getX();
        float halfScreen = WORLD_WIDTH / 2f;
        targetCameraX = MathUtils.clamp(targetCameraX, halfScreen, worldWidth - halfScreen);
        cameraX += (targetCameraX - cameraX) * CAMERA_LERP * delta;
    }

    private void checkObstacleCollisions() {
        for (Obstacle obstacle :  obstacles.getObstacles()) {
            if (! obstacle.isCollidable()) continue;

            if (player.getBounds().overlaps(obstacle.bounds)) {
                if (obstacle.isDeadly()) {
                    player.die();
                    ui.showDeath();
                    return;
                }

                handleWallCollision(obstacle);
            }
        }
    }

    private void handleWallCollision(Obstacle obstacle) {
        float playerBottom = player.getBounds().y;
        float playerTop = player.getBounds().y + player.getBounds().height;
        float playerLeft = player.getBounds().x;
        float playerRight = player.getBounds().x + player.getBounds().width;

        float obsLeft = obstacle.bounds. x;
        float obsRight = obstacle. bounds.x + obstacle.bounds.width;
        float obsTop = obstacle.bounds. y + obstacle.bounds.height;
        float obsBottom = obstacle.bounds.y;

        // Landing on top
        if (player.getVelocityY() <= 0 && playerBottom < obsTop && playerBottom > obsTop - 30) {
            player.landOnObstacle(obstacle);
        }
        // Hit from left
        else if (playerRight > obsLeft && playerLeft < obsLeft && playerBottom < obsTop - 10) {
            player.pushLeft(obsLeft);
        }
        // Hit from right
        else if (playerLeft < obsRight && playerRight > obsRight && playerBottom < obsTop - 10) {
            player.pushRight(obsRight);
        }
        // Hit from below
        else if (playerTop > obsBottom && playerBottom < obsBottom && player.getVelocityY() > 0) {
            player.stopUpwardMovement();
        }
    }

    private void checkGoalReached() {
        if (player.getBounds().overlaps(obstacles.getGoalBounds())) {
            reachedGoal = true;
            ui.showVictory();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (player != null) player.dispose();
        if (obstacles != null) obstacles.dispose();
    }


    private void renderStarfield() {
        game.shapeRenderer.setProjectionMatrix(gameCamera.combined);
        game.shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

        // Generate stars based on camera position (consistent starfield)
        int gridSize = 1000;
        int gridX = (int)(gameCamera.position.x / gridSize);
        int gridY = (int)(gameCamera.position.y / gridSize);

        for (int gx = gridX - 2; gx <= gridX + 2; gx++) {
            for (int gy = gridY - 2; gy <= gridY + 2; gy++) {
                renderStarGridCell(gx, gy);
            }
        }

        game.shapeRenderer.end();
    }


    private void renderStarGridCell(int gridX, int gridY) {
        int cellSize = 1000;

        // Use grid coordinates as seed for consistent stars
        java.util.Random random = new java.util.Random(gridX * 73856093L ^ gridY * 19349663L);

        int starCount = 100; // Increased to 100 for more visible stars
        for (int i = 0; i < starCount; i++) {
            float x = gridX * cellSize + random.nextFloat() * cellSize;
            float y = gridY * cellSize + random.nextFloat() * cellSize;
            float size = random.nextFloat() * 3f + 1f; // Larger stars (1-4 pixels)
            float brightness = random.nextFloat() * 0.4f + 0.6f; // Brighter (0.6-1.0)

            // Add color variation - some stars are white, some bluish, some yellowish
            float colorVariation = random.nextFloat();
            float r, g, b;
            if (colorVariation < 0.7f) {
                // White stars (most common)
                r = g = b = brightness;
            } else if (colorVariation < 0.85f) {
                // Bluish stars
                r = brightness * 0.8f;
                g = brightness * 0.9f;
                b = brightness;
            } else {
                // Yellowish stars
                r = brightness;
                g = brightness * 0.95f;
                b = brightness * 0.7f;
            }

            game.shapeRenderer.setColor(r, g, b, 1f);
            game.shapeRenderer.circle(x, y, size);
        }
    }

}

