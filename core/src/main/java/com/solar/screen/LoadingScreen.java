package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils; // Import toán học
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.solar.MainGame;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class LoadingScreen extends BaseScreen {

    private Runnable onLoadingFinished;
    private ProgressBar progressBar;
    private Label progressLabel;

    private float stateTime = 0f;
    private static final float MIN_LOADING_TIME = 2.0f;

    public LoadingScreen(MainGame game, Runnable onLoadingFinished) {
        super(game);
        this.onLoadingFinished = onLoadingFinished;

        // 1. SETUP BACKGROUND (Dùng hàm có sẵn của BaseScreen)
        // Đảm bảo file ảnh tồn tại: assets/background/background.png
        setBackground("background/background.png");

        // Load Skin đồng bộ
        if (!game.assetManager.isLoaded("assets/uiskin.json")) {
            game.assetManager.load("assets/uiskin.json", Skin.class);
            game.assetManager.finishLoading();
        }
        this.skin = game.skin;

        setupUI();

        // Xếp hàng asset nặng
        if (!game.assetManager.isLoaded("images/assets.atlas")) {
            game.assetManager.load("images/assets.atlas", TextureAtlas.class);
        }
    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);

        // TỰ ĐỘNG DÙNG TITLE FONT (Do đã config style "default" ở MainGame)
        Label titleLabel = new Label("LOADING...", skin);
        titleLabel.setAlignment(Align.center);

        progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
        progressBar.setValue(0);
        progressBar.setAnimateDuration(0.0f);

        progressLabel = new Label("0%", skin); // Tự động dùng font Title

        table.add(titleLabel).padBottom(20).row();
        table.add(progressBar).width(400).height(30).padBottom(10).row();
        table.add(progressLabel);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Xóa màn hình
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;

        // Update AssetManager
        boolean isLoaded = game.assetManager.update();

        // --- LOGIC SMOOTH BAR ---
        // 1. Tiến độ theo thời gian (0 -> 1 trong 2 giây)
        float timeProgress = stateTime / MIN_LOADING_TIME;

        // 2. Tiến độ load thật (0 -> 1)
        float realProgress = game.assetManager.getProgress();

        // 3. Lấy số NHỎ HƠN để đảm bảo bar không chạy quá nhanh
        float visualProgress = Math.min(timeProgress, realProgress);

        // Giới hạn max là 1
        visualProgress = MathUtils.clamp(visualProgress, 0f, 1f);

        // Cập nhật UI
        progressBar.setValue(visualProgress);
        progressLabel.setText((int)(visualProgress * 100) + "%");

        // --- ĐIỀU KIỆN CHUYỂN MÀN (KHÔNG FADE) ---
        if (isLoaded && stateTime >= MIN_LOADING_TIME) {
            if (onLoadingFinished != null) {
                // Gọi trực tiếp Runnable để chuyển màn ngay lập tức
                onLoadingFinished.run();
            }
        }

        stage.act(delta);
        stage.draw();
    }
}
