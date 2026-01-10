package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.solar.MainGame;
import com.solar.data.*;
import com.solar.ui.GameHud;
import com.solar.actor.PlayerActor;
import com.solar.util.GravityScaler;

public class PlanetScreen extends BaseScreen {

    private PlanetType planet;
    private PlanetData data;

    private Image backgroundImage;
    private GameHud hud;
    private PlayerActor player;

    public PlanetScreen(MainGame game, PlanetType planet) {
        super(game);
        this.planet = planet;
        this.data = PlanetDatabase.get(planet);

        addBackButton(() ->
            game.setScreen(new SolarSystemScreen(game))
        );
        // 1. Tạo HUD
        hud = new GameHud(game.batch);

        // 2. CẤU HÌNH INPUT MULTIPLEXER (QUAN TRỌNG)
        // Tạo bộ gộp input
        InputMultiplexer multiplexer = new InputMultiplexer();

        // Ưu tiên xử lý HUD trước (nếu HUD có nút bấm inventory),
        // sau đó đến Stage của màn hình chính (chứa nút Back)
        multiplexer.addProcessor(hud.stage);  // Ưu tiên 1: HUD
        multiplexer.addProcessor(this.stage); // Ưu tiên 2: Màn hình chính (Nút Back)

        // Set bộ gộp này làm bộ xử lý chính
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        System.out.println("Entered: " + data.displayName);

        Texture bgTexture = new Texture(Gdx.files.internal("background/background.png"));
        backgroundImage = new Image(bgTexture);
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(backgroundImage);
        backgroundImage.toBack();

        spawnPlayer();
    }

    private void spawnPlayer() {
        float gravity = GravityScaler.scale(data.gravity);

        player = new PlayerActor(gravity);

        float centerX = stage.getViewport().getWorldWidth() / 2f;
        float spawnY  = stage.getViewport().getWorldHeight() * 0.35f;

        player.setPosition(centerX - player.getWidth() / 2f, spawnY);
        stage.addActor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage != null) {
            stage.getViewport().apply(); // Đảm bảo viewport của nút Back đúng
            stage.act(delta);
            stage.draw();
        }
        if (hud != null) {
            hud.draw(); // Trong này đã có stage.act() và stage.draw() của HUD rồi
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height); // Quan trọng!
    }

    @Override
    public void dispose() {
        super.dispose();
        hud.dispose();
    }
}
