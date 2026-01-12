package com.solar. solarsystem;

import com.badlogic.gdx. math.MathUtils;

/**
 * State management for Solar System screen
 */
public class SolarSystemState {

    // Animation state
    private boolean paused = false;
    private float speed = 1f;
    private float time = 0;

    // View state
    private float zoom = 1f;
    private float offsetX = 0;
    private float offsetY = 0;
    private boolean showOrbits = true;

    // Dragging state
    private boolean dragging = false;
    private float lastMouseX, lastMouseY;

    // Constants
    private static final float MIN_ZOOM = 0.2f;
    private static final float MAX_ZOOM = 2f;

    public SolarSystemState() {
        reset();
    }

    /**
     * Reset all state to default values
     */
    public void reset() {
        offsetX = 0;
        offsetY = 0;
        zoom = 1f;
        time = 0;
        speed = 1f;
        paused = false;
    }

    /**
     * Update time if not paused
     */
    public void update(float delta) {
        if (!paused) {
            time += speed * delta * 60;
        }
    }

    // ==================== Getters & Setters ====================

    public boolean isPaused() { return paused; }
    public void setPaused(boolean paused) { this.paused = paused; }
    public void togglePause() { this.paused = ! this.paused; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public float getTime() { return time; }

    public float getZoom() { return zoom; }
    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MIN_ZOOM, MAX_ZOOM);
    }
    public void adjustZoom(float delta) {
        setZoom(zoom - delta * 0.1f);
    }

    public float getOffsetX() { return offsetX; }
    public float getOffsetY() { return offsetY; }
    public void setOffset(float x, float y) {
        this.offsetX = x;
        this.offsetY = y;
    }
    public void adjustOffset(float dx, float dy) {
        this.offsetX += dx;
        this. offsetY += dy;
    }

    public boolean isShowOrbits() { return showOrbits; }
    public void setShowOrbits(boolean show) { this.showOrbits = show; }
    public void toggleOrbits() { this.showOrbits = !this.showOrbits; }

    public boolean isDragging() { return dragging; }
    public void setDragging(boolean dragging) { this.dragging = dragging; }

    public float getLastMouseX() { return lastMouseX; }
    public float getLastMouseY() { return lastMouseY; }
    public void setLastMouse(float x, float y) {
        this.lastMouseX = x;
        this.lastMouseY = y;
    }
}
