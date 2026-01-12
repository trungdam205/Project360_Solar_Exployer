package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.solar.MainGame;

// Main menu screen: shows title and navigation buttons
public class MenuScreen extends BaseScreen {

    private MainGame game;
    private Texture astronautTexture;

    public MenuScreen(MainGame game) {
        super(game);
        this.game = game;
    }
    @Override
    public void show() {
        super.show();
        setBackground("background/background.png");

        astronautTexture = new Texture(Gdx.files.internal("Astronaut.png"));
        Image astronautImage = new Image(astronautTexture);

        // Position astronaut (adjust as needed)
        Table astronautTable = new Table();
        astronautTable.setFillParent(true);
        astronautTable.right().bottom().pad(-200, 300, 100, 100);
        astronautTable.setTransform(true);
        astronautTable.add(astronautImage).size(972, 1188); // Adjust size
        astronautTable.setRotation(-3); // nghiêng trái 20 độ
        stage.addActor(astronautTable);

        // Title label
        Label titleLabel = new Label("SOLAR SYSTEM\nEXPLORER", skin);
        titleLabel.setFontScale(3.0f);
        titleLabel.setAlignment(Align.left);

        Table titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.top().left().padLeft(250).padTop(200);
        titleTable.add(titleLabel);
        stage.addActor(titleTable);

        // Main buttons
        TextButton startButton = new TextButton("START", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        Table table = new Table();
        table.setFillParent(true);
        table.left().bottom().padLeft(250).padBottom(350);
        table.add(startButton).width(400).height(150).pad(10).row();
        table.add(exitButton).width(400).height(150).pad(10);
        stage.addActor(table);

        // Button listeners
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SolarSystemScreen(game));
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        // BaseScreen.dispose() handles stage disposal
        super.dispose();
    }

}
