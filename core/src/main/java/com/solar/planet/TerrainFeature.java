package com.solar.planet;

import com.badlogic.gdx.graphics.Color;

/**
 * Surface terrain feature (rocks, bumps, etc.)
 */
public class TerrainFeature {
    public float x, width, height;
    public Color color;

    public TerrainFeature() {}

    public TerrainFeature(float x, float width, float height, Color color) {
        this.x = x;
        this.width = width;
        this.height = height;
        this.color = color;
    }
}
