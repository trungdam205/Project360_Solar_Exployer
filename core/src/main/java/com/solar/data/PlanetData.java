package com.solar.data;

// Stores all data for a planet
public class PlanetData {
    // Planet type (enum)
    public PlanetType type;
    // Display name for UI
    public String displayName;
    // Texture region name in atlas
    public String texturePath;
    public float size; // Size of the planet (pixels)
    public boolean canEnter;     // Can player enter this planet?

    // Temporary position for Moon
    public float positionX;
    // X position in solar system
    public float x;
    // Y position in solar system
    public float y;

    // Physics and info
    public double gravity;       // Gravity value
    public String weather;       // Weather description
    public String atmosphere;    // Atmosphere type
    public String surfaceType;   // Surface type
    public String primaryRes;    // Main resource

    // Constructor: initializes all planet data
    public PlanetData(PlanetType type, String displayName, String texturePath, float size, boolean canEnter, float x, float y,
                      double gravity, String weather, String atmosphere, String surfaceType, String primaryRes) {
        this.type = type;
        this.displayName = displayName;
        this.texturePath = texturePath;
        this.size = size;
        this.canEnter = canEnter;
        this.positionX = 0;
        this.x = x;
        this.y = y;
        this.gravity = gravity;
        this.weather = weather;
        this.atmosphere = atmosphere;
        this.surfaceType = surfaceType;
        this.primaryRes = primaryRes;
    }
}
