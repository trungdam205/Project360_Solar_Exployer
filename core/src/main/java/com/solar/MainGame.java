package com.solar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.solar.screen.LoadingScreen;
import com.solar.screen.MenuScreen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Texture; // <--- MỚI THÊM
import com.badlogic.gdx.graphics.g2d.BitmapFont; // <--- MỚI THÊM
public class MainGame extends Game {
    private static final String SKIN_PATH = "uiskin.json";
    private static final String TITLE_FONT_PATH = "titletext.ttf";
    private static final String BODY_FONT_PATH = "bodytext.ttf";
    private static final String ATLAS_PATH = "images/assets.atlas";

    private SpriteBatch batch;
    private AssetManager assetManager;
    private Skin skin;

    public SpriteBatch getBatch() { return batch; }
    public AssetManager getAssetManager() { return assetManager; }
    public Skin getSkin() { return skin; }

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        // 1. Load Skin gốc (chứa ảnh nút, khung...)
        skin = new Skin(Gdx.files.internal(SKIN_PATH));

        // 2. KHỞI TẠO 2 FONT
        // --- Font Title (Dùng cho tất cả màn hình hiện tại) ---
        FreeTypeFontGenerator genTitle = new FreeTypeFontGenerator(Gdx.files.internal(TITLE_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter paramTitle = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitle.size = 32; // Size to
        paramTitle.magFilter = Texture.TextureFilter.Nearest;
        paramTitle.minFilter = Texture.TextureFilter.Nearest;
        BitmapFont fontTitle = genTitle.generateFont(paramTitle);
        genTitle.dispose();

        // --- Font Body (Khởi tạo sẵn để dành) ---
        FreeTypeFontGenerator genBody = new FreeTypeFontGenerator(Gdx.files.internal(BODY_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter paramBody = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramBody.size = 48; // Size lớn hơn để chữ nét khi phóng to
        paramBody.magFilter = Texture.TextureFilter.Linear;
        paramBody.minFilter = Texture.TextureFilter.Linear;
        BitmapFont fontBody = genBody.generateFont(paramBody);
        genBody.dispose();

        // 3. NHÉT VÀO SKIN & CẤU HÌNH
        // Lưu font vào skin để sau này cần thì lấy: skin.getFont("title")
        skin.add("title-font", fontTitle);
        skin.add("body-font", fontBody);

        // --- CẤU HÌNH LABEL ---
        // Ghi đè style "default" -> Dùng Title Font (Theo yêu cầu của bạn)
        Label.LabelStyle defaultLabelStyle = skin.get("default", Label.LabelStyle.class);
        defaultLabelStyle.font = fontTitle;

        // Tạo thêm style "body" -> Dùng Body Font (Để dành)
        Label.LabelStyle bodyLabelStyle = new Label.LabelStyle(fontBody, Color.WHITE);
        skin.add("body", bodyLabelStyle);

        // --- CẤU HÌNH BUTTON ---
        // Ghi đè style nút bấm mặc định -> Dùng Title Font
        TextButton.TextButtonStyle defaultBtnStyle = skin.get("default", TextButton.TextButtonStyle.class);
        defaultBtnStyle.font = fontTitle;
        defaultBtnStyle.fontColor = Color.WHITE;

        // Load Atlas (như cũ)
        assetManager.load(ATLAS_PATH, TextureAtlas.class);

        // Vào Loading
        this.setScreen(new LoadingScreen(this, () -> setScreen(new MenuScreen(MainGame.this))));
    }

    @Override
    public void render() {
        super.render();

        // --- LOGIC FULLSCREEN ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            // Nếu đang là Fullscreen -> Chuyển về Cửa sổ
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 800);
            }
            // Nếu đang là Cửa sổ -> Chuyển sang Fullscreen
            else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch != null) batch.dispose();
        if (assetManager != null) assetManager.dispose();
        if (skin != null) skin.dispose();
    }
}
