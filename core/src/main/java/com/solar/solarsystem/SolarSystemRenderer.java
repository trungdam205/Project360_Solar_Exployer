package com.solar.solarsystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils. Disposable;
import com.solar.actor.Star;
import com. solar.celestial.CelestialBodyRenderer;
import com. solar.data.CelestialData;

/**
 * Rendering for Solar System screen
 */
public class SolarSystemRenderer implements Disposable {

    private final float worldWidth;
    private final float worldHeight;

    // Stars
    private final Array<Star> stars;

    // Celestial bodies
    private final Array<CelestialData> bodies;
    private final float[] bodyX;
    private final float[] bodyY;
    private final float[] bodySizes;

    // Renderer
    private final CelestialBodyRenderer bodyRenderer;

    public SolarSystemRenderer(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        // Initialize stars
        stars = new Array<>();
        for (int i = 0; i < 300; i++) {
            stars.add(new Star(
                MathUtils. random(-500, worldWidth + 500),
                MathUtils.random(-500, worldHeight + 500),
                MathUtils.random(1f, 3f),
                MathUtils.random(2f, 5f)
            ));
        }

        // Initialize celestial bodies
        bodies = CelestialData.createAllBodies();
        bodyX = new float[bodies. size];
        bodyY = new float[bodies.size];
        bodySizes = new float[bodies. size];

        // Initialize body renderer
        bodyRenderer = new CelestialBodyRenderer();
    }

    /**
     * Update positions and animations
     */
    public void update(float delta, SolarSystemState state) {
        bodyRenderer.update(delta);
        updatePositions(state);
    }

    /**
     * Update celestial body positions
     */
    private void updatePositions(SolarSystemState state) {
        float centerX = worldWidth / 2f + state.getOffsetX();
        float centerY = worldHeight / 2f + state.getOffsetY();
        float zoom = state.getZoom();
        float time = state.getTime();

        float earthX = 0, earthY = 0;

        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies. get(i);

            if (body.id. equals("sun")) {
                bodyX[i] = centerX;
                bodyY[i] = centerY;
                bodySizes[i] = body. baseSize * zoom;
            } else if (body.id. equals("moon")) {
                float angle = time * 0.01f * body.orbitSpeed;
                float orbitRadius = body. orbitRadius * zoom;
                bodyX[i] = earthX + MathUtils.cos(angle) * orbitRadius;
                bodyY[i] = earthY + MathUtils. sin(angle) * orbitRadius;
                bodySizes[i] = body. baseSize * zoom;
            } else {
                float angle = time * 0.01f * body.orbitSpeed + body.orbitPhase;
                float orbitRadius = body. orbitRadius * zoom;
                bodyX[i] = centerX + MathUtils.cos(angle) * orbitRadius;
                bodyY[i] = centerY + MathUtils. sin(angle) * orbitRadius;
                bodySizes[i] = body. baseSize * zoom;

                if (body. id.equals("earth")) {
                    earthX = bodyX[i];
                    earthY = bodyY[i];
                }
            }
        }
    }

    /**
     * Render everything
     */
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer,
                       OrthographicCamera camera, SolarSystemState state, float delta) {

        shapeRenderer.setProjectionMatrix(camera. combined);

        // Render stars
        shapeRenderer.begin(ShapeType. Filled);
        for (Star star :  stars) {
            star.update(delta);
            star.render(shapeRenderer);
        }
        shapeRenderer.end();

        // Render orbits
        if (state.isShowOrbits()) {
            renderOrbits(shapeRenderer, state);
        }

        // Render sun glow and fallback shapes
        shapeRenderer.begin(ShapeType. Filled);
        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies. get(i);
            if (body.id.equals("sun")) {
                bodyRenderer.renderSunGlow(shapeRenderer, bodyX[i], bodyY[i], bodySizes[i]);
            }
        }

        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies. get(i);
            if (! bodyRenderer.hasTexture(body.id)) {
                bodyRenderer.renderFallbackShape(shapeRenderer, body, bodyX[i], bodyY[i], bodySizes[i]);
            }
        }
        shapeRenderer.end();

        // Render celestial bodies with textures
        batch.setProjectionMatrix(camera. combined);
        batch.begin();
        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies.get(i);
            if (bodyRenderer.hasTexture(body.id)) {
                bodyRenderer.render(batch, body, bodyX[i], bodyY[i], bodySizes[i]);
            }
        }
        batch.end();
    }

    /**
     * Render orbit lines
     */
    private void renderOrbits(ShapeRenderer shapeRenderer, SolarSystemState state) {
        float centerX = worldWidth / 2f + state.getOffsetX();
        float centerY = worldHeight / 2f + state.getOffsetY();
        float zoom = state.getZoom();

        shapeRenderer.begin(ShapeType. Line);
        Color orbitColor = new Color(0.4f, 0.5f, 0.8f, 0.25f);

        for (CelestialData body : bodies) {
            if (body. orbitRadius > 0 && ! body.id.equals("moon")) {
                float orbitRadius = body.orbitRadius * zoom;
                shapeRenderer.setColor(orbitColor);
                shapeRenderer.circle(centerX, centerY, orbitRadius, 128);
            }
        }

        // Moon orbit around Earth
        float earthX = 0, earthY = 0;
        for (int i = 0; i < bodies.size; i++) {
            if (bodies.get(i).id.equals("earth")) {
                earthX = bodyX[i];
                earthY = bodyY[i];
                break;
            }
        }

        for (CelestialData body : bodies) {
            if (body.id.equals("moon")) {
                shapeRenderer.setColor(0.5f, 0.5f, 0.6f, 0.2f);
                shapeRenderer.circle(earthX, earthY, body.orbitRadius * zoom, 48);
            }
        }

        shapeRenderer.end();
    }

    /**
     * Check if a point hits a celestial body
     * @return The body that was hit, or null
     */
    public CelestialData getBodyAtPosition(float worldX, float worldY) {
        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies. get(i);
            float dx = worldX - bodyX[i];
            float dy = worldY - bodyY[i];
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            float clickRadius = Math.max(bodySizes[i], 25f);

            if (dist <= clickRadius) {
                return body;
            }
        }
        return null;
    }

    // Getters
    public Array<CelestialData> getBodies() { return bodies; }
    public float getBodyX(int index) { return bodyX[index]; }
    public float getBodyY(int index) { return bodyY[index]; }
    public float getBodySize(int index) { return bodySizes[index]; }

    @Override
    public void dispose() {
        if (bodyRenderer != null) {
            bodyRenderer.dispose();
        }
    }
}
