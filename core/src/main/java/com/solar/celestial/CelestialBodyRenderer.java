package com. solar.celestial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com. badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic. gdx.graphics. g2d.TextureRegion;
import com.badlogic.gdx. graphics.glutils.ShapeRenderer;
import com.badlogic. gdx.math.MathUtils;
import com.badlogic.gdx.utils. Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.solar.data.CelestialData;

/**
 * Renderer for celestial bodies using texture atlas
 */
public class CelestialBodyRenderer implements Disposable {

    private TextureAtlas atlas;
    private ObjectMap<String, TextureRegion> planetTextures;
    private float pulseTime = 0;

    private static final Color SUN_GLOW_OUTER = new Color(1f, 0.5f, 0f, 0.15f);
    private static final Color SUN_GLOW_MID = new Color(1f, 0.6f, 0.1f, 0.25f);
    private static final Color SUN_GLOW_INNER = new Color(1f, 0.7f, 0.2f, 0.4f);

    public CelestialBodyRenderer() {
        try {
            atlas = new TextureAtlas(Gdx. files.internal("planets/planets.atlas"));

            planetTextures = new ObjectMap<>();
            planetTextures. put("sun", atlas.findRegion("sun"));
            planetTextures.put("mercury", atlas.findRegion("mercury"));
            planetTextures.put("venus", atlas. findRegion("venus"));
            planetTextures.put("earth", atlas.findRegion("earth"));
            planetTextures. put("moon", atlas.findRegion("moon"));
            planetTextures. put("mars", atlas.findRegion("mars"));
            planetTextures.put("jupiter", atlas.findRegion("jupiter"));
            planetTextures.put("saturn", atlas.findRegion("saturn"));
            planetTextures.put("uranus", atlas.findRegion("uranus"));
            planetTextures.put("neptune", atlas.findRegion("neptune"));

            Gdx.app.log("CelestialBodyRenderer", "Loaded planet textures");
        } catch (Exception e) {
            Gdx.app.error("CelestialBodyRenderer", "Failed to load planet atlas", e);
            planetTextures = new ObjectMap<>();
        }
    }

    public void update(float delta) {
        pulseTime += delta;
    }

    public void renderSunGlow(ShapeRenderer shapeRenderer, float x, float y, float size) {
        float pulse = MathUtils.sin(pulseTime * 2f) * 0.1f;

        float glowSize = size * (1.8f + pulse);
        shapeRenderer.setColor(SUN_GLOW_OUTER);
        shapeRenderer.circle(x, y, glowSize, 48);

        glowSize = size * (1.5f + pulse * 0.8f);
        shapeRenderer.setColor(SUN_GLOW_MID);
        shapeRenderer.circle(x, y, glowSize, 48);

        glowSize = size * (1.25f + pulse * 0.5f);
        shapeRenderer.setColor(SUN_GLOW_INNER);
        shapeRenderer.circle(x, y, glowSize, 48);
    }

    public void render(SpriteBatch batch, CelestialData body, float x, float y, float size) {
        TextureRegion texture = planetTextures. get(body.id);

        if (texture != null) {
            float drawSize = size * 2;

            if (body.id. equals("saturn")) {
                float aspectRatio = (float) texture.getRegionWidth() / texture.getRegionHeight();
                float drawWidth = drawSize * aspectRatio * 1.5f;
                float drawHeight = drawSize * 1.5f;
                batch.draw(texture,
                    x - drawWidth / 2,
                    y - drawHeight / 2,
                    drawWidth,
                    drawHeight);
            } else {
                batch.draw(texture,
                    x - drawSize / 2,
                    y - drawSize / 2,
                    drawSize,
                    drawSize);
            }
        }
    }

    public void renderFallbackShape(ShapeRenderer shapeRenderer, CelestialData body, float x, float y, float size) {
        if (! hasTexture(body.id)) {
            // Main body
            shapeRenderer.setColor(body.outerColor);
            shapeRenderer.circle(x, y, size, 32);

            // Inner highlight
            shapeRenderer.setColor(body.innerColor);
            shapeRenderer. circle(x - size * 0.15f, y + size * 0.15f, size * 0.7f, 32);

            // Special features
            if (body.hasHeartRegion) {
                renderHeartRegion(shapeRenderer, x, y, size);
            }
            if (body.hasCraters) {
                renderCraters(shapeRenderer, x, y, size);
            }
        }
    }

    private void renderHeartRegion(ShapeRenderer shapeRenderer, float x, float y, float size) {
        shapeRenderer.setColor(0.9f, 0.85f, 0.75f, 0.5f);
        float heartX = x - size * 0.1f;
        float heartY = y + size * 0.05f;
        shapeRenderer.circle(heartX - size * 0.1f, heartY + size * 0.06f, size * 0.12f, 12);
        shapeRenderer.circle(heartX + size * 0.1f, heartY + size * 0.06f, size * 0.12f, 12);
        shapeRenderer. triangle(
            heartX - size * 0.2f, heartY + size * 0.03f,
            heartX + size * 0.2f, heartY + size * 0.03f,
            heartX, heartY - size * 0.15f
        );
    }

    private void renderCraters(ShapeRenderer shapeRenderer, float x, float y, float size) {
        shapeRenderer.setColor(0.35f, 0.35f, 0.35f, 0.5f);
        shapeRenderer.circle(x - size * 0.2f, y + size * 0.25f, size * 0.12f, 12);
        shapeRenderer.circle(x + size * 0.15f, y - size * 0.1f, size * 0.1f, 10);
        shapeRenderer.circle(x - size * 0.1f, y - size * 0.3f, size * 0.08f, 8);
    }

    public boolean hasTexture(String bodyId) {
        return planetTextures != null &&
            planetTextures.containsKey(bodyId) &&
            planetTextures.get(bodyId) != null;
    }

    @Override
    public void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }
}
