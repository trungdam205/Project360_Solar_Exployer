package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.solar.MainGame;
import com.solar.data.*;

public class PlanetScreen extends BaseScreen {

    private PlanetType planet;
    private PlanetData data;

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

        Texture bgTexture = new Texture(Gdx.files.internal("background/background.png"));
        Image bg = new Image(bgTexture);
        bg.setFillParent(true);
        stage.addActor(bg);
        bg.toBack();

        System.out.println("Entered: " + data.displayName);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
