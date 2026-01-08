package com.solar;

import java.util.HashMap;

public class PlanetData {
    public String id;
    public String name;
    public float x;
    public float y;
    public float gravity;
    public float radius;
    public String texturePath;
    public String description;

    // Drop rate map (Key: Resource ID, Value: Probability 0.0 - 1.0)
    // Example: "R1" -> 0.65 (65% chance)
    public HashMap<String, Float> dropRates;

    public PlanetData() {
        // Initialize the map to avoid NullPointerException
        dropRates = new HashMap<>();
    }

    // Utility method to safely get drop rate
    public float getDropRate(String resourceId) {
        return dropRates.getOrDefault(resourceId, 0f);
    }
}
