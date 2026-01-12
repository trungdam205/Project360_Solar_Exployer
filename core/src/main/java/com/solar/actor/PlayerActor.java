package com.solar.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class PlayerActor extends Actor {

    // ===== SPRITE CONFIG =====
    private static final int FRAME_COLS = 5;
    private static final int FRAME_ROWS = 3;
    private float velocityX = 0f;
    private boolean onGround;

    // ===== EARTH REFERENCE =====
    private static final float EARTH_GRAVITY = 9.81f;

    // ===== BASE VALUES (EARTH) =====
    private static final float SCALE_BASE = 1.0f;
    private static final float BASE_MOVE_SPEED = 200f * SCALE_BASE;
    private static final float BASE_JUMP_FORCE = 450f * SCALE_BASE;
    private static final float BASE_FALL_SPEED = 900f * SCALE_BASE;

    // ===== PHYSICS =====
    private float gravity;
    private float groundLineY;

    private float velocityY;
    private boolean isJumping;

    private float moveSpeed;
    private float jumpForce;
    private float fallSpeed;

    // ===== GRAPHICS =====
    private Texture texture;
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> moveAnim;
    private Animation<TextureRegion> currentAnim;

    private float stateTime;
    private boolean facingRight;

    // ===== CONSTRUCTOR =====
    public PlayerActor(float planetGravity, float groundLineY) {
        this.gravity = planetGravity;
        this.groundLineY = groundLineY;

        float normalizedGravity = planetGravity / EARTH_GRAVITY;

        // === CORE UPGRADE ===
        this.moveSpeed = BASE_MOVE_SPEED / normalizedGravity;
        this.jumpForce = BASE_JUMP_FORCE / normalizedGravity;
        this.fallSpeed = BASE_FALL_SPEED * normalizedGravity;

        loadAnimations();

        int frameWidth = texture.getWidth() / FRAME_COLS;
        int frameHeight = texture.getHeight() / FRAME_ROWS;
        setSize(frameWidth * 0.7f, frameHeight * 0.7f);

        setY(groundLineY);
    }

    // ===== LOAD ANIMATION =====
    private void loadAnimations() {
        texture = new Texture(Gdx.files.internal("images/astronaut.png"));

        TextureRegion[][] tmp = TextureRegion.split(
            texture,
            texture.getWidth() / FRAME_COLS,
            texture.getHeight() / FRAME_ROWS
        );

        Array<TextureRegion> idleFrames = new Array<>();
        for (int i = 0; i < FRAME_COLS; i++) {
            idleFrames.add(tmp[0][i]);
        }
        idleAnim = new Animation<>(0.15f, idleFrames, Animation.PlayMode.LOOP);

        Array<TextureRegion> moveFrames = new Array<>();
        for (int i = 0; i < FRAME_COLS; i++) {
            moveFrames.add(tmp[2][i]);
        }
        moveAnim = new Animation<>(0.1f, moveFrames, Animation.PlayMode.LOOP);

        currentAnim = idleAnim;
        facingRight = true;
    }

    // ===== UPDATE =====
    @Override
    public void act(float delta) {
        super.act(delta);
        onGround = false;
        handleInput(delta);
        applyGravity(delta);
        stateTime += delta;
    }

    // ===== INPUT =====
    private void handleInput(float delta) {
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean jump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

        float moveX = 0;

        if (left) {
            moveX = -moveSpeed * delta;
            facingRight = false;
            currentAnim = moveAnim;
        } else if (right) {
            moveX = moveSpeed * delta;
            facingRight = true;
            currentAnim = moveAnim;
        } else {
            currentAnim = idleAnim;
        }

        velocityX = moveX / delta; // chỉ lưu vận tốc

        if (jump && !isJumping) {
            velocityY = jumpForce;
            isJumping = true;
        }
    }

    // ===== GRAVITY =====
    private void applyGravity(float delta) {
        if (isJumping) {
            velocityY -= fallSpeed * delta;
            moveBy(0, velocityY * delta);

            if (getY() <= groundLineY) {
                setY(groundLineY);
                velocityY = 0;
                isJumping = false;
            }
        }
    }

    // ===== DRAW =====
    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = currentAnim.getKeyFrame(stateTime);

        if (facingRight && !frame.isFlipX()) frame.flip(true, false);
        if (!facingRight && frame.isFlipX()) frame.flip(true, false);

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    public void setGravity(float newGravity) {
        this.gravity = newGravity;

        float normalizedGravity = newGravity / EARTH_GRAVITY;

        this.moveSpeed = BASE_MOVE_SPEED / normalizedGravity;
        this.jumpForce = BASE_JUMP_FORCE / normalizedGravity;
        this.fallSpeed = BASE_FALL_SPEED * normalizedGravity;

        // reset trạng thái để tránh bug
        velocityY = 0;
        isJumping = false;
        setY(groundLineY);
    }

    public void resetState() {
        velocityY = 0;
        isJumping = false;
        setY(groundLineY);
    }

    public Rectangle getRect(float nextX, float nextY) {
        return new Rectangle(
            nextX,
            nextY,
            getWidth(),
            getHeight()
        );
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setOnGround(boolean value) {
        onGround = value;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void dispose() {
        texture.dispose();
    }
}
