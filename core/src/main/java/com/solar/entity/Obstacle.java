package com.solar. entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com. badlogic.gdx.math.Rectangle;

/**
 * Obstacle entity for planet exploration
 */
public class Obstacle {

    public enum Type {
        WALL, SPIKE, SIGN, DECORATION
    }

    public float x, y, width, height;
    public Type type;
    public Color color;
    public Rectangle bounds;
    public Texture texture;

    public Obstacle() {
        this. bounds = new Rectangle();
        this.color = new Color(0.45f, 0.38f, 0.32f, 1f);
    }

    public Obstacle(float x, float y, float width, float height, Type type) {
        this();
        this.x = x;
        this.y = y;
        this. width = width;
        this.height = height;
        this.type = type;
        updateBounds();
    }

    public void updateBounds() {
        bounds.set(x, y, width, height);
    }

    public boolean isCollidable() {
        return type != Type. DECORATION && type != Type. SIGN;
    }

    public boolean isDeadly() {
        return type == Type. SPIKE;
    }
}
