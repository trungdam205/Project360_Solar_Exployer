package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic. gdx.graphics. OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.solar.MainGame;
import com.solar.data.CelestialData;
import com.solar.entity. Obstacle;
import com.solar.planet.*;

/**
 * Planet exploration screen - refactored to use modular components
 */
public class PlanetScreen extends BaseScreen {

    // Constants
    private static final float WORLD_EXTENT = 5000f;
    private static final float TERRAIN_HEIGHT_RATIO = 0.28f;
    private static final float CAMERA_LERP = 5f;

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
        worldWidth = WORLD_EXTENT;
        groundY = WORLD_HEIGHT * TERRAIN_HEIGHT_RATIO;
        cameraX = WORLD_WIDTH / 2f;

        // Initialize physics first (other components depend on it)
        physics = new PlanetPhysics(planet);
        Gdx.app. log("PlanetScreen", "=== INITIALIZING ===");
        Gdx.app. log("PlanetScreen", "Planet: " + planet. name);
        Gdx.app.log("PlanetScreen", "Exploration: " + (planet.exploration != null ? "OK" : "NULL"));

        // ...  rest of initialization

        physics = new PlanetPhysics(planet);
        Gdx.app. log("PlanetScreen", "Physics created: " + physics. getPlanetType());
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

        // Clear screen
        Gdx.gl.glClearColor(
                planet.exploration.skyColorTop. r,
                planet.exploration.skyColorTop. g,
                planet.exploration.skyColorTop.b,
                1
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx. gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Update camera
        gameCamera.position.x = cameraX;
        gameCamera. position.y = WORLD_HEIGHT / 2f;
        gameCamera.update();

        // Render terrain
        game.shapeRenderer.setProjectionMatrix(gameCamera.combined);
        terrain.render(game. shapeRenderer, cameraX);

        // Render obstacles
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.shapeRenderer. setProjectionMatrix(gameCamera.combined);
        obstacles.render(game.batch, game.shapeRenderer, cameraX, WORLD_WIDTH);
        obstacles.renderGoal(game.shapeRenderer);

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
}
