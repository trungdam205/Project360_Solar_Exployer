package com.solar.planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.solar.render.PlayerRenderer;
import com.solar.config.GameConfig;

/**
 * Player controller for planet exploration
 */
public class PlanetPlayer {

    // Use constants from GameConfig
    public static final float WIDTH = GameConfig.PLAYER_WIDTH;
    public static final float HEIGHT = GameConfig.PLAYER_HEIGHT;
    private static final float WALK_SPEED = GameConfig.WALK_SPEED;
    private static final float RUN_SPEED = GameConfig.RUN_SPEED;

    // Position & Movement
    private float x, y;
    private float velocityY;
    private boolean isJumping;
    private boolean movingLeft, movingRight;
    private boolean isRunning;
    private boolean isDead;

    // Collision
    private final Rectangle bounds;
    private Obstacle standingOnObstacle;

    // Renderer
    private final PlayerRenderer renderer;

    // World reference
    private float groundY;
    private float worldWidth;

    // Physics reference
    private PlanetPhysics physics;

    public PlanetPlayer() {
        this.bounds = new Rectangle();
        this.renderer = new PlayerRenderer();
        this.renderer.setSize(WIDTH, HEIGHT);
    }

    public void init(float startX, float groundY, float worldWidth, PlanetPhysics physics) {
        this.x = startX;
        this.y = groundY;
        this.groundY = groundY;
        this.worldWidth = worldWidth;
        this.physics = physics;
        reset();
    }

    public void reset() {
        x = 150f;
        y = groundY;
        velocityY = 0;
        isJumping = false;
        isDead = false;
        movingLeft = false;
        movingRight = false;
        isRunning = false;
        standingOnObstacle = null;
    }

    public void handleInput() {
        if (isDead) return;

        movingLeft = Gdx.input. isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        movingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input. isKeyPressed(Input.Keys.D);
        isRunning = Gdx.input.isKeyPressed(Input.Keys. SHIFT_LEFT) || Gdx.input.isKeyPressed(Input. Keys.SHIFT_RIGHT);

        if (Gdx.input. isKeyJustPressed(Input. Keys. SPACE) && !isJumping) {
            jump();
        }
    }

    public void jump() {
        isJumping = true;
        standingOnObstacle = null;
        velocityY = physics.getJumpVelocity();
    }

    public void update(float delta) {
        if (isDead) return;

        // Horizontal movement
        float currentSpeed = isRunning ?  RUN_SPEED :  WALK_SPEED;

        if (movingLeft) {
            x -= currentSpeed * delta;
        } else if (movingRight) {
            x += currentSpeed * delta;
        }

        x = MathUtils. clamp(x, WIDTH / 2, worldWidth - WIDTH / 2);

        // Check if still on obstacle
        float currentGroundY = groundY;
        if (standingOnObstacle != null) {
            float playerLeft = x - WIDTH / 2 + 15;
            float playerRight = x + WIDTH / 2 - 15;
            float obsLeft = standingOnObstacle.bounds.x;
            float obsRight = standingOnObstacle.bounds. x + standingOnObstacle.bounds.width;

            if (playerRight < obsLeft || playerLeft > obsRight) {
                standingOnObstacle = null;
                isJumping = true;
                velocityY = 0;
            } else {
                currentGroundY = standingOnObstacle.bounds.y + standingOnObstacle.bounds.height;
            }
        }

        // Apply gravity using physics
        if (isJumping || y > currentGroundY) {
            velocityY -= physics.getGravityDelta(delta);
            y += velocityY * delta;

            if (y <= groundY) {
                y = groundY;
                velocityY = 0;
                isJumping = false;
                standingOnObstacle = null;
            }
        }

        // Update bounds
        updateBounds();

        // Update animation
        renderer.update(delta, movingLeft, movingRight, isRunning);
    }

    private void updateBounds() {
        bounds. set(x - WIDTH / 2 + 15, y, WIDTH - 30, HEIGHT - 10);
    }

    public void render(SpriteBatch batch) {
        if (! isDead) {
            renderer.render(batch, x, y, WIDTH, HEIGHT);
        }
    }

    public void landOnObstacle(Obstacle obstacle) {
        y = obstacle.bounds. y + obstacle.bounds.height;
        velocityY = 0;
        isJumping = false;
        standingOnObstacle = obstacle;
    }

    public void pushLeft(float obstacleLeft) {
        x = obstacleLeft - WIDTH / 2 - 1;
    }

    public void pushRight(float obstacleRight) {
        x = obstacleRight + WIDTH / 2 + 1;
    }

    public void stopUpwardMovement() {
        velocityY = 0;
    }

    public void die() {
        isDead = true;
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVelocityY() { return velocityY; }
    public boolean isJumping() { return isJumping; }
    public boolean isDead() { return isDead; }
    public Rectangle getBounds() { return bounds; }

    public void dispose() {
        if (renderer != null) {
            renderer.dispose();
        }
    }
}
