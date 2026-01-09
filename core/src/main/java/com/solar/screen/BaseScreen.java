package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.solar.MainGame;

public abstract class BaseScreen implements Screen {

    protected static final float WORLD_WIDTH = 1920;
    protected static final float WORLD_HEIGHT = 1200;

    protected MainGame game;
    protected Stage stage;
    protected Skin skin;
    protected Texture bgTexture;

    public BaseScreen(MainGame game) {
        this.game = game;

        // ExtendViewport helps extend the window with fixed ratio
        stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT),game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
    }
    /*
    Sets background image for screen.
    Handles texture memory management automatically
    */
    protected void setBackground(String texturePath) {
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        bgTexture = new Texture(Gdx.files.internal(texturePath));
        Image bgImage = new Image(bgTexture);
        stage.addActor(bgImage);
        bgImage.toBack();
    }

    /*
    Add "Back" Button
    @param onBack: a runnable containing the logic to execute when button is pressed
     */
    protected void addBackButton(Runnable onBack) {
        TextButton back = new TextButton("Back", skin);
        back.setSize(160, 60);
        back.setPosition(40, 40);
        back.addListener(e -> {
            if (back.isPressed()) {
                onBack.run();
            }
            return true;
        });
        stage.addActor(back);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // update viewport
        //  find and stretch background
        if (stage.getActors().size > 0 && stage.getActors().get(0) instanceof Image) {
            Image bg = (Image) stage.getActors().get(0);

            // force background size to match extended window
            bg.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());

            // re-center background logic
            bg.setPosition(0, 0);
        }
    }

    // dispose of stage and skin to free up native resources
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void show() {}
    @Override public void render(float delta) {}
}
