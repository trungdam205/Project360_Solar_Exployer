package com.solar. planet;

import com.badlogic.gdx. Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics. Texture;
import com.badlogic.gdx.graphics.g2d. SpriteBatch;
import com.badlogic.gdx. graphics.glutils.ShapeRenderer;
import com.badlogic. gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.solar.data.CelestialData;


/**
 * Obstacle management for planet exploration
 */
public class PlanetObstacles implements Disposable {

    private final Array<Obstacle> obstacles;
    private final ObjectMap<String, Texture> textures;
    private final CelestialData planet;
    private final PlanetPhysics physics;
    private final float groundY;
    private final float worldWidth;

    // Goal
    private final float goalX;
    private final Rectangle goalBounds;

    public PlanetObstacles(CelestialData planet, float groundY, float worldWidth, PlanetPhysics physics) {
        this. planet = planet;
        this.physics = physics;
        this.groundY = groundY;
        this.worldWidth = worldWidth;
        this.obstacles = new Array<>();
        this.textures = new ObjectMap<>();

        this.goalX = worldWidth - 350f;
        this. goalBounds = new Rectangle(goalX - 60, groundY, 120, 200);

        loadTextures();
        generate();
    }

    private void loadTextures() {
        try {
            // Wall texture
            String wallPath = "Obstacle/" + capitalize(planet.id) + "Obstacle1.png";
            if (Gdx.files. internal(wallPath).exists()) {
                textures.put("wall", new Texture(Gdx. files.internal(wallPath)));
            }

            // Spike texture (for high gravity planets)
            if (physics.getPlanetType() == PlanetPhysics.PlanetType.HIGH_GRAVITY) {
                String spikePath = "Obstacle/" + capitalize(planet. id) + "Spike.png";
                if (Gdx.files.internal(spikePath).exists()) {
                    textures.put("spike", new Texture(Gdx.files.internal(spikePath)));
                }
            }
            String flagPath = "woodenboard2.png";
            if (Gdx.files.internal(flagPath).exists()) {
                textures.put("flag", new Texture(Gdx.files.internal(flagPath)));
            }

            Gdx.app.log("PlanetObstacles", "Loaded " + textures.size + " textures for " + planet.name);
        } catch (Exception e) {
            Gdx.app.error("PlanetObstacles", "Error loading textures: " + e.getMessage());
        }

    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void generate() {
        float earthJumpHeight = physics.getEarthJumpHeight();
        float planetJumpHeight = physics. getPlanetJumpHeight();

        switch (physics.getPlanetType()) {
            case LOW_GRAVITY:
                generateLowGravityObstacles(earthJumpHeight, planetJumpHeight);
                break;
            case HIGH_GRAVITY:
                generateHighGravityObstacles(earthJumpHeight, planetJumpHeight);
                break;
            case NORMAL_GRAVITY:
                generateNormalObstacles(earthJumpHeight);
                break;
        }
    }

    private void generateLowGravityObstacles(float earthJump, float planetJump) {
        float wallHeight = earthJump + 54;
        if (wallHeight > wallHeight - 15) wallHeight = wallHeight - 15;
        if (wallHeight < 100) wallHeight = 100;

        Gdx.app.log("PlanetObstacles", "Low gravity walls - Height: " + wallHeight +
            ", EarthJump: " + earthJump + ", PlanetJump: " + planetJump);

        float[] positions = {700, 1400, 2100, 2800, 3500, 4200};
        for (float posX : positions) {
            obstacles.add(createWall(posX, wallHeight));
        }
    }

    private void generateHighGravityObstacles(float earthJump, float planetJump) {
        float wallHeight = 60;

        float safeGap = 50;
        float spikeY = groundY + wallHeight + safeGap + 20;
        Gdx. app.log("HighGravity", planet.name + " - Wall:  " + wallHeight +" "+ spikeY);


        if (spikeY > groundY + earthJump - 50) {
            spikeY = groundY + earthJump - 50;
        }
        Gdx. app.log("HighGravity", planet.name + " - Wall:  " + wallHeight +" "+ spikeY);

        float planetMaxY = groundY + planetJump;
        if (spikeY < planetMaxY + 40) {
            spikeY = planetMaxY + 40;
        }
        Gdx. app.log("HighGravity", planet.name + " - Wall:  " + wallHeight +" "+ spikeY);
        float[] positions = {600, 1300, 2000, 2700, 3400, 4100};
        for (float posX : positions) {
            obstacles.add(createWall(posX, wallHeight - 20));
            obstacles.add(createSpike(posX - 50, spikeY + wallHeight, 180, 30));
        }
    }

    private void generateNormalObstacles(float earthJump) {
        float wallHeight = earthJump * 0.7f;

        Gdx. app.log("PlanetObstacles", "Normal gravity walls - Height: " + wallHeight);

        float[] positions = {800, 1600, 2400, 3200, 4000};
        for (float posX : positions) {
            obstacles. add(createWall(posX, wallHeight));
        }
    }

    private Obstacle createWall(float x, float height) {
        Obstacle wall = new Obstacle(x, groundY, 80, height, Obstacle.Type. WALL);
        wall.texture = textures.get("wall");
        return wall;
    }

    private Obstacle createSpike(float x, float y, float width, float height) {
        Obstacle spike = new Obstacle(x, y, width, height, Obstacle.Type. SPIKE);
        spike.color = new Color(0.85f, 0.2f, 0.15f, 1f);
        spike.texture = textures. get("spike");
        return spike;
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, float cameraX, float screenWidth) {
        float viewLeft = cameraX - screenWidth;
        float viewRight = cameraX + screenWidth;

        // Render textures
        batch. begin();
        for (Obstacle obstacle :  obstacles) {
            if (isVisible(obstacle, viewLeft, viewRight) && obstacle.texture != null) {
                batch.draw(obstacle. texture, obstacle.x, obstacle.y, obstacle.width, obstacle.height);
            }
        }
        batch.end();

        // Render fallback shapes
        shapeRenderer. begin(ShapeType. Filled);
        for (Obstacle obstacle :  obstacles) {
            if (isVisible(obstacle, viewLeft, viewRight) && obstacle.texture == null) {
                renderObstacleShape(shapeRenderer, obstacle);
            }
        }
        shapeRenderer.end();
    }

    private boolean isVisible(Obstacle obstacle, float viewLeft, float viewRight) {
        return obstacle.x > viewLeft - obstacle.width - 100 && obstacle.x < viewRight + 100;
    }

    private void renderObstacleShape(ShapeRenderer renderer, Obstacle obstacle) {
        switch (obstacle.type) {
            case WALL:
                renderer.setColor(obstacle.color);
                renderer.rect(obstacle.x, obstacle. y, obstacle.width, obstacle.height);
                renderer.setColor(obstacle.color.cpy().mul(0.8f));
                renderer.rect(obstacle. x + 5, obstacle.y + 5, obstacle.width - 10, obstacle.height - 10);
                break;

            case SPIKE:
                int numSpikes = Math.max(3, (int)(obstacle.width / 35));
                float spikeWidth = obstacle.width / numSpikes;
                for (int i = 0; i < numSpikes; i++) {
                    float spikeX = obstacle.x + i * spikeWidth;
                    renderer.setColor(obstacle.color);
                    renderer. triangle(
                        spikeX, obstacle.y + obstacle.height,
                        spikeX + spikeWidth, obstacle. y + obstacle.height,
                        spikeX + spikeWidth / 2, obstacle. y
                    );
                }
                break;

            default:
                renderer. setColor(obstacle. color);
                renderer.rect(obstacle. x, obstacle.y, obstacle.width, obstacle.height);
                break;
        }
    }

    public void renderGoal(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Render pole
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(232/255f, 162/255f, 102/255f, 1f);
        shapeRenderer.rectLine(goalX, groundY, goalX, groundY + 150, 7);

        // Base
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(goalX - 35, groundY, 70, 18);
        shapeRenderer.end();

        // Render flag texture
        batch.begin();
        if (textures.get("flag") != null) {
            batch.draw(textures.get("flag"), goalX - 35f, groundY + 90, 70, 60);
        }
        batch.end();
    }

    // Getters
    public Array<Obstacle> getObstacles() { return obstacles; }
    public Rectangle getGoalBounds() { return goalBounds; }
    public float getGoalX() { return goalX; }

    @Override
    public void dispose() {
        for (Texture texture : textures. values()) {
            if (texture != null) texture.dispose();
        }
        textures.clear();
    }
}
