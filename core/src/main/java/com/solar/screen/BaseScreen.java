package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.solar.MainGame;

public abstract class BaseScreen implements Screen {

    protected static final float WORLD_WIDTH = 1920;
    protected static final float WORLD_HEIGHT = 1200;

    protected MainGame game;
    protected Stage stage;
    protected Skin skin;
    protected Texture bgTexture;
    protected Image backgroundImage;

    private Image fadeOverlay;
    private Texture blackTexture;

    public BaseScreen(MainGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT), game.batch);

        if (game.assetManager.isLoaded("assets/uiskin.json")) {
            skin = game.assetManager.get("assets/uiskin.json", Skin.class);
        } else {
            skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        }

        createFadeOverlay();
    }

    private void createFadeOverlay() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        blackTexture = new Texture(pixmap);
        fadeOverlay = new Image(blackTexture);
        fadeOverlay.setFillParent(true);
        fadeOverlay.setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.disabled);
        pixmap.dispose();
    }

    protected void setBackground(String texturePath) {
        if (bgTexture != null) bgTexture.dispose();
        if (backgroundImage != null) backgroundImage.remove();

        bgTexture = new Texture(Gdx.files.internal(texturePath));
        backgroundImage = new Image(bgTexture);

        stage.addActor(backgroundImage);
        backgroundImage.toBack();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        // LOGIC TÍNH TOÁN "COVER" (Phủ kín màn hình mà không méo)
        if (backgroundImage != null && bgTexture != null) {
            float worldW = stage.getViewport().getWorldWidth();
            float worldH = stage.getViewport().getWorldHeight();

            // Tính toán kích thước mới
            Vector2 scaledSize = Scaling.fill.apply(
                bgTexture.getWidth(), bgTexture.getHeight(),
                worldW, worldH
            );

            // Áp dụng kích thước
            backgroundImage.setSize(scaledSize.x, scaledSize.y);

            // Căn giữa
            backgroundImage.setPosition(
                (worldW - scaledSize.x) / 2,
                (worldH - scaledSize.y) / 2
            );
        }

        if (fadeOverlay != null) {
            fadeOverlay.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
            fadeOverlay.setPosition(0, 0);
            fadeOverlay.toFront();
        }
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if(fadeOverlay != null) {
            stage.addActor(fadeOverlay);
            fadeOverlay.toFront();
            fadeOverlay.setColor(0, 0, 0, 1);
            fadeOverlay.addAction(Actions.sequence(
                Actions.fadeOut(0.5f),
                Actions.run(() -> fadeOverlay.remove())
            ));
        }
    }

    // ... (Giữ nguyên các hàm khác) ...
    protected void setScreenWithFade(Screen nextScreen) {
        stage.addActor(fadeOverlay);
        fadeOverlay.toFront();
        fadeOverlay.setColor(0, 0, 0, 0);
        fadeOverlay.addAction(Actions.sequence(
            Actions.fadeIn(0.5f),
            Actions.run(() -> game.setScreen(nextScreen))
        ));
    }

    protected void addBackButton(Runnable onBack) {
        TextButton back = new TextButton("Back", skin);
        back.setSize(160, 60);
        back.setPosition(40, 40);
        back.addListener(e -> {
            if (back.isPressed()) onBack.run();
            return true;
        });
        stage.addActor(back);
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (blackTexture != null) blackTexture.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
