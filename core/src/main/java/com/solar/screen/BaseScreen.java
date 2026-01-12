package com.solar.screen;

import com.badlogic.gdx. Gdx;
import com.badlogic.gdx.Screen;
import com. badlogic.gdx.graphics.Color;
import com. badlogic.gdx.graphics. Pixmap;
import com.badlogic. gdx.graphics. Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d. Stage;
import com. badlogic.gdx.scenes.scene2d. Touchable;
import com.badlogic.gdx.scenes.scene2d. actions.Actions;
import com. badlogic.gdx.scenes.scene2d.ui.Image;
import com. badlogic.gdx.scenes.scene2d.ui. Skin;
import com.badlogic. gdx.scenes.scene2d. ui.TextButton;
import com.badlogic.gdx. utils. Scaling;
import com. badlogic.gdx.utils.viewport.FitViewport;
import com. solar.MainGame;

/**
 * Base screen class with common functionality
 */
public abstract class BaseScreen implements Screen {

    protected static final float WORLD_WIDTH = MainGame.WORLD_WIDTH;
    protected static final float WORLD_HEIGHT = MainGame.WORLD_HEIGHT;

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
        skin = game.getSkin();
        createFadeOverlay();
    }

    private void createFadeOverlay() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format. RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        blackTexture = new Texture(pixmap);
        fadeOverlay = new Image(blackTexture);
        fadeOverlay.setFillParent(true);
        fadeOverlay.setTouchable(Touchable.disabled);
        pixmap.dispose();
    }

    protected void setBackground(String texturePath) {
        if (bgTexture != null) bgTexture.dispose();
        if (backgroundImage != null) backgroundImage.remove();

        try {
            bgTexture = new Texture(Gdx. files.internal(texturePath));
            backgroundImage = new Image(bgTexture);
            stage.addActor(backgroundImage);
            backgroundImage.toBack();
        } catch (Exception e) {
            Gdx.app.error("BaseScreen", "Failed to load background:  " + texturePath);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        if (backgroundImage != null && bgTexture != null) {
            float worldW = stage.getViewport().getWorldWidth();
            float worldH = stage. getViewport().getWorldHeight();

            Vector2 scaledSize = Scaling.fill. apply(
                bgTexture.getWidth(), bgTexture.getHeight(),
                worldW, worldH
            );

            backgroundImage.setSize(scaledSize.x, scaledSize. y);
            backgroundImage.setPosition(
                (worldW - scaledSize.x) / 2,
                (worldH - scaledSize.y) / 2
            );
        }

        if (fadeOverlay != null) {
            fadeOverlay.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
            fadeOverlay. setPosition(0, 0);
            fadeOverlay.toFront();
        }
    }

    @Override
    public void show() {
        Gdx.input. setInputProcessor(stage);

        if (fadeOverlay != null) {
            stage.addActor(fadeOverlay);
            fadeOverlay.toFront();
            fadeOverlay.setColor(0, 0, 0, 1);
            fadeOverlay.addAction(Actions.sequence(
                Actions. fadeOut(0.5f),
                Actions.run(() -> fadeOverlay.remove())
            ));
        }
    }

    protected void setScreenWithFade(Screen nextScreen) {
        if (fadeOverlay != null) {
            stage.addActor(fadeOverlay);
            fadeOverlay. toFront();
            fadeOverlay.setColor(0, 0, 0, 0);
            fadeOverlay.addAction(Actions.sequence(
                Actions.fadeIn(0.5f),
                Actions. run(() -> game.setScreen(nextScreen))
            ));
        } else {
            game.setScreen(nextScreen);
        }
    }

    protected void addBackButton(Runnable onBack) {
        TextButton back = new TextButton("< Back", skin);
        back.setSize(160, 60);
        back.setPosition(40, WORLD_HEIGHT - 100);
        back.addListener(e -> {
            if (back.isPressed()) onBack.run();
            return true;
        });
        stage.addActor(back);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (blackTexture != null) blackTexture.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
