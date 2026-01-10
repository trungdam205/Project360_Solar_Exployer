package com.solar.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerActor extends Actor {

    private static final float BASE_MOVE_SPEED = 120f;

    private Texture spriteSheet;
    private TextureRegion[][] regions;

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> upAnim;
    private Animation<TextureRegion> sideAnim;

    private TextureRegion idleFrame;
    private float stateTime = 0f;
    private float gravity;

    private enum Direction {
        LEFT, RIGHT, UP, DOWN, IDLE
    }

    private Direction direction = Direction.IDLE;
    private boolean facingRight = false;

    public PlayerActor(float gravity) {
        this.gravity = gravity;

        spriteSheet = new Texture(Gdx.files.internal("images/astronaut.png"));

        int FRAME_WIDTH  = 340; // ✅ CHUẨN
        int FRAME_HEIGHT = 500; // ✅ CHUẨN

        regions = TextureRegion.split(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT);

        idleAnim = new Animation<>(0.2f,
            regions[0][0], regions[0][1], regions[0][2],
            regions[0][3], regions[0][4]);

        upAnim = new Animation<>(0.2f,
            regions[1][0], regions[1][1], regions[1][2],
            regions[1][3], regions[1][4]);

        sideAnim = new Animation<>(0.15f,
            regions[2][0], regions[2][1], regions[2][2],
            regions[2][3], regions[2][4]);

        idleFrame = regions[0][0];

        setSize(FRAME_WIDTH / 3f, FRAME_HEIGHT / 3f); // thu nhỏ hiển thị
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        handleMovement(delta);
    }

    private void handleMovement(float delta) {
        float speed = BASE_MOVE_SPEED / gravity;
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx -= speed * delta;
            direction = Direction.LEFT;
            facingRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx += speed * delta;
            direction = Direction.RIGHT;
            facingRight = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            dy += speed * delta;
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            dy -= speed * delta;
            direction = Direction.DOWN;
        }  else {
            direction = Direction.IDLE;
        }

        moveBy(dx, dy);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame;

        switch (direction) {

            case LEFT:
            case RIGHT:
                frame = sideAnim.getKeyFrame(stateTime, true);

                // sprite gốc là LEFT
                if (facingRight && !frame.isFlipX()) {
                    frame.flip(true, false); // sang phải → flip
                }
                if (!facingRight && frame.isFlipX()) {
                    frame.flip(true, false); // sang trái → bỏ flip
                }
                break;

            case UP:
                frame = upAnim.getKeyFrame(stateTime, true);
                break;

            case DOWN:
            case IDLE:
            default:
                frame = idleAnim.getKeyFrame(stateTime, true);
                break;
        }

        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
