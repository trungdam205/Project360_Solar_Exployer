package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

    // Biến để tạo độ trễ giả (cho người dùng kịp nhìn thấy loading)
    private float stateTime = 0f;
    private static final float MIN_LOADING_TIME = 2.0f; // Load ít nhất 2 giây mới cho qua

    public LoadingScreen(MainGame game, Runnable onLoadingFinished) {
        super(game);
        this.onLoadingFinished = onLoadingFinished;

        // --- BƯỚC 1: LOAD SKIN TRƯỚC (ĐỒNG BỘ) ---
        // Vấn đề "Con gà quả trứng": Muốn hiện thanh loading đẹp (cần Skin)
        // thì phải có Skin trước khi load các thứ khác.
        // Vì Skin rất nhẹ, ta load cứng luôn để dùng ngay.
        if (!game.assetManager.isLoaded("assets/uiskin.json")) {
            game.assetManager.load("assets/uiskin.json", Skin.class);
            game.assetManager.finishLoading(); // Chặn lại 1 chút để load xong skin ngay lập tức
        }
        // Lấy skin ra để dùng cho UI loading
        this.skin = game.assetManager.get("assets/uiskin.json", Skin.class);

        // --- BƯỚC 2: SETUP UI ---
        setupUI();

        // --- BƯỚC 3: XẾP HÀNG CÁC ASSET NẶNG CẦN LOAD ---
        // Ví dụ load Atlas (ảnh game)
        if (!game.assetManager.isLoaded("images/assets.atlas")) {
            game.assetManager.load("images/assets.atlas", TextureAtlas.class);
        }

        // (Sau này có nhạc, map thì load tiếp ở đây)
        // game.assetManager.load("music/bgm.mp3", Music.class);
    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);

        // 1. Label chữ "LOADING..."
        Label titleLabel = new Label("LOADING...", skin);
        titleLabel.setFontScale(2f);
        titleLabel.setAlignment(Align.center);

        // 2. Thanh ProgressBar
        // Tham số: min, max, step, vertical?, skinStyleName
        progressBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
        progressBar.setValue(0);
        progressBar.setAnimateDuration(0.25f); // Hiệu ứng chạy mượt

        // 3. Label hiển thị % (0%)
        progressLabel = new Label("0%", skin);

        // Add vào table
        table.add(titleLabel).padBottom(20).row();
        table.add(progressBar).width(400).height(30).padBottom(10).row();
        table.add(progressLabel);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Xóa màn hình (Màu đen)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật thời gian đã trôi qua
        stateTime += delta;

        // --- UPDATE ASSET MANAGER ---
        // Hàm update() trả về true nếu đã load xong 100%
        boolean isLoaded = game.assetManager.update();

        // Lấy tiến độ thực tế (0.0 -> 1.0)
        float progress = game.assetManager.getProgress();

        // --- LOGIC TRỄ GIẢ (QUAN TRỌNG) ---
        // Nếu load xong asset NHƯNG chưa đủ 2 giây -> Giữ nguyên ở 100% đợi hết giờ
        // Nếu chưa load xong -> Hiển thị progress thật
        if (isLoaded) {
            progressBar.setValue(1f);
            progressLabel.setText("100%");
        } else {
            progressBar.setValue(progress);
            progressLabel.setText((int)(progress * 100) + "%");
        }

        // --- ĐIỀU KIỆN CHUYỂN MÀN ---
        // 1. Asset phải load xong (isLoaded == true)
        // 2. Thời gian chạy phải lớn hơn MIN_LOADING_TIME (để kịp nhìn)
        // 3. (Tùy chọn) Người dùng bấm phím bất kỳ (như "Press any key")
        if (isLoaded && stateTime >= MIN_LOADING_TIME) {
            if (onLoadingFinished != null) {
                onLoadingFinished.run();
            }
        }

        // Vẽ UI
        stage.act(delta);
        stage.draw();
    }
}
