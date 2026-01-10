package com.solar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.solar.screen.LoadingScreen;
import com.solar.screen.MenuScreen;

public class MainGame extends Game {


    public SpriteBatch batch;
    public AssetManager assetManager;


    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();


        assetManager.load("assets/uiskin.json", Skin.class);
        assetManager.load("images/assets.atlas", TextureAtlas.class);

        this.setScreen(new com.solar.screen.LoadingScreen(this, new Runnable() {
            @Override
            public void run() {
                // Load xong thì vào Menu
                setScreen(new com.solar.screen.MenuScreen(MainGame.this));
            }
        }));

    }

    @Override
    public void render() {
        super.render();

        // --- LOGIC FULLSCREEN ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            // Nếu đang là Fullscreen -> Chuyển về Cửa sổ
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 800);
            }
            // Nếu đang là Cửa sổ -> Chuyển sang Fullscreen
            else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch != null) batch.dispose();
        if (assetManager != null) assetManager.dispose();
    }

}
