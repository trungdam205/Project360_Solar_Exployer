package com.solar. planet;

import com.solar.data.CelestialData;

/**
 * Physics and gravity calculations for planet exploration
 */
public class PlanetPhysics {

    // Constants
    public static final float EARTH_GRAVITY = 1.0f;
    public static final float GRAVITY_FORCE = 2000f;
    public static final float BASE_JUMP_VELOCITY = 750f;

    // State
    private final CelestialData planet;
    private final PlanetType planetType;
    private boolean planetGravityEnabled;
    private float currentGravity;

    public enum PlanetType {
        LOW_GRAVITY,    // Mercury, Venus, Mars, Moon, Pluto, Uranus
        HIGH_GRAVITY,   // Jupiter, Saturn, Neptune
        NORMAL_GRAVITY  // Earth
    }

    public PlanetPhysics(CelestialData planet) {
        this. planet = planet;
        this.planetType = determinePlanetType(planet);
        this.planetGravityEnabled = false;
        this. currentGravity = EARTH_GRAVITY;
    }

    /**
     * Determine planet type based on gravity
     */
    private static PlanetType determinePlanetType(CelestialData planet) {
        if (planet == null || planet.id == null) {
            return PlanetType.NORMAL_GRAVITY;
        }

        if (planet.id. equals("earth")) {
            return PlanetType. NORMAL_GRAVITY;
        } else if (planet.id.equals("neptune") ||
            planet. id.equals("jupiter") ||
            planet.id.equals("saturn")) {
            return PlanetType.HIGH_GRAVITY;
        } else {
            return PlanetType.LOW_GRAVITY;
        }
    }

    /**
     * Toggle between Earth gravity and planet gravity
     */
    public void toggleGravity() {
        planetGravityEnabled = !planetGravityEnabled;

        if (planetGravityEnabled && planet != null && planet.exploration != null) {
            currentGravity = planet.exploration.gravity;
        } else {
            currentGravity = EARTH_GRAVITY;
        }
    }

    /**
     * Reset to Earth gravity
     */
    public void reset() {
        planetGravityEnabled = false;
        currentGravity = EARTH_GRAVITY;
    }

    /**
     * Calculate jump velocity for current gravity
     */
    public float getJumpVelocity() {
        if (currentGravity <= 0) currentGravity = EARTH_GRAVITY;
        return BASE_JUMP_VELOCITY / (float) Math.sqrt(currentGravity);
    }

    /**
     * Calculate gravity force for delta time
     */
    public float getGravityDelta(float delta) {
        return GRAVITY_FORCE * currentGravity * delta;
    }

    /**
     * Calculate max jump height for given gravity
     */
    public static float calculateMaxJumpHeight(float gravity) {
        if (gravity <= 0) gravity = EARTH_GRAVITY;
        float jumpVelocity = BASE_JUMP_VELOCITY / (float) Math.sqrt(gravity);
        return (jumpVelocity * jumpVelocity) / (2 * gravity * GRAVITY_FORCE);
    }

    /**
     * Get max jump height with Earth gravity
     */
    public float getEarthJumpHeight() {
        return calculateMaxJumpHeight(EARTH_GRAVITY);
    }

    /**
     * Get max jump height with planet gravity
     */
    public float getPlanetJumpHeight() {
        if (planet == null || planet.exploration == null) {
            return calculateMaxJumpHeight(EARTH_GRAVITY);
        }
        return calculateMaxJumpHeight(planet. exploration.gravity);
    }

    // Getters
    public float getCurrentGravity() { return currentGravity; }

    public float getPlanetGravity() {
        if (planet == null || planet.exploration == null) {
            return EARTH_GRAVITY;
        }
        return planet.exploration.gravity;
    }

    public boolean isPlanetGravityEnabled() { return planetGravityEnabled; }
    public PlanetType getPlanetType() { return planetType; }
    public CelestialData getPlanet() { return planet; }

    /**
     * Get gravity as a formatted string
     */
    public String getGravityString() {
        if (planetGravityEnabled && planet != null && planet.exploration != null) {
            return String.format("%s (%.2fg)", planet.name, planet.exploration.gravity);
        }
        return "Earth (1.00g)";
    }

    /**
     * Get color indicator for gravity status
     * Returns:  0 = normal (white), 1 = low (green), 2 = high (orange)
     */
    public int getGravityColorIndicator() {
        if (! planetGravityEnabled) return 0;
        if (planetType == PlanetType.LOW_GRAVITY) return 1;
        if (planetType == PlanetType.HIGH_GRAVITY) return 2;
        return 0;
    }
}
