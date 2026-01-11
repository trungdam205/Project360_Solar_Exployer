package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.solar.MainGame;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public abstract class BaseScreen implements Screen {
    // World size constants
    protected static final float WORLD_WIDTH = 1920;
    protected static final float WORLD_HEIGHT = 1200;

    protected MainGame game;
    protected Stage stage;
    protected Skin skin;
    protected Texture bgTexture;
    protected Image bgImage;

    public BaseScreen(MainGame game) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), game.getBatch());
        this.skin = game.getSkin();
        if (this.skin == null) {
            throw new IllegalStateException("Skin not initialized in MainGame!");
        }
    }

    // Set background image for the screen
    protected void setBackground(String texturePath) {
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        bgTexture = new Texture(Gdx.files.internal(texturePath));
        this.bgImage = new Image(bgTexture);
        stage.addActor(this.bgImage);
        this.bgImage.toBack();
    }

    // Add a back button to the screen
    protected void addBackButton(Runnable onBack) {
        TextButton.TextButtonStyle originalStyle = skin.get(TextButton.TextButtonStyle.class);
        TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle(originalStyle);
        customStyle.font = skin.getFont("title-font");
        customStyle.fontColor = Color.WHITE;
        TextButton back = new TextButton("BACK", customStyle);
        back.setSize(160, 60);
        back.setPosition(40, 40);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack.run();
            }
        });
        stage.addActor(back);
    }

    // Handle screen resize
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        if (bgImage != null) {
            bgImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
            bgImage.setPosition(0, 0);
            bgImage.toBack();
        }
    }

    // Dispose resources
    @Override
    public void dispose() {
        stage.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }
}
