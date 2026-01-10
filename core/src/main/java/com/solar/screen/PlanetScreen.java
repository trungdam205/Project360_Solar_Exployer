package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.solar.MainGame;
import com.solar.data.*;
import com.solar.ui.GameHud;

public class PlanetScreen extends BaseScreen {

    private PlanetType planet;
    private PlanetData data;

    private GameHud hud;

    public PlanetScreen(MainGame game, PlanetType planet) {
        super(game);
        this.planet = planet;
        this.data = PlanetDatabase.get(planet);

        addBackButton(() ->
            setScreenWithFade(new SolarSystemScreen(game))
        );
        // 1. Tạo HUD
        hud = new GameHud(game.batch);

        if (data != null) {
            hud.updateInfo(
                data.gravity,
                data.weather,
                data.atmosphere,
                data.surfaceType,
                data.primaryRes
            );
        }
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
        super.show();
        System.out.println("Entered: " + data.displayName);
    }

    @Override
    public void render(float delta) {
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
