package com.solar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.solar.screen.LoadingScreen;
import com.solar.screen.MenuScreen;
import com.solar.screen.SolarSystemScreen;

public class MainGame extends Game {

    // ==================== CONSTANTS ====================
    // Remove stray spaces in resource paths and align to assets folder layout
    private static final String SKIN_PATH = "uiskin.json";
    private static final String TITLE_FONT_PATH = "titletext.ttf";
    private static final String BODY_FONT_PATH = "bodytext.ttf";
    private static final String ATLAS_PATH = "planets.atlas"; // Keep if you truly have this; otherwise consider removing
    private static final String PLANET_ATLAS_PATH = "planets.atlas"; // assets/planets.atlas
    private static final String PLAYER_ATLAS_PATH = "player.atlas";  // assets/player.atlas

    // Screen dimensions
    public static final float WORLD_WIDTH = 1920;
    public static final float WORLD_HEIGHT = 1200;

    // ==================== GRAPHICS ====================
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    // ==================== FONTS ====================
    public BitmapFont font;
    public BitmapFont titleFont;

    // ==================== ASSET MANAGEMENT ====================
    public AssetManager assetManager;
    private Skin skin;

    // ==================== SCREENS (lazy initialization) ====================
    private SolarSystemScreen solarSystemScreen;

    // ==================== GETTERS ====================
    public SpriteBatch getBatch() { return batch; }
    public ShapeRenderer getShapeRenderer() { return shapeRenderer; }
    public AssetManager getAssetManager() { return assetManager; }

    @Override
    public void create() {
        // Initialize graphics
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Initialize asset manager
        assetManager = new AssetManager();

        // Load and configure skin with custom fonts
        loadSkinWithFonts();

        // Queue assets for loading
        queueAssets();

        // Start with loading screen
        // Provide message and showProgressBar flag to match LoadingScreen(MainGame, String, boolean, Runnable)
        this.setScreen(new LoadingScreen(
            this,
            "LOADING...",
            true,
            () -> setScreen(new MenuScreen(MainGame.this))
        ));
    }

    /**
     * Load skin and configure custom fonts
     */
    private void loadSkinWithFonts() {
        // 1. Load base skin (contains button images, frames, etc.)
        skin = new Skin(Gdx.files.internal(SKIN_PATH));

        // 2. Generate custom fonts using FreeType
        try {
            // --- Title Font ---
            FreeTypeFontGenerator genTitle = new FreeTypeFontGenerator(Gdx.files.internal(TITLE_FONT_PATH));
            FreeTypeFontGenerator.FreeTypeFontParameter paramTitle = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramTitle.size = 32;
            paramTitle.magFilter = Texture.TextureFilter.Nearest;
            paramTitle.minFilter = Texture.TextureFilter.Nearest;
            titleFont = genTitle.generateFont(paramTitle);
            genTitle.dispose();

            // --- Body Font ---
            FreeTypeFontGenerator genBody = new FreeTypeFontGenerator(Gdx.files.internal(BODY_FONT_PATH));
            FreeTypeFontGenerator.FreeTypeFontParameter paramBody = new FreeTypeFontGenerator.FreeTypeFontParameter();
            paramBody.size = 48;
            paramBody.magFilter = Texture.TextureFilter.Linear;
            paramBody.minFilter = Texture.TextureFilter.Linear;
            font = genBody.generateFont(paramBody);
            genBody.dispose();

            // 3. Add fonts to skin
            skin.add("title-font", titleFont);
            skin.add("body-font", font);
            skin.add("default-font", titleFont); // For compatibility

            // 4. Configure Label styles
            Label.LabelStyle defaultLabelStyle = skin.get("default", Label.LabelStyle.class);
            defaultLabelStyle.font = titleFont;

            Label.LabelStyle bodyLabelStyle = new Label.LabelStyle(font, Color.WHITE);
            skin.add("body", bodyLabelStyle);

            // Title style
            Label.LabelStyle titleLabelStyle = new Label.LabelStyle(titleFont, Color.CYAN);
            skin.add("title", titleLabelStyle);

            // 5. Configure Button styles
            TextButton.TextButtonStyle defaultBtnStyle = skin.get("default", TextButton.TextButtonStyle.class);
            defaultBtnStyle.font = titleFont;
            defaultBtnStyle.fontColor = Color.WHITE;

            Gdx.app.log("MainGame", "Custom fonts loaded successfully");

        } catch (Exception e) {
            Gdx.app.error("MainGame", "Failed to load custom fonts, using defaults:  " + e.getMessage());

            // Fallback to default fonts
            font = new BitmapFont();
            font.getData().setScale(1.0f);
            titleFont = new BitmapFont();
            titleFont.getData().setScale(1.5f);
        }
    }

