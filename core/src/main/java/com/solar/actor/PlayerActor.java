package com.solar.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {

    // ===== CONFIG =====
    private static final float BASE_MOVE_SPEED = 160f;
    private static final float BASE_GRAVITY    = 900f;
    private static final float JUMP_FORCE      = 420f;

    // ===== SPRITE =====
    private Texture spriteSheet;
    private TextureRegion[][] regions;

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> jumpAnim;

    private float stateTime = 0f;

    // ===== STATE =====
    private float gravity;
    private float velocityY = 0f;
    private boolean isGrounded = true;
    private boolean facingRight = true;

    private enum Direction {
        LEFT,
        RIGHT,
        JUMP,
        IDLE
    }

    private Direction direction = Direction.IDLE;

    // ===== CONSTRUCTOR =====
    public PlayerActor(float gravity) {
        this.gravity = gravity;

        spriteSheet = new Texture(Gdx.files.internal("images/astronaut.png"));

        int FRAME_WIDTH  = 340;
        int FRAME_HEIGHT = 500;

        regions = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);

        // Row 0: IDLE
        idleAnim = new Animation<>(0.2f,
            regions[0][0], regions[0][1], regions[0][2],
            regions[0][3], regions[0][4]);

        // Row 1: JUMP
        jumpAnim = new Animation<>(0.2f,
            regions[1][0], regions[1][1], regions[1][2],
            regions[1][3], regions[1][4]);

        // Row 2: WALK (LEFT base)
        walkAnim = new Animation<>(0.15f,
            regions[2][0], regions[2][1], regions[2][2],
            regions[2][3], regions[2][4]);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
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
            facingRight = false;

            if (isGrounded)
                direction = Direction.LEFT;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx = BASE_MOVE_SPEED * delta;
            facingRight = true;

            if (isGrounded)
                direction = Direction.RIGHT;
        }
        else {
            if (isGrounded)
                direction = Direction.IDLE;
        }

        moveBy(dx, 0);
    }

    // ===== JUMP + GRAVITY =====
    private void handleJumpAndGravity(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            velocityY = JUMP_FORCE * gravity;
            isGrounded = false;
            direction = Direction.JUMP;
        }

        velocityY -= BASE_GRAVITY * gravity * delta;
        moveBy(0, velocityY * delta);

        if (getY() <= 0) {
            setY(0);
            velocityY = 0;
            isGrounded = true;
            direction = Direction.IDLE;
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

        switch (direction) {

            case LEFT:
                frame = walkAnim.getKeyFrame(stateTime, true);
                applyFlip(frame, false);
                break;

            case RIGHT:
                frame = walkAnim.getKeyFrame(stateTime, true);
                applyFlip(frame, true);
                break;

            case JUMP:
                frame = jumpAnim.getKeyFrame(stateTime, true);
                applyFlip(frame, facingRight);
                break;

            case IDLE:
            default:
                frame = idleAnim.getKeyFrame(stateTime, true);
                applyFlip(frame, facingRight);
                break;
        }

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    // ===== FLIP HELPER =====
    private void applyFlip(TextureRegion frame, boolean faceRight) {
        if (faceRight && !frame.isFlipX())
            frame.flip(true, false);
        if (!faceRight && frame.isFlipX())
            frame.flip(true, false);
    }

    // ===== CLEAN UP =====
    public void dispose() {
        spriteSheet.dispose();
    }
}
