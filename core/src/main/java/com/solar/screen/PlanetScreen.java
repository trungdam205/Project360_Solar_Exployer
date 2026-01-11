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

        // Tắt cảm ứng của background để không che mất input của game
        if (bgImage != null) {
            bgImage.setTouchable(Touchable.disabled);
        }

        // --- 2. SETUP NÚT BACK ---
        addBackButton(() -> {
            // Chuyển về màn hình hệ mặt trời
            game.setScreen(new SolarSystemScreen(game));
        });

        // --- 3. SETUP HUD ---
        // FIX LỖI FONT: Lấy "body-font" từ Skin (size 20) thay vì game.font (null)
        // Font này phù hợp để hiển thị các thông số kỹ thuật dài
        hud = new GameHud(game.batch, game.skin.getFont("body-font"));

        // Cập nhật thông tin lên HUD nếu dữ liệu tồn tại
        if (data != null) {
            // Các biến này khớp với thứ tự trong PlanetDatabase bạn cung cấp:
            // Gravity (float), Weather, Atmosphere, Surface, Resource
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

        // --- 4. XỬ LÝ INPUT MULTIPLEXER ---
        // Kết hợp Input của HUD (để kéo/xem thông tin) và Stage (để bấm nút Back)
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

        // Vẽ lớp trên: HUD thông số
        if (hud != null) {
            hud.draw();
        }
    }

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
