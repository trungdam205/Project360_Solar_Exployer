package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.solar.MainGame;
import com.solar.actor.PlayerActor;
import com.solar.data.*;

public class PlanetScreen extends BaseScreen {

    private PlanetType planet;
    private PlanetData data;
    private PlayerActor player;
    private float groundLineY;

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
        Group actorLayer  = new Group();

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

        // ===== UI =====
        addBackButton(() ->
            setScreenWithFade(new SolarSystemScreen(game))
        );
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
