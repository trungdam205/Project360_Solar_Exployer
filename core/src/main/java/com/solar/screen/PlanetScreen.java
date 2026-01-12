package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.solar.MainGame;
import com.solar.actor.PlayerActor;
import com.solar.actor.obstacle.RockObstacle;
import com.solar.actor.obstacle.SpineRockObstacle;
import com.solar.data.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.math.Rectangle;
import com.solar.actor.obstacle.Obstacle;

public class PlanetScreen extends BaseScreen {

    private PlanetType planet;
    private PlanetData data;
    private PlayerActor player;
    private float groundLineY;
    private Group actorLayer;
    private ShapeRenderer debugRenderer = new ShapeRenderer();

    public PlanetScreen(MainGame game, PlanetType planet) {
        super(game);
        this.planet = planet;
        this.data = PlanetDatabase.get(planet);

        addBackButton(() ->
            setScreenWithFade(new SolarSystemScreen(game))
        );

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        super.show();

        // ===== LAYERS =====
        Group starLayer   = new Group();
        Group planetLayer = new Group();
        actorLayer  = new Group();

        stage.addActor(starLayer);
        stage.addActor(planetLayer);
        stage.addActor(actorLayer);

        // ===== STAR BACKGROUND =====
        Texture starTexture =
            new Texture(Gdx.files.internal("background/background.png"));

        Image starBg = new Image(starTexture);
        starBg.setSize(
            stage.getViewport().getWorldWidth(),
            stage.getViewport().getWorldHeight()
        );
        starBg.setPosition(0, 0);

        starLayer.addActor(starBg);

        // ===== PLANET = GROUND =====
        Texture groundTexture =
            new Texture(Gdx.files.internal(data.texturePathPlanetScreen));

        Image ground = new Image(groundTexture);

        ground.setSize(
            stage.getViewport().getWorldWidth(),
            groundTexture.getHeight()
        );
        ground.setPosition(0, 0);
        planetLayer.addActor(ground);

        // ===== GROUND LINE TỪ DATABASE =====
        groundLineY = ground.getHeight() * data.groundHeightRatio;

        // ===== PLAYER =====
        player = new PlayerActor(data.gravity, groundLineY);
        player.setPosition(50, groundLineY);
        actorLayer.addActor(player);

        // ===== OBSTACLES =====
        if (data.obstacles != null) {
            for (ObstacleData o : data.obstacles) {
                Texture tex = new Texture(Gdx.files.internal(o.texturePath));

                Obstacle obstacle;

                switch (o.type) {
                    case ROCK:
                        obstacle = new RockObstacle(
                            tex,
                            o.x, o.y,
                            o.width, o.height
                        );
                        break;

                    case SPINE:
                        obstacle = new SpineRockObstacle(
                            tex,
                            o.x, o.y,
                            o.width, o.height,
                            50, groundLineY // respawn
                        );
                        break;

                    default:
                        continue;
                }

                actorLayer.addActor(obstacle);

                actorLayer.addActor(obstacle); // ⚠️ dùng actorLayer
            }
        }

        // ===== GRAVITY COMPARISON UI =====
        Table gravityTable = new Table();
        gravityTable.setFillParent(true);
        gravityTable.bottom().right().pad(40);

// Button: Earth's Gravity
        TextButton earthGravityBtn =
            new TextButton("Earth's Gravity", skin);

// Button: Planet Gravity
        TextButton planetGravityBtn =
            new TextButton(data.displayName + "'s Gravity", skin);

// ===== ACTIONS =====
        earthGravityBtn.addListener(e -> {
            if (!earthGravityBtn.isPressed()) return false;
            player.setGravity(9.81f);
            return true;
        });

        planetGravityBtn.addListener(e -> {
            if (!planetGravityBtn.isPressed()) return false;
            player.setGravity(data.gravity);
            return true;
        });

// ===== LAYOUT =====
        gravityTable.add(earthGravityBtn)
            .width(300)
            .height(70)
            .padBottom(15)
            .row();

        gravityTable.add(planetGravityBtn)
            .width(300)
            .height(70);

        stage.addActor(gravityTable);

        // ===== UI =====
        addBackButton(() ->
            setScreenWithFade(new SolarSystemScreen(game))
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float deltaX = player.getVelocityX() * delta;
        float nextX = player.getX() + deltaX;

        for (Actor a : actorLayer.getChildren()) {
            if (!(a instanceof Obstacle)) continue;

            Obstacle o = (Obstacle) a;

            // ✅ CHỈ CẦN OVERLAP THEO Y
            boolean overlapY =
                player.getY() < o.getY() + o.getHeight() &&
                    player.getY() + player.getHeight() > o.getY();

            if (!overlapY) continue;

            // 👉 ĐANG ĐI SANG PHẢI
            if (deltaX > 0 &&
                player.getX() + player.getWidth() <= o.getX() &&
                nextX + player.getWidth() > o.getX()) {

                nextX = o.getX() - player.getWidth();
                player.setVelocityX(0);
            }

            // 👉 ĐANG ĐI SANG TRÁI
            if (deltaX < 0 &&
                player.getX() >= o.getX() + o.getWidth() &&
                nextX < o.getX() + o.getWidth()) {

                nextX = o.getX() + o.getWidth();
                player.setVelocityX(0);
            }
        }

        player.setX(nextX);

        // ===== DEBUG HITBOX =====
        debugRenderer.setProjectionMatrix(stage.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(1, 0, 0, 1);

// PLAYER
        debugRenderer.rect(
            player.getX(),
            player.getY(),
            player.getWidth(),
            player.getHeight()
        );

// OBSTACLES
        for (Actor a : actorLayer.getChildren()) {
            if (a instanceof Obstacle) {
                Obstacle o = (Obstacle) a;
                debugRenderer.rect(
                    o.getX(),
                    o.getY(),
                    o.getWidth(),
                    o.getHeight()
                );
            }
        }

        debugRenderer.end();


        stage.act(delta);
        stage.draw();

        debugRenderer.setProjectionMatrix(stage.getCamera().combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(1, 0, 0, 1);

// player hitbox
        debugRenderer.rect(
            player.getX(),
            player.getY(),
            player.getWidth(),
            player.getHeight()
        );

// obstacle hitbox
        for (Actor a : actorLayer.getChildren()) {
            if (a instanceof Obstacle) {
                Obstacle o = (Obstacle) a;
                debugRenderer.rect(
                    o.getX(),
                    o.getY(),
                    o.getWidth(),
                    o.getHeight()
                );
            }
        }

        debugRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
