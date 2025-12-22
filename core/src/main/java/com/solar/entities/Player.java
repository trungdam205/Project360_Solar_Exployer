package com.solar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {

    // ================= BOX2D =================
    private Body body;
    private static final float PPM = 100f;

    // ================= MOVEMENT =================
    private final float speed = 2.5f;
    private boolean facingRight = true;

    // ================= ANIMATION =================
    private Texture spriteSheet;
    private Animation<TextureRegion> runAnimation;
    private TextureRegion currentFrame;
    private float stateTime;

    // ================= CONSTANTS =================
    private boolean hihi;
    private boolean onGround = false;
    private static final int FRAME_COLS = 5;
    private static final int FRAME_ROWS = 1;
    private static final float FRAME_DURATION = 0.1f;

    public Player(World world) {

        // ===== LOAD SPRITE =====
        spriteSheet = new Texture("player/astronaut.png");

        TextureRegion[][] tmp = TextureRegion.split(
            spriteSheet,
            spriteSheet.getWidth() / FRAME_COLS,
            spriteSheet.getHeight() / FRAME_ROWS
        );

        TextureRegion[] runFrames = new TextureRegion[FRAME_COLS];
        for (int i = 0; i < FRAME_COLS; i++) {
            runFrames[i] = tmp[0][i];
        }

        runAnimation = new Animation<>(FRAME_DURATION, runFrames);
        stateTime = 0f;

        // ===== CREATE BODY =====
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(100 / PPM, 100 / PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            (spriteSheet.getWidth() / FRAME_COLS / 2f) / PPM,
            (spriteSheet.getHeight() / 2f) / PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.2f;

        body.createFixture(fixtureDef);
        body.setUserData("player");
        shape.dispose();

        // ===== FOOT SENSOR =====
        PolygonShape footShape = new PolygonShape();
        footShape.setAsBox(
            (spriteSheet.getWidth() / FRAME_COLS / 2f) / PPM,
            4f / PPM,
            new Vector2(0, -spriteSheet.getHeight() / 2f / PPM),
            0
        );

        FixtureDef footDef = new FixtureDef();
        footDef.shape = footShape;
        footDef.isSensor = true;

        Fixture footFixture = body.createFixture(footDef);
        footFixture.setUserData("foot");
        body.setUserData(this);
        footShape.dispose();
    }

    public void update(float delta) {

        stateTime += delta;
        Vector2 velocity = body.getLinearVelocity();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.setLinearVelocity(-speed, velocity.y);
            facingRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.setLinearVelocity(speed, velocity.y);
            facingRight = true;
        } else {
            body.setLinearVelocity(0, velocity.y);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump();
        }
    }

    public void render(SpriteBatch batch) {
        currentFrame = runAnimation.getKeyFrame(stateTime, true);

        if (facingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        if (!facingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }

        batch.draw(
            currentFrame,
            body.getPosition().x * PPM - currentFrame.getRegionWidth() / 2f,
            body.getPosition().y * PPM - currentFrame.getRegionHeight() / 2f,
            currentFrame.getRegionWidth() / 2f,     // originX
            currentFrame.getRegionHeight() / 2f,    // originY
            currentFrame.getRegionWidth(),          // width
            currentFrame.getRegionHeight(),         // height
            1f,                                     // scaleX
            1f,                                     // scaleY
            (float) Math.toDegrees(body.getAngle()) // rotation (Box2D → degrees)
        );
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public void setOnGround(boolean value) {
        onGround = value;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void jump() {
        if (onGround) {
            body.applyLinearImpulse(
                new Vector2(0, 4f),
                body.getWorldCenter(),
                true
            );
            onGround = false; // tránh double jump
        }
    }
}
