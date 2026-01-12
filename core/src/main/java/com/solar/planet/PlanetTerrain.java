package com.solar.planet;

import com. badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.solar.data.CelestialData;
import com. solar.entity.Mountain;
import com. solar.entity.TerrainFeature;

/**
 * Terrain generation and rendering for planet exploration
 */
public class PlanetTerrain {

    private final Array<TerrainFeature> features;
    private final Array<Mountain> mountains;
    private final CelestialData planet;
    private final float groundY;
    private final float worldWidth;
    private final float worldHeight;

    public PlanetTerrain(CelestialData planet, float groundY, float worldWidth, float worldHeight) {
        this.planet = planet;
        this.groundY = groundY;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.features = new Array<>();
        this.mountains = new Array<>();

        generate();
    }

    private void generate() {
        generateMountains();
        generateFeatures();
    }

    private void generateMountains() {
        for (int i = 0; i < 20; i++) {
            Mountain mountain = new Mountain();
            mountain.x = MathUtils. random(-300, worldWidth + 300);
            mountain.width = MathUtils.random(200, 500);
            mountain.height = MathUtils.random(150, 400);
            mountain. color = new Color(planet.exploration.terrainColor).mul(0.55f);
            mountains.add(mountain);
        }
    }

    private void generateFeatures() {
        for (int i = 0; i < 50; i++) {
            TerrainFeature feature = new TerrainFeature();
            feature.x = MathUtils. random(50, worldWidth - 50);
            feature.width = MathUtils.random(25, 70);
            feature. height = MathUtils.random(20, 60);
            feature. color = new Color(planet.exploration.terrainColor).mul(0.7f);
            features.add(feature);
        }
    }

    public void render(ShapeRenderer shapeRenderer, float cameraX) {
        float viewLeft = cameraX - worldWidth;
        float viewRight = cameraX + worldWidth;

        // Sky gradient
        renderSky(shapeRenderer, viewLeft);

        // Mountains (parallax)
        renderMountains(shapeRenderer, cameraX, viewLeft, viewRight);

        // Surface features
        renderFeatures(shapeRenderer, viewLeft, viewRight);

        // Ground
        renderGround(shapeRenderer, viewLeft);
    }

    private void renderSky(ShapeRenderer shapeRenderer, float viewLeft) {
        shapeRenderer.begin(ShapeType. Filled);
        Color top = planet.exploration.skyColorTop;
        Color bottom = planet.exploration.skyColorBottom;
        shapeRenderer.rect(viewLeft, 0, worldWidth * 3, worldHeight,
            bottom, bottom, top, top);
        shapeRenderer. end();
    }

    private void renderMountains(ShapeRenderer shapeRenderer, float cameraX, float viewLeft, float viewRight) {
        shapeRenderer.begin(ShapeType. Filled);
        for (Mountain mountain : mountains) {
            float parallaxX = mountain.x - (cameraX - worldWidth / 2) * 0.25f;
            if (parallaxX > viewLeft - mountain.width && parallaxX < viewRight + mountain.width) {
                shapeRenderer.setColor(mountain.color);
                shapeRenderer.triangle(
                    parallaxX, groundY,
                    parallaxX + mountain. width, groundY,
                    parallaxX + mountain.width / 2, groundY + mountain.height
                );
            }
        }
        shapeRenderer.end();
    }

    private void renderFeatures(ShapeRenderer shapeRenderer, float viewLeft, float viewRight) {
        shapeRenderer.begin(ShapeType.Filled);
        for (TerrainFeature feature : features) {
            if (feature.x > viewLeft - feature. width && feature.x < viewRight + feature.width) {
                shapeRenderer. setColor(feature. color);
                shapeRenderer.ellipse(feature.x, groundY - feature.height * 0.4f,
                    feature. width, feature.height);
            }
        }
        shapeRenderer.end();
    }

    private void renderGround(ShapeRenderer shapeRenderer, float viewLeft) {
        shapeRenderer. begin(ShapeType.Filled);
        shapeRenderer. setColor(planet.exploration.terrainColor);
        shapeRenderer.rect(viewLeft, 0, worldWidth * 3, groundY);

        Color darker = new Color(planet.exploration.terrainColor).mul(0.75f);
        shapeRenderer.rect(viewLeft, groundY - 8, worldWidth * 3, 16,
            darker, darker, planet.exploration. terrainColor, planet.exploration.terrainColor);
        shapeRenderer.end();
    }
}
