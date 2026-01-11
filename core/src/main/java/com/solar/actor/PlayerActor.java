package com.solar.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {

    // ===== CONFIG =====
    private static final float BASE_MOVE_SPEED = 160f;
    private static final float BASE_GRAVITY = 900f;
    private static final float JUMP_FORCE = 420f;

    // ===== SPRITE =====
    private Texture spriteSheet;
    private TextureRegion[][] regions;

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> jumpAnim;

    private float stateTime = 0f;

    // ===== STATE =====
    private float gravity;
    private float VelocityX = 0f;
    private float velocityY = 0f;
    private boolean isGrounded = true;
    private boolean facingRight = false;
    private float groundY;

    private enum State {
        MOVE,
        JUMP,
        IDLE
    }
    private enum Direction {
        LEFT,
        RIGHT,
    }

    private State state = State.IDLE;
    private Direction direction = Direction.RIGHT;

    // ===== CONSTRUCTOR =====
    public PlayerActor(float gravity, float groundY) {
        this.groundY = groundY;
        this.gravity = gravity;

        spriteSheet = new Texture(Gdx.files.internal("images/astronaut.png"));

        int FRAME_WIDTH = 340;
        int FRAME_HEIGHT = 500;

        regions = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);

        // Row 0: IDLE
        idleAnim = new Animation<>(0.2f,
            regions[0][0], regions[0][1], regions[0][2],
            regions[0][3], regions[0][4]);

        // Row 1: JUMP
        jumpAnim = new Animation<>(0.2f,
            regions[0][0], regions[0][1], regions[0][2],
            regions[0][3], regions[0][4]);

        // Row 2: WALK (LEFT base)
        walkAnim = new Animation<>(0.15f,
            regions[2][0], regions[2][1], regions[2][2],
            regions[2][3], regions[2][4]);

        float SCALE = 0.75f;
        setSize(FRAME_WIDTH * SCALE, FRAME_HEIGHT * SCALE);
    }

    // ===== GAME LOOP =====
    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        handleHorizontalMovement(delta);
        handleJumpAndGravity(delta);
        clampToScreen();
    }

    // ===== MOVE LEFT / RIGHT =====
    private void handleHorizontalMovement(float delta) {
        float dx = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx = -BASE_MOVE_SPEED * delta;
            direction = Direction.LEFT;

            if (isGrounded)
                state = State.MOVE;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = BASE_MOVE_SPEED * delta;
            direction = Direction.RIGHT;

            if (isGrounded)
                state = State.MOVE;
        }
        else {
            if (isGrounded)
                state = State.IDLE;
        }

        moveBy(dx, 0);
    }

    // ===== JUMP + GRAVITY =====
    private void handleJumpAndGravity(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            velocityY = JUMP_FORCE * gravity;
            isGrounded = false;
            state = State.JUMP;
        }

        velocityY -= BASE_GRAVITY * gravity * delta;
        moveBy(0, velocityY * delta);

        if (getY() <= groundY) {
            setY(groundY);
            velocityY = 0;
            isGrounded = true;
            state = State.IDLE;
        }
    }

    // ===== CLAMP TO SCREEN =====
    private void clampToScreen() {
        float worldWidth = getStage().getViewport().getWorldWidth();

        if (getX() < 0)
            setX(0);
        if (getX() > worldWidth - getWidth())
            setX(worldWidth - getWidth());
    }

    // ===== DRAW =====
    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;

        switch (state) {
            case MOVE:
                frame = walkAnim.getKeyFrame(stateTime, true);
                break;
            case JUMP:
                frame = jumpAnim.getKeyFrame(stateTime, true);
                break;
            case IDLE:
            default:
                frame = idleAnim.getKeyFrame(stateTime, true);
                break;
        }

        float drawX = getX();
        float drawWidth = getWidth();

        if (direction == Direction.RIGHT) {
            drawX += drawWidth;
            drawWidth = -drawWidth;
        }

        batch.draw(
            frame,
            drawX,
            getY(),
            drawWidth,
            getHeight()
        );
    }

    // ===== CLEAN UP =====
    public void dispose() {
        spriteSheet.dispose();
    }
}
