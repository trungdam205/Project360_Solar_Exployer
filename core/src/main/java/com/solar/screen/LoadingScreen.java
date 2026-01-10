package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.solar.MainGame;

public class LoadingScreen extends BaseScreen {

    private Runnable onLoadingFinished;
    private ProgressBar progressBar;
    private Label progressLabel;
    private String loadingMessage;

    private boolean showProgressBar;

    // Biến để tạo độ trễ giả (cho người dùng kịp nhìn thấy loading)
    private float stateTime = 0f;

    private static final float LOADING_DURATION = 2.5f; // Load ít nhất 2 giây mới cho qua

    public LoadingScreen(MainGame game, String message, boolean showProgressBar, Runnable onLoadingFinished) {
        super(game);
        setBackground("background/background.png");
        this.loadingMessage = message;
        this.showProgressBar = showProgressBar;
        this.onLoadingFinished = onLoadingFinished;


        if (!game.assetManager.isLoaded("assets/uiskin.json")) {
            game.assetManager.load("assets/uiskin.json", Skin.class);
            game.assetManager.finishLoading(); // Chặn lại 1 chút để load xong skin ngay lập tức
        }
        // Lấy skin ra để dùng cho UI loading
        this.skin = game.assetManager.get("assets/uiskin.json", Skin.class);


        setupUI();

        //  XẾP HÀNG CÁC ASSET NẶNG CẦN LOAD ---
        //
        if (!game.assetManager.isLoaded("images/assets.atlas")) {
            game.assetManager.load("images/assets.atlas", TextureAtlas.class);
        }


    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label(loadingMessage, skin);
        titleLabel.setFontScale(2f);
        titleLabel.setAlignment(Align.center);

        if (!showProgressBar) {
            titleLabel.addAction(Actions.forever(
                Actions.sequence(
                    Actions.fadeOut(0.9f),
                    Actions.fadeIn(0.9f)
                )
            ));
        }

        table.add(titleLabel).padBottom(30).row();

        // 2. ProgressBar & % Label (CHỈ HIỆN KHI showProgressBar = TRUE)
        if (showProgressBar) {
            progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
            progressBar.setAnimateDuration(0.1f);

            progressLabel = new Label("0%", skin);

            table.add(progressBar).width(400).height(30).padBottom(10).row();
            table.add(progressLabel);
        }

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Xóa màn hình (Màu đen)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật thời gian đã trôi qua
        stateTime += delta;

        // Kiểm tra xem AssetManager đã tải xong thật chưa
        boolean assetLoaded = game.assetManager.update();

        // --- LOGIC LÀM CHẬM THANH LOADING ---
        float visualProgress = stateTime / LOADING_DURATION;
        float currentDisplayValue = Math.min(visualProgress, 1.0f);

        // =================================================================
        // [FIX] CHỈ CẬP NHẬT PROGRESS BAR NẾU NÓ TỒN TẠI (showProgressBar = true)
        // =================================================================
        if (showProgressBar && progressBar != null && progressLabel != null) {
            progressBar.setValue(currentDisplayValue);
            progressLabel.setText((int)(currentDisplayValue * 100) + "%");
        }
        // =================================================================

        // CHỈ CHUYỂN MÀN KHI:
        // 1. Asset thật đã tải xong
        // 2. Thanh loading đã chạy đủ thời gian (visualProgress >= 1)
        if (assetLoaded && visualProgress >= 1.0f) {
            if (onLoadingFinished != null) {
                onLoadingFinished.run();
            }
        }

        stage.act(delta);
        stage.draw();
    }
}
