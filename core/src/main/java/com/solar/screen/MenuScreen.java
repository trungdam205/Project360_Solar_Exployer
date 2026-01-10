package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.solar.MainGame;

public class MenuScreen extends BaseScreen {
    private Skin skin;
    private MainGame game;

    public MenuScreen(MainGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        if (game.assetManager.isLoaded("assets/uiskin.json")) {
            skin = game.assetManager.get("assets/uiskin.json", Skin.class);
        } else {
            // Phòng hờ
            skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        }
        setBackground("background/background.png");

        // Title
        Label titleLabel = new Label("SOLAR SYSTEM\nEXPLORER", skin);
        titleLabel.setFontScale(5f);Gdx.gl.glClearColor(0, 0, 0, 1);
        Table titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.top().left().padLeft(250).padTop(200);
        titleTable.add(titleLabel);

        stage.addActor(titleTable);

        // Buttons
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton optionButton = new TextButton("Option", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.left().bottom().padLeft(250).padBottom(100);
        table.add(startButton).width(400).height(150).pad(10).row();
        table.add(optionButton).width(400).height(150).pad(10).row();
        table.add(exitButton).width(400).height(150).pad(10);

        stage.addActor(table);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Screen nextScreen = new LoadingScreen(
                    game,
                    "ENTERING SOLAR SYSTEM...",
                    false,
                    () -> {
                        game.setScreen(((MainGame) game).solarSystemScreen);
                    }
                );

                setScreenWithFade(nextScreen);
            }
        });


        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
