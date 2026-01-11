package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.solar.MainGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class LoadingScreen extends BaseScreen {

    // Callback to run when loading finished
    private Runnable onLoadingFinished;
    private ProgressBar progressBar;
    private Label progressLabel;

    // Time tracker used to smooth the progress bar
    private float stateTime = 0f;
    private static final float MIN_LOADING_TIME = 2.0f; // minimum display time for loading screen

    public LoadingScreen(MainGame game, Runnable onLoadingFinished) {
        super(game);
        this.onLoadingFinished = onLoadingFinished;

        setBackground("background/background.png");

        // Ensure skin is available synchronously so UI can be built
        if (!game.getAssetManager().isLoaded("assets/uiskin.json")) {
            game.getAssetManager().load("assets/uiskin.json", Skin.class);
            game.getAssetManager().finishLoading();
        }
        this.skin = game.getSkin();

        // Build UI widgets
        setupUI();

        // Queue large assets for async loading
        if (!game.getAssetManager().isLoaded("images/assets.atlas")) {
            game.getAssetManager().load("images/assets.atlas", TextureAtlas.class);
        }
    }

    // Create and layout the loading UI (title, progress bar, percent label)
    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("LOADING...", skin);
        titleLabel.setAlignment(Align.center);

        progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
        progressBar.setValue(0);
        progressBar.setAnimateDuration(0.0f);

        progressLabel = new Label("0%", skin);

        table.add(titleLabel).padBottom(20).row();
        table.add(progressBar).width(400).height(30).padBottom(10).row();
        table.add(progressLabel);

        stage.addActor(table);
    }

    // Render loop: update asset manager, smooth progress visualization, and run callback when done
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;

        // Advance AssetManager; returns true when all queued assets finished loading
        boolean isLoaded = game.getAssetManager().update();

        // Visual progress = min(time-based progress, real asset progress) to avoid jumps
        float timeProgress = stateTime / MIN_LOADING_TIME;
        float realProgress = game.getAssetManager().getProgress();
        float visualProgress = Math.min(timeProgress, realProgress);
        visualProgress = MathUtils.clamp(visualProgress, 0f, 1f);

        progressBar.setValue(visualProgress);
        progressLabel.setText((int)(visualProgress * 100) + "%");

        // When both assets loaded and min display time passed, fire callback
        if (isLoaded && stateTime >= MIN_LOADING_TIME) {
            if (onLoadingFinished != null) {
                onLoadingFinished.run();
            }
        }

        stage.act(delta);
        stage.draw();
    }
}
