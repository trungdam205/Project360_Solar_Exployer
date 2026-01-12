package com.solar.render;

import com. badlogic.gdx.Gdx;
import com. badlogic.gdx.graphics.g2d.Animation;
import com. badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx. graphics.g2d.TextureAtlas;
import com. badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com. badlogic.gdx.utils. Disposable;

/**
 * Renderer for player astronaut with animations
 */
public class PlayerRenderer implements Disposable {

    private TextureAtlas atlas;

    // Animations
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private Animation<TextureRegion> runLeftAnimation;
    private Animation<TextureRegion> runRightAnimation;

    // Current state
    private float stateTime = 0;
    private PlayerState currentState = PlayerState.IDLE;
    private boolean facingRight = true;

    // Player dimensions
    private float width = 120f;
    private float height = 120f;

    public enum PlayerState {
        IDLE,
        WALKING,
        RUNNING,
        JUMPING
    }

    public PlayerRenderer() {
        loadAnimations();
    }

    private void loadAnimations() {
        try {
            atlas = new TextureAtlas(Gdx.files.internal("images/player.atlas"));

            // Idle animation
            TextureRegion idleFrame = atlas.findRegion("idle");
            if (idleFrame != null) {
                Array<TextureRegion> idleFrames = new Array<>();
                idleFrames.add(idleFrame);
                idleAnimation = new Animation<>(0.5f, idleFrames, Animation.PlayMode.LOOP);
            }

            // Walk Left animation
            Array<TextureRegion> walkLeftFrames = new Array<>();
            addRegionIfExists(walkLeftFrames, "walkleft1");
            addRegionIfExists(walkLeftFrames, "walkleft2");
            if (walkLeftFrames.size > 0) {
                walkLeftAnimation = new Animation<>(0.15f, walkLeftFrames, Animation.PlayMode.LOOP);
            }

            // Walk Right animation
            Array<TextureRegion> walkRightFrames = new Array<>();
            addRegionIfExists(walkRightFrames, "walkright1");
            addRegionIfExists(walkRightFrames, "walkright2");
            if (walkRightFrames. size > 0) {
                walkRightAnimation = new Animation<>(0.15f, walkRightFrames, Animation. PlayMode.LOOP);
            }

            // Run Left animation
            Array<TextureRegion> runLeftFrames = new Array<>();
            addRegionIfExists(runLeftFrames, "runleft1");
            addRegionIfExists(runLeftFrames, "runleft2");
            addRegionIfExists(runLeftFrames, "runleft3");
            addRegionIfExists(runLeftFrames, "runleft4");
            if (runLeftFrames. size > 0) {
                runLeftAnimation = new Animation<>(0.1f, runLeftFrames, Animation.PlayMode.LOOP);
            }

            // Run Right animation
            Array<TextureRegion> runRightFrames = new Array<>();
            addRegionIfExists(runRightFrames, "runright1");
            addRegionIfExists(runRightFrames, "runright2");
            addRegionIfExists(runRightFrames, "runright3");
            addRegionIfExists(runRightFrames, "runright4");
            addRegionIfExists(runRightFrames, "runright5");
            if (runRightFrames.size > 0) {
                runRightAnimation = new Animation<>(0.1f, runRightFrames, Animation.PlayMode. LOOP);
            }

            Gdx.app. log("PlayerRenderer", "Player animations loaded successfully");

        } catch (Exception e) {
            Gdx.app.error("PlayerRenderer", "Failed to load player atlas:  " + e.getMessage());
        }
    }

    private void addRegionIfExists(Array<TextureRegion> frames, String regionName) {
        TextureRegion region = atlas.findRegion(regionName);
        if (region != null) {
            frames.add(region);
        }
    }

    public void update(float delta, boolean movingLeft, boolean movingRight, boolean isRunning) {
        stateTime += delta;

        // Update facing direction
        if (movingLeft) {
            facingRight = false;
        } else if (movingRight) {
            facingRight = true;
        }

        // Update state
        if (movingLeft || movingRight) {
            currentState = isRunning ? PlayerState.RUNNING : PlayerState.WALKING;
        } else {
            currentState = PlayerState.IDLE;
        }
    }

    public void render(SpriteBatch batch, float x, float y) {
        render(batch, x, y, width, height);
    }

    public void render(SpriteBatch batch, float x, float y, float customWidth, float customHeight) {
        TextureRegion currentFrame = getCurrentFrame();

        if (currentFrame != null) {
            float drawX = x - customWidth / 2;
            float drawY = y;
            batch.draw(currentFrame, drawX, drawY, customWidth, customHeight);
        }
    }

    private TextureRegion getCurrentFrame() {
        Animation<TextureRegion> animation = null;

        switch (currentState) {
            case IDLE:
                animation = idleAnimation;
                break;
            case WALKING:
                animation = facingRight ? walkRightAnimation : walkLeftAnimation;
                break;
            case RUNNING:
                animation = facingRight ?  runRightAnimation :  runLeftAnimation;
                break;
            case JUMPING:
                animation = idleAnimation;
                break;
        }

        if (animation != null) {
            return animation.getKeyFrame(stateTime);
        }

        if (idleAnimation != null) {
            return idleAnimation.getKeyFrame(stateTime);
        }

        return null;
    }

    public void setState(PlayerState state) {
        if (currentState != state) {
            currentState = state;
            stateTime = 0;
        }
    }

    public PlayerState getState() {
        return currentState;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this. height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }
}
