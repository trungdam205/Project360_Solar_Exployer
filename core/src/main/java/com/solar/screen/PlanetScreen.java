package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.solar.MainGame;
import com.solar.data.PlanetData;
import com.solar.data.PlanetDatabase;
import com.solar.data.PlanetType;
import com.solar.ui.GameHud;

public class PlanetScreen extends BaseScreen {

    private PlanetType planet;
    private PlanetData data;
    private GameHud hud;

    public PlanetScreen(MainGame game, PlanetType planet) {
        super(game);
        this.planet = planet;

        // Lấy dữ liệu hành tinh từ Database
        this.data = PlanetDatabase.get(planet);

        // --- 1. SETUP BACKGROUND ---
        // Sử dụng hàm có sẵn của BaseScreen
        setBackground("background/background.png");

        // Disable background touch so underlying game input is not blocked
        if (bgImage != null) {
            bgImage.setTouchable(Touchable.disabled);
        }

        // --- 2. SETUP NÚT BACK ---
        addBackButton(() -> {
            // Chuyển về màn hình hệ mặt trời
            game.setScreen(new SolarSystemScreen(game));
        });

        // 3. Setup HUD: create with a reasonable font scale
        hud = new GameHud(game.getBatch(), game.getSkin().getFont("body-font"), 0.8f);

        // Cập nhật thông tin lên HUD nếu dữ liệu tồn tại
        if (data != null) {
            hud.updateInfo(
                data.gravity,
                data.weather,
                data.atmosphere,
                data.surfaceType,
                data.primaryRes
            );
        }
    }

    @Override
    public void show() {
        super.show(); // Init InputProcessor cơ bản của stage

        if (data != null) {
            System.out.println("Entered: " + data.displayName);
        }

        // 4. Setup input multiplexer: HUD input first, then stage
        InputMultiplexer multiplexer = new InputMultiplexer();

        if (hud != null) {
            multiplexer.addProcessor(hud.stage);  // Ưu tiên 1: HUD
        }
        multiplexer.addProcessor(this.stage);     // Ưu tiên 2: Nút Back

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Vẽ lớp dưới: Background + Nút Back
        if (stage != null) {
            stage.getViewport().apply();
            stage.act(delta);
            stage.draw();
        }

        // Draw HUD on top
        if (hud != null) {
            hud.draw();
        }
    }

    // Handle resize events, ensure HUD also resizes
    @Override
    public void resize(int width, int height) {
        super.resize(width, height); // BaseScreen tự xử lý resize background

        if (hud != null) {
            hud.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (hud != null) hud.dispose();
    }
}
