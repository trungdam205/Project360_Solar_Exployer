package com.solar.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {

    // ===== CONFIG =====
    private static final float BASE_MOVE_SPEED = 120f;
    private static final float BASE_GRAVITY = 900f;
    private static final float JUMP_FORCE = 420f;

    // ===== SPRITE =====
    private Texture spriteSheet;
    private TextureRegion[][] regions;

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> moveLeftAnim;
    private Animation<TextureRegion> moveRightAnim;
    private Animation<TextureRegion> idleJumpAnim;
    private Animation<TextureRegion> leftJumpAnim;
    private Animation<TextureRegion> rightJumpAnim;

    private float stateTime = 0f;

    // ===== STATE =====
    private float gravity;
    private float velocityX = 0f;
    private float velocityY = 0f;
    private boolean isGrounded = true;
    private float groundY;

    private enum State {
        IDLE,
        MOVE,
        JUMP
    }

    private enum Direction {
        LEFT,
        RIGHT
    }

    private State state = State.IDLE;
    private Direction direction = Direction.RIGHT;

    // ===== CONSTRUCTOR =====
    public PlayerActor(float gravity, float groundY) {
        this.gravity = gravity;
        this.groundY = groundY;

        spriteSheet = new Texture(Gdx.files.internal("images/astronaut.png"));

        int FRAME_WIDTH = 340;
        int FRAME_HEIGHT = 500;

        regions = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);

        // IDLE
        idleAnim = new Animation<>(0.2f,
            regions[0][0], regions[0][1], regions[0][2],
            regions[0][3], regions[0][4]);

        // MOVE LEFT (base)
        moveLeftAnim = new Animation<>(0.15f,
            regions[2][0], regions[2][1], regions[2][2],
            regions[2][3], regions[2][4]);

        // MOVE RIGHT (flipped copy)
        moveRightAnim = new Animation<>(0.15f,
            copyAndFlip(regions[2][0]),
            copyAndFlip(regions[2][1]),
            copyAndFlip(regions[2][2]),
            copyAndFlip(regions[2][3]),
            copyAndFlip(regions[2][4])
        );

        // JUMP (reuse for now)
        idleJumpAnim = idleAnim;
        leftJumpAnim = moveLeftAnim;
        rightJumpAnim = moveRightAnim;

        float SCALE = 0.75f;
        setSize(FRAME_WIDTH * SCALE, FRAME_HEIGHT * SCALE);
    }

    // ===== GAME LOOP =====
    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        handleInput(delta);
        applyGravity(delta);
        moveBy(velocityX * delta, velocityY * delta);
        resolveGround();
        clampToScreen();
    }

    // ===== INPUT =====
    private void handleInput(float delta) {
        float speed = BASE_MOVE_SPEED / gravity; // gravity ảnh hưởng tốc độ

        velocityX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX = -speed;
            direction = Direction.LEFT;
            if (isGrounded) state = State.MOVE;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX = speed;
            direction = Direction.RIGHT;
            if (isGrounded) state = State.MOVE;
        }
        else {
            if (isGrounded) state = State.IDLE;
        }

        // Jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            velocityY = JUMP_FORCE / gravity;
            isGrounded = false;
            state = State.JUMP;
        }
    }

    // ===== GRAVITY =====
    private void applyGravity(float delta) {
        if (!isGrounded) {
            velocityY -= BASE_GRAVITY * gravity * delta;
        }
    }

    // ===== GROUND COLLISION =====
    private void resolveGround() {
        if (getY() <= groundY) {
            setY(groundY);
            velocityY = 0;
            isGrounded = true;

            if (velocityX == 0) state = State.IDLE;
            else state = State.MOVE;
        }
    }

    // ===== CLAMP =====
    private void clampToScreen() {
        float worldWidth = getStage().getViewport().getWorldWidth();

        if (getX() < 0) setX(0);
        if (getX() > worldWidth - getWidth())
            setX(worldWidth - getWidth());
    }

    // ===== DRAW =====
    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;

        switch (state) {
            case MOVE:
                frame = (direction == Direction.RIGHT)
                    ? moveRightAnim.getKeyFrame(stateTime, true)
                    : moveLeftAnim.getKeyFrame(stateTime, true);
                break;

            case JUMP:
                frame = (direction == Direction.RIGHT)
                    ? rightJumpAnim.getKeyFrame(stateTime, true)
                    : leftJumpAnim.getKeyFrame(stateTime, true);
                break;

            case IDLE:
            default:
                frame = idleAnim.getKeyFrame(stateTime, true);
                break;
        }

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    // ===== CLEAN UP =====
    public void dispose() {
        spriteSheet.dispose();
    }

    // ===== HELPER =====
    private TextureRegion copyAndFlip(TextureRegion src) {
        TextureRegion copy = new TextureRegion(src);
        copy.flip(true, false);
        return copy;
    }
}