    /**
     * Queue assets for async loading
     */
    private void queueAssets() {
        try {
            // Load main atlas (optional, only if it exists)
            if (Gdx.files.internal(ATLAS_PATH).exists()) {
                assetManager.load(ATLAS_PATH, TextureAtlas.class);
            } else {
                // Log if missing to avoid runtime Internal file not found
                Gdx.app.log("MainGame", "Main atlas not found: " + ATLAS_PATH + ", skipping");
            }

            // Load planet atlas from assets root
            if (Gdx.files.internal(PLANET_ATLAS_PATH).exists()) {
                assetManager.load(PLANET_ATLAS_PATH, TextureAtlas.class);
            } else {
                Gdx.app.log("MainGame", "Planet atlas not found: " + PLANET_ATLAS_PATH);
            }

            // Load player atlas from assets root
            if (Gdx.files.internal(PLAYER_ATLAS_PATH).exists()) {
                assetManager.load(PLAYER_ATLAS_PATH, TextureAtlas.class);
            } else {
                Gdx.app.log("MainGame", "Player atlas not found: " + PLAYER_ATLAS_PATH);
            }

            Gdx.app.log("MainGame", "Assets queued for loading");

        } catch (Exception e) {
            Gdx.app.error("MainGame", "Error queuing assets: " + e.getMessage());
        }
    }

    /**
     * Get UI Skin
     */
    public Skin getSkin() {
        return skin;
    }

    /**
     * Get Solar System Screen (lazy initialization)
     */
    public SolarSystemScreen getSolarSystemScreen() {
        if (solarSystemScreen == null) {
            solarSystemScreen = new SolarSystemScreen(this);
        }
        return solarSystemScreen;
    }

    /**
     * Get Planet TextureAtlas from asset manager
     */
    public TextureAtlas getPlanetAtlas() {
        if (assetManager.isLoaded(PLANET_ATLAS_PATH)) {
            return assetManager.get(PLANET_ATLAS_PATH, TextureAtlas.class);
        }
        return null;
    }

    /**
     * Get Player TextureAtlas from asset manager
     */
    public TextureAtlas getPlayerAtlas() {
        if (assetManager.isLoaded(PLAYER_ATLAS_PATH)) {
            return assetManager.get(PLAYER_ATLAS_PATH, TextureAtlas.class);
        }
        return null;
    }

    /**
     * Get Main TextureAtlas from asset manager
     */
    public TextureAtlas getMainAtlas() {
        if (assetManager.isLoaded(ATLAS_PATH)) {
            return assetManager.get(ATLAS_PATH, TextureAtlas.class);
        }
        return null;
    }

    @Override
    public void render() {
        super.render();

        // --- FULLSCREEN TOGGLE (F11) ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 800);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        // Dispose graphics
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }

        // Dispose fonts
        if (font != null) {
            font.dispose();
            font = null;
        }
        if (titleFont != null) {
            titleFont.dispose();
            titleFont = null;
        }

        // Dispose screens
        if (solarSystemScreen != null) {
            solarSystemScreen.dispose();
            solarSystemScreen = null;
        }

        // Dispose asset manager (this disposes all loaded assets)
        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }

        // Dispose skin
        if (skin != null) {
            skin.dispose();
            skin = null;
        }

        Gdx.app.log("MainGame", "All resources disposed");
    }
}

