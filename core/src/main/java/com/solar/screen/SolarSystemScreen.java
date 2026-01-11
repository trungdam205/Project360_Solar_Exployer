package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.solar.MainGame;
import com.solar.actor.PlanetActor;
import com.solar.data.PlanetData;
import com.solar.data.PlanetDatabase;
public class SolarSystemScreen extends BaseScreen {

    private Group solarGroup;
    private Label planetNameLabel;
    private TextureAtlas atlas;
    private boolean shouldDisposeAtlas = false;

    // Constructor: set background and build UI + solar system
    public SolarSystemScreen(MainGame game) {
        super(game);
        setBackground("background/background.png");

        createSolarSystem();
        createUI();
    }

    // Build top-level UI (title and back button)
    private void createUI() {
        // Nút Back tự động dùng font xịn từ BaseScreen
        addBackButton(() -> game.setScreen(new MenuScreen(game)));

        // --- SỬA ĐỔI: Dùng skin thay vì game.font ---
        // Vì MainGame đã set font mặc định cho skin là TitleFont rồi,
        // nên chỉ cần new Label(text, skin) là xong.
        Label titleLabel = new Label("SOLAR SYSTEM", skin);
        titleLabel.setFontScale(4f);
        titleLabel.setAlignment(Align.left);

        Table titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.top();
        titleTable.add(titleLabel).padTop(50);
        stage.addActor(titleTable);
    }

    // Create planet actors and add them to a group
    private void createSolarSystem() {
        solarGroup = new Group();
        float centerX = stage.getViewport().getWorldWidth() / 2;
        float centerY = stage.getViewport().getWorldHeight() / 2;

        if (game.getAssetManager().isLoaded("images/assets.atlas")) {
            atlas = game.getAssetManager().get("images/assets.atlas", TextureAtlas.class);
            shouldDisposeAtlas = false;
        } else {
            atlas = new TextureAtlas(Gdx.files.internal("images/assets.atlas"));
            shouldDisposeAtlas = true;
        }

        for (final PlanetData data : PlanetDatabase.getAllPlanets()) {
            TextureRegion planetRegion = atlas.findRegion(data.texturePath);

            if (planetRegion == null) {
                Gdx.app.error("SolarSystem", "Không tìm thấy ảnh: " + data.texturePath);
                continue;
            }

            PlanetActor p = new PlanetActor(data, planetRegion, () -> {
                if (data.canEnter) {
                    game.setScreen(new PlanetScreen(game, data.type));
                }
            });

            // Position actor relative to viewport center
            float drawX = centerX + data.x - (data.size / 2);
            float drawY = centerY + data.y - (data.size / 2);
            p.setPosition(drawX, drawY);

            // Hover listener: show name and scale up on enter, reset on exit
            p.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                    planetNameLabel.setText(data.displayName);
                    planetNameLabel.setVisible(true);
                    p.setScale(1.1f);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                    planetNameLabel.setText("");
                    planetNameLabel.setVisible(false);
                    p.setScale(1.0f);
                }
            });
            solarGroup.addActor(p);
        }
        createNameLabel();
        stage.addActor(solarGroup);
    }

    // Create the floating label used to display planet names on hover
    private void createNameLabel() {
        // --- SỬA ĐỔI: Dùng skin luôn ---
        planetNameLabel = new Label("", skin);
        planetNameLabel.setColor(1, 1, 0, 1); // yellow color
        planetNameLabel.setFontScale(1.5f);
        planetNameLabel.setAlignment(Align.center);
        stage.addActor(planetNameLabel);
    }

    // ===========BỎ ĐI HOẶC THAY CODE CỦA TRUNG==================
    // Helper to enter a planet (loads assets then switches screen)
    public void enterPlanet(PlanetData data) {
        // Optionally load a tiled map here, then switch screens via LoadingScreen
        game.setScreen(new LoadingScreen(game, () -> game.setScreen(new PlanetScreen(game, data.type))));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        // Debug: show mouse coordinates in window title
        Vector2 mouseLoc = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Gdx.graphics.setTitle("X: " + (int)mouseLoc.x + " | Y: " + (int)mouseLoc.y);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (planetNameLabel != null) {
            float labelX = (stage.getViewport().getWorldWidth() - planetNameLabel.getWidth()) / 2;
            float labelY = 50;
            planetNameLabel.setPosition(labelX, labelY);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Dispose atlas only if this screen created it
        if (shouldDisposeAtlas && atlas != null) {
            atlas.dispose();
        }
    }
}
