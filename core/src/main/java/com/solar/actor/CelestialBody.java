package com.solar.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class CelestialBody {
    public String id;
    public String name;
    public float x, y;
    public float size;
    public float baseSize;
    public Color innerColor;
    public Color outerColor;
    public Color accentColor;

    // Info data
    public String type;
    public String diameter;
    public String mass;
    public String temperature;
    public String distance;
    public String orbitalPeriod;
    public String orbitalSpeed;
    public String[] facts;

    // Animation
    private float pulseTime = 0;

    // Special rendering flags
    public boolean hasRings = false;
    public boolean hasGreatSpot = false;
    public boolean hasPolarCap = false;
    public boolean hasCraters = false;
    public boolean hasContinents = false;
    public boolean hasHeartRegion = false;

    public CelestialBody(String id, String name, float size, Color innerColor, Color outerColor) {
        this. id = id;
        this.name = name;
        this.baseSize = size;
        this.size = size;
        this.innerColor = innerColor;
        this.outerColor = outerColor;
        this.accentColor = new Color(outerColor).lerp(Color.WHITE, 0.3f);
    }

    public CelestialBody setAccentColor(Color color) {
        this.accentColor = color;
        return this;
    }

    public CelestialBody setRings(boolean hasRings) {
        this.hasRings = hasRings;
        return this;
    }

    public CelestialBody setGreatSpot(boolean hasSpot) {
        this.hasGreatSpot = hasSpot;
        return this;
    }

    public CelestialBody setPolarCap(boolean hasCap) {
        this.hasPolarCap = hasCap;
        return this;
    }

    public CelestialBody setCraters(boolean hasCraters) {
        this. hasCraters = hasCraters;
        return this;
    }

    public CelestialBody setContinents(boolean hasContinents) {
        this.hasContinents = hasContinents;
        return this;
    }

    public CelestialBody setHeartRegion(boolean hasHeart) {
        this.hasHeartRegion = hasHeart;
        return this;
    }

    public void setData(String type, String diameter, String mass, String temperature,
                        String distance, String orbitalPeriod, String orbitalSpeed, String[] facts) {
        this.type = type;
        this.diameter = diameter;
        this.mass = mass;
        this.temperature = temperature;
        this. distance = distance;
        this.orbitalPeriod = orbitalPeriod;
        this.orbitalSpeed = orbitalSpeed;
        this.facts = facts;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this. y = y;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public boolean contains(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        return dx * dx + dy * dy <= size * size;
    }

    public void render(ShapeRenderer renderer) {
        pulseTime += 0.05f;

        // Special rendering for Sun
        if (id.equals("sun")) {
            renderSun(renderer);
            return;
        }

        // Render rings behind planet (Saturn)
        if (hasRings) {
            renderRings(renderer, false);
        }

        // Main body - outer color
        renderer.setColor(outerColor);
        renderer.circle(x, y, size, 48);

        // Inner highlight (lighting effect)
        renderer.setColor(innerColor);
        renderer.circle(x - size * 0.15f, y + size * 0.15f, size * 0.75f, 48);

        // Brighter core highlight
        Color brightCore = new Color(innerColor).lerp(Color. WHITE, 0.3f);
        renderer.setColor(brightCore. r, brightCore. g, brightCore. b, 0.6f);
        renderer.circle(x - size * 0.25f, y + size * 0.25f, size * 0.4f, 32);

        // Special features
        if (hasContinents) {
            renderContinents(renderer);
        }

        if (hasCraters) {
            renderCraters(renderer);
        }

        if (hasPolarCap) {
            renderPolarCap(renderer);
        }

        if (hasGreatSpot) {
            renderGreatSpot(renderer);
        }

        if (hasHeartRegion) {
            renderHeartRegion(renderer);
        }

        // Jupiter bands
        if (id.equals("jupiter")) {
            renderJupiterBands(renderer);
        }

        // Render rings in front of planet (Saturn)
        if (hasRings) {
            renderRings(renderer, true);
        }

        // Shadow on the right side
        renderer.setColor(0, 0, 0, 0.3f);
        renderer.arc(x, y, size, -60, 120, 24);
    }

    private void renderSun(ShapeRenderer renderer) {
        // Outer glow
        float glowSize = size * 1.6f + MathUtils.sin(pulseTime) * size * 0.1f;
        renderer.setColor(1f, 0.5f, 0f, 0.15f);
        renderer.circle(x, y, glowSize, 48);

        glowSize = size * 1.4f + MathUtils.sin(pulseTime * 1.5f) * size * 0.08f;
        renderer.setColor(1f, 0.6f, 0.1f, 0.25f);
        renderer.circle(x, y, glowSize, 48);

        glowSize = size * 1.2f + MathUtils. sin(pulseTime * 2f) * size * 0.05f;
        renderer.setColor(1f, 0.7f, 0.2f, 0.4f);
        renderer.circle(x, y, glowSize, 48);

        // Main sun body
        renderer.setColor(outerColor);
        renderer.circle(x, y, size, 48);

        // Inner gradient layers
        renderer.setColor(innerColor);
        renderer.circle(x - size * 0.1f, y + size * 0.1f, size * 0.85f, 48);

        renderer.setColor(1f, 0.95f, 0.8f, 1f);
        renderer.circle(x - size * 0.2f, y + size * 0.2f, size * 0.6f, 48);

        // Bright core
        renderer. setColor(1f, 1f, 0.95f, 0.8f);
        renderer.circle(x - size * 0.25f, y + size * 0.25f, size * 0.35f, 32);
    }

    private void renderContinents(ShapeRenderer renderer) {
        // Green continents for Earth
        renderer. setColor(0.24f, 0.55f, 0.25f, 0.9f);

        // North America/Europe area
        renderer.circle(x - size * 0.2f, y + size * 0.15f, size * 0.25f, 16);

        // South America/Africa area
        renderer.circle(x + size * 0.1f, y - size * 0.2f, size * 0.18f, 16);

        // Asia area
        renderer.circle(x + size * 0.25f, y + size * 0.1f, size * 0.15f, 12);
    }

    private void renderCraters(ShapeRenderer renderer) {
        // Moon/Mercury craters
        renderer.setColor(0.35f, 0.35f, 0.35f, 0.5f);
        renderer.circle(x - size * 0.2f, y + size * 0.25f, size * 0.12f, 12);
        renderer.circle(x + size * 0.15f, y - size * 0.1f, size * 0.1f, 10);
        renderer.circle(x - size * 0.1f, y - size * 0.3f, size * 0.08f, 8);
        renderer.circle(x + size * 0.3f, y + size * 0.15f, size * 0.06f, 8);
    }

    private void renderPolarCap(ShapeRenderer renderer) {
        // Mars polar ice cap
        renderer. setColor(1f, 1f, 1f, 0.5f);
        renderer.arc(x, y, size * 0.9f, 50, 80, 16);
    }

    private void renderGreatSpot(ShapeRenderer renderer) {
        // Jupiter's Great Red Spot
        renderer.setColor(0.85f, 0.4f, 0.3f, 0.7f);

        // Draw ellipse-like spot
        float spotX = x + size * 0.15f;
        float spotY = y - size * 0.1f;
        float spotWidth = size * 0.25f;
        float spotHeight = size * 0.15f;

        for (int i = 0; i < 8; i++) {
            float angle = i * MathUtils. PI2 / 8;
            float px = spotX + MathUtils.cos(angle) * spotWidth * 0.5f;
            float py = spotY + MathUtils.sin(angle) * spotHeight * 0.5f;
            renderer.circle(px, py, size * 0.06f, 8);
        }
        renderer.circle(spotX, spotY, size * 0.08f, 12);
    }

    private void renderJupiterBands(ShapeRenderer renderer) {
        // Horizontal bands
        Color bandLight = new Color(0.9f, 0.8f, 0.6f, 0.3f);
        Color bandDark = new Color(0.7f, 0.55f, 0.35f, 0.3f);

        for (int i = -3; i <= 3; i++) {
            float bandY = y + i * size * 0.18f;
            float bandWidth = (float) Math.sqrt(size * size - (i * size * 0.18f) * (i * size * 0.18f));
            if (bandWidth > 0) {
                renderer.setColor(i % 2 == 0 ? bandLight : bandDark);
                renderer.rect(x - bandWidth, bandY - size * 0.05f, bandWidth * 2, size * 0.1f);
            }
        }
    }

    private void renderRings(ShapeRenderer renderer, boolean front) {
        // Saturn's rings
        float ringInner = size * 1.3f;
        float ringOuter = size * 2.0f;

        // Ring color with transparency
        if (front) {
            renderer.setColor(0.85f, 0.78f, 0.6f, 0.4f);
        } else {
            renderer.setColor(0.75f, 0.68f, 0.5f, 0.5f);
        }

        // Draw ring segments
        int segments = 60;
        for (int i = 0; i < segments; i++) {
            float angle1 = i * MathUtils. PI2 / segments;
            float angle2 = (i + 1) * MathUtils.PI2 / segments;

            // Only draw front or back half
            boolean isFront = Math.sin(angle1) > 0;
            if (isFront != front) continue;

            // Ring tilt effect (viewed at angle)
            float tilt = 0.3f;

            float x1Inner = x + MathUtils.cos(angle1) * ringInner;
            float y1Inner = y + MathUtils.sin(angle1) * ringInner * tilt;
            float x1Outer = x + MathUtils.cos(angle1) * ringOuter;
            float y1Outer = y + MathUtils. sin(angle1) * ringOuter * tilt;

            float x2Inner = x + MathUtils. cos(angle2) * ringInner;
            float y2Inner = y + MathUtils.sin(angle2) * ringInner * tilt;
            float x2Outer = x + MathUtils. cos(angle2) * ringOuter;
            float y2Outer = y + MathUtils.sin(angle2) * ringOuter * tilt;

            renderer.triangle(x1Inner, y1Inner, x1Outer, y1Outer, x2Inner, y2Inner);
            renderer.triangle(x2Inner, y2Inner, x1Outer, y1Outer, x2Outer, y2Outer);
        }

        // Additional ring bands
        if (! front) {
            renderer.setColor(0.65f, 0.58f, 0.4f, 0.3f);
            for (int i = 0; i < segments; i++) {
                float angle1 = i * MathUtils.PI2 / segments;
                float angle2 = (i + 1) * MathUtils.PI2 / segments;

                boolean isFront = Math.sin(angle1) > 0;
                if (isFront) continue;

                float tilt = 0.3f;
                float midRing = size * 1.6f;

                float x1 = x + MathUtils.cos(angle1) * midRing;
                float y1 = y + MathUtils. sin(angle1) * midRing * tilt;
                float x2 = x + MathUtils.cos(angle2) * midRing;
                float y2 = y + MathUtils.sin(angle2) * midRing * tilt;

                renderer.rectLine(x1, y1, x2, y2, size * 0.08f);
            }
        }
    }

    private void renderHeartRegion(ShapeRenderer renderer) {
        // Pluto's heart-shaped region (Tombaugh Regio)
        renderer.setColor(0.9f, 0.85f, 0.75f, 0.5f);

        // Simplified heart shape using circles
        float heartX = x - size * 0.1f;
        float heartY = y + size * 0.05f;

        renderer.circle(heartX - size * 0.12f, heartY + size * 0.08f, size * 0.15f, 12);
        renderer.circle(heartX + size * 0.12f, heartY + size * 0.08f, size * 0.15f, 12);
        renderer.triangle(
            heartX - size * 0.25f, heartY + size * 0.05f,
            heartX + size * 0.25f, heartY + size * 0.05f,
            heartX, heartY - size * 0.2f
        );
    }
}
