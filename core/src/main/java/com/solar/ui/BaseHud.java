package com.solar.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseHud implements Disposable {
    public Stage stage;
    protected Viewport viewport;

    // Kích thước ảo cho UI (1280x720 là chuẩn phổ biến cho pixel art HD)
    protected static final float HUD_WIDTH = 1280;
    protected static final float HUD_HEIGHT = 720;

    public BaseHud(SpriteBatch batch) {
        viewport = new FitViewport(HUD_WIDTH, HUD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void draw() {
        // Áp dụng viewport của HUD trước khi vẽ để không bị lệch
        viewport.apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
