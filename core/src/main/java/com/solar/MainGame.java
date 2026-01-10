package com.solar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.solar.screen.LoadingScreen;
import com.solar.screen.MenuScreen;
import com.solar.screen.SolarSystemScreen;

public class MainGame extends Game {

    public SpriteBatch batch;
    public AssetManager assetManager;

    private Texture background;
    public SolarSystemScreen solarSystemScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        background = new Texture(Gdx.files.internal("background/background.png"));
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        solarSystemScreen = new SolarSystemScreen(this);

        setScreen(new LoadingScreen(
            this,
            "LOADING ASSETS...",
            true,
            () -> setScreen(new MenuScreen(MainGame.this))
        ));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(
            background,
            0, 0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        batch.end();
        super.render();
    }


    @Override
    public void dispose() {
        super.dispose();
        if (background != null) background.dispose();
        if (batch != null) batch.dispose();
        if (assetManager != null) assetManager.dispose();
    }

}
