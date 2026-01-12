package com. solar.entity;

import com.badlogic.gdx. graphics.Color;

/**
 * Background mountain for parallax effect
 */
public class Mountain {
    public float x, width, height;
    public Color color;

    public Mountain() {}

    public Mountain(float x, float width, float height, Color color) {
        this.x = x;
        this.width = width;
        this.height = height;
        this.color = color;
    }
}
