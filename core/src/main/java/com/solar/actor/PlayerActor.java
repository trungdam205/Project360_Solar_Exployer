package com.solar.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {

    // ===== BASE CONFIG =====
    private static final float BASE_MOVE_SPEED = 160f;
    private static final float BASE_JUMP_SPEED = 420f;
    private static final float BASE_FALL_SPEED = 900f;

    // ===== SPRITE =====
    private Texture spriteSheet;
    private TextureRegion[][] regions;

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> moveLeftAnim;
    private Animation<TextureRegion> moveRightAnim;
    private Animation<TextureRegion> jumpLeftAnim;
    private Animation<TextureRegion> jumpRightAnim;

    private float stateTime = 0f;

    // ===== STATE =====
    private final float gravity;
    private final float groundY;

    private boolean isGrounded = true;

    private enum State { IDLE, MOVE, JUMP }
    private enum Direction { LEFT, RIGHT }

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

        idleAnim = new Animation<>(0.2f,
            regions[0][0], regions[0][1], regions[0][2],
            regions[0][3], regions[0][4]);

        moveLeftAnim = new Animation<>(0.15f,
            regions[2][0], regions[2][1], regions[2][2],
            regions[2][3], regions[2][4]);

        moveRightAnim = new Animation<>(0.15f,
            copyAndFlip(regions[2][0]),
            copyAndFlip(regions[2][1]),
            copyAndFlip(regions[2][2]),
            copyAndFlip(regions[2][3]),
            copyAndFlip(regions[2][4])
        );

        jumpLeftAnim  = moveLeftAnim;
        jumpRightAnim = moveRightAnim;

        setSize(FRAME_WIDTH * 0.75f, FRAME_HEIGHT * 0.75f);
    }

    // ===== GAME LOOP =====
    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;

        handleHorizontalMove(delta);
        handleJumpAndFall(delta);
    }

    // ===== HORIZONTAL MOVE (NO ACCEL) =====
    private void handleHorizontalMove(float delta) {
        float speed = BASE_MOVE_SPEED / gravity;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveBy(-speed * delta, 0);
            direction = Direction.LEFT;
            if (isGrounded) state = State.MOVE;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveBy(speed * delta, 0);
            direction = Direction.RIGHT;
            if (isGrounded) state = State.MOVE;
        }
        else if (isGrounded) {
            state = State.IDLE;
        }
    }

    // ===== JUMP + FALL (SIMPLE) =====
    private float jumpVelocity = 0f;

    private void handleJumpAndFall(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            jumpVelocity = BASE_JUMP_SPEED / gravity;
            isGrounded = false;
            state = State.JUMP;
        }

        if (!isGrounded) {
            jumpVelocity -= BASE_FALL_SPEED * gravity * delta;
            moveBy(0, jumpVelocity * delta);
        }

        if (getY() <= groundY) {
            setY(groundY);
            jumpVelocity = 0;
            isGrounded = true;
            state = State.IDLE;
        }
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
                    ? jumpRightAnim.getKeyFrame(stateTime, true)
                    : jumpLeftAnim.getKeyFrame(stateTime, true);
                break;

            default:
                frame = idleAnim.getKeyFrame(stateTime, true);
        }

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    // ===== CLEAN =====
    public void dispose() {
        spriteSheet.dispose();
    }

    private TextureRegion copyAndFlip(TextureRegion src) {
        TextureRegion copy = new TextureRegion(src);
        copy.flip(true, false);
        return copy;
    }
}
