package com.solar.screen;

import com.badlogic.gdx. Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic. gdx.graphics. g2d.TextureAtlas;
import com.badlogic.gdx. scenes.scene2d. actions.Actions;
import com. badlogic.gdx.scenes.scene2d. ui.Label;
import com. badlogic.gdx.scenes.scene2d.ui. ProgressBar;
import com.badlogic.gdx.scenes.scene2d. ui. Skin;
import com.badlogic. gdx.scenes.scene2d. ui.Table;
import com.badlogic.gdx. utils.Align;
import com.solar. MainGame;

public class LoadingScreen extends BaseScreen {

    private Runnable onLoadingFinished;
    private ProgressBar progressBar;
    private Label progressLabel;
    private String loadingMessage;

    private boolean showProgressBar;

    private float stateTime = 0f;

    private static final float LOADING_DURATION = 2.5f;

    public LoadingScreen(MainGame game, String message, boolean showProgressBar, Runnable onLoadingFinished) {
        super(game);
        setBackground("background/background.png");
        this.loadingMessage = message;
        this.showProgressBar = showProgressBar;
        this.onLoadingFinished = onLoadingFinished;

        // Load UI skin using internal path relative to assets root
        final String SKIN_PATH = "font/uiskin.json";
        if (!game.assetManager.isLoaded(SKIN_PATH)) {
            game.assetManager.load(SKIN_PATH, Skin.class);
            game.assetManager.finishLoading();
        }
        this.skin = game.assetManager.get(SKIN_PATH, Skin.class);

        setupUI();

        // Queue heavy assets
        if (!game.assetManager.isLoaded("planets/planets.atlas")) {
            game.assetManager.load("planets/planets.atlas", TextureAtlas.class);
        }
    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label(loadingMessage, skin);
        titleLabel.setFontScale(2f);
        titleLabel.setAlignment(Align.center);

        if (! showProgressBar) {
            titleLabel.addAction(Actions.forever(
                    Actions.sequence(
                            Actions.fadeOut(0.9f),
                            Actions.fadeIn(0.9f)
                    )
            ));
        }

        table.add(titleLabel).padBottom(30).row();

        if (showProgressBar) {
            progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
            progressBar.setAnimateDuration(0.1f);

            progressLabel = new Label("0%", skin);

            table.add(progressBar).width(400).height(30).padBottom(10).row();
            table. add(progressLabel);
        }

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl. glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;

        boolean assetLoaded = game.assetManager.update();

        float visualProgress = stateTime / LOADING_DURATION;
        float currentDisplayValue = Math.min(visualProgress, 1.0f);

        if (showProgressBar && progressBar != null && progressLabel != null) {
            progressBar.setValue(currentDisplayValue);
            progressLabel. setText((int)(currentDisplayValue * 100) + "%");
        }

        if (assetLoaded && visualProgress >= 1.0f) {
            if (onLoadingFinished != null) {
                onLoadingFinished.run();
            }
        }

        stage.act(delta);
        stage.draw();
    }
}
