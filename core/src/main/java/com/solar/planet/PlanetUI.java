package com.solar.planet;

import com.badlogic.gdx. Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d. InputEvent;
import com.badlogic. gdx.scenes. scene2d.Stage;
import com. badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d. utils. ClickListener;
import com.badlogic.gdx.utils.Align;
import com.solar.data.CelestialData;

/**
 * UI management for planet exploration screen
 */
public class PlanetUI {

    private final Stage stage;
    private final Skin skin;
    private final CelestialData planet;
    private final PlanetPhysics physics;
    private final float worldWidth;
    private final float worldHeight;

    // Callbacks
    private Runnable onBack;
    private Runnable onToggleGravity;
    private Runnable onRetry;

    // UI Elements
    private Label gravityStatusLabel;
    private TextButton gravityButton;
    private Label hintLabel;
    private Window victoryWindow;
    private Window deathWindow;
    private boolean gravityButtonClicked = false;

    public PlanetUI(Stage stage, Skin skin, CelestialData planet, PlanetPhysics physics,
                    float worldWidth, float worldHeight) {
        this.stage = stage;
        this.skin = skin;
        this.planet = planet;
        this.physics = physics;
        this. worldWidth = worldWidth;
        this. worldHeight = worldHeight;
    }

    public void init(Runnable onBack, Runnable onToggleGravity, Runnable onRetry) {
        this.onBack = onBack;
        this.onToggleGravity = onToggleGravity;
        this.onRetry = onRetry;

        stage.clear();
        createTopBar();
        createGravityPanel();
        createControlsHint();
        createVictoryWindow();
        createDeathWindow();
    }

    private void createTopBar() {
        Table topBar = new Table();
        topBar. setFillParent(true);
        topBar.top().pad(20);

        Label titleLabel = new Label(planet.name.toUpperCase()+ " SURFACE", skin);
        titleLabel. setFontScale(2.0f);
        titleLabel.setColor(Color. WHITE);
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onBack != null) onBack.run();
            }
        });

        topBar.add(titleLabel).expandX().left().padLeft(20).padTop(10);
        topBar.add(backButton).width(120).height(50).right();
        stage.addActor(topBar);
    }

    private void createGravityPanel() {
        // Góc trái dưới - chỉ có nút gravity
        Table panel = new Table();
        panel.setFillParent(true);
        panel.bottom().left().pad(20);

        gravityStatusLabel = new Label("Gravity: Earth (1.00g)", skin);
        gravityStatusLabel.setFontScale(1.1f);
        gravityStatusLabel.setColor(Color.WHITE);

        gravityButton = new TextButton("Try " + planet.name + "'s Gravity!", skin);
        gravityButton.getLabel().setColor(Color.WHITE);
        gravityButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onToggleGravity != null) {
                    onToggleGravity.run();
                    gravityButtonClicked = !gravityButtonClicked;
                    gravityButton.getLabel().setColor(gravityButtonClicked ? Color.BLACK : Color.WHITE);
                }
            }
        });

        // Cố định width để button không thay đổi kích cỡ khi text thay đổi
        panel.add(gravityButton).width(360).height(55);
        stage.addActor(panel);

        // Dòng chữ hint di chuyển ra giữa màn hình, phía trên hướng dẫn keyboard
        createHintLabel();
    }

    private void createHintLabel() {
        Table hintPanel = new Table();
        hintPanel.setFillParent(true);
        hintPanel.top().left().padLeft(20).padTop(80); // Đặt phía trên hướng dẫn keyboard

        Table bg = new Table();
        bg.setBackground(skin.newDrawable("white", new Color()));
        bg.pad(12, 25, 12, 25);

        hintLabel = new Label(getHintText(), skin);
        hintLabel.setFontScale(1.0f);
        hintLabel.setColor(Color.LIGHT_GRAY); // Đổi từ màu vàng sang trắng
        hintLabel.setWrap(true);

        bg.add(hintLabel).width(800);
        hintPanel.add(bg);
        stage.addActor(hintPanel);
    }

    private void createControlsHint() {
        Table hint = new Table();
        hint.setFillParent(true);
        hint.bottom().padBottom(20);

        Table bg = new Table();
        bg.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.6f)));
        bg.pad(10, 20, 10, 20);

        Label label = new Label("Arrows/WASD: Move | SPACE: Jump | G: Toggle Gravity", skin);
        label.setFontScale(1.0f);
        label.setColor(1f, 1f, 1f, 0.85f);
        bg.add(label);

        hint.add(bg);
        stage.addActor(hint);
    }

    private void createVictoryWindow() {
        victoryWindow = new Window("", skin);
        victoryWindow.setSize(1000, 640);
        victoryWindow.setPosition(worldWidth / 2 - victoryWindow.getWidth() / 2, worldHeight / 2 - victoryWindow.getHeight() / 2);
        victoryWindow.setVisible(false);
        victoryWindow.setMovable(false);

        Table content = new Table();

        Label title = new Label("MISSION COMPLETE!", skin);
        title.setFontScale(2.2f);
        title.setColor(Color.GOLD);

        Label message = new Label("You mastered " + planet.name + "'s gravity!", skin);
        message.setFontScale(1.5f);

        Label fact = new Label(Planet_Fact(), skin, "body");
        fact.setWrap(true);
        fact.setFontScale(1.3f);
        fact.setColor(Color.WHITE);

        TextButton continueBtn = new TextButton("Back To Space", skin);
        continueBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onBack != null) onBack.run();
            }
        });

        TextButton retryBtn = new TextButton("Play Again", skin);
        retryBtn. addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onRetry != null) onRetry.run();
            }
        });

        content.add(title).colspan(2).padBottom(12).row();
        content.add(message).colspan(2).padBottom(10).row();
        content.add(fact).colspan(2).width(800).padBottom(18).row();
        content.add(retryBtn).width(200).height(50).padRight(15);
        content.add(continueBtn).width(250).height(50);

        victoryWindow.add(content).expand().fill().pad(18);
        stage.addActor(victoryWindow);
    }

    private void createDeathWindow() {
        deathWindow = new Window("", skin);
        deathWindow.setSize(480, 300);
        deathWindow.setPosition(worldWidth / 2 - 240, worldHeight / 2 - 125);
        deathWindow.setVisible(false);
        deathWindow.setMovable(false);

        Table content = new Table();

        Label title = new Label("OUCH!", skin);
        title.setFontScale(2.0f);
        title.setColor(Color.RED);

        Label message = new Label(getDeathMessage(), skin);
        message.setWrap(true);
        message.setFontScale(1.15f);

        TextButton retryBtn = new TextButton("Try Again", skin);
        retryBtn. addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onRetry != null) onRetry.run();
            }
        });

        content.add(title).padBottom(12).row();
        content.add(message).width(430).padBottom(18).row();
        content.add(retryBtn).width(180).height(50);

        deathWindow.add(content).expand().fill().pad(18);
        stage.addActor(deathWindow);
    }

    // ==================== State Management ====================

    /**
     * Update gravity display after physics toggle
     * Called from PlanetScreen after physics.toggleGravity()
     */
    public void updateGravityDisplay() {
        try {
            if (physics == null) {
                Gdx.app.error("PlanetUI", "Physics is null!");
                return;
            }

            if (gravityButton == null) {
                Gdx.app.error("PlanetUI", "UI elements not initialized!");
                return;
            }

            // Update button text (width cố định nên text thay đổi không ảnh hưởng kích cỡ)
            if (physics.isPlanetGravityEnabled()) {
                gravityButton.setText("Use Earth's Gravity");
            } else {
                gravityButton.setText("Try " + planet.name + "'s Gravity!");
            }

            // Update hint label
            if (hintLabel != null) {
                hintLabel.setText(getHintText());
            }

            Gdx.app.log("PlanetUI", "Gravity display updated");

        } catch (Exception e) {
            Gdx.app.error("PlanetUI", "Error updating gravity display: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showVictory() {
        if (victoryWindow != null) {
            victoryWindow.setVisible(true);
        }
    }

    public void showDeath() {
        if (deathWindow != null) {
            deathWindow.setVisible(true);
        }
    }

    public void hideWindows() {
        if (victoryWindow != null) {
            victoryWindow. setVisible(false);
        }
        if (deathWindow != null) {
            deathWindow. setVisible(false);
        }
    }

    public void resetGravityUI() {
        try {
            if (gravityButton != null) {
                gravityButton.setText("Try " + planet.name + "'s Gravity!");
                gravityButton.setColor(Color.WHITE);
            }
            gravityButtonClicked = false;
            if (hintLabel != null) {
                hintLabel.setText(getHintText());
            }
            hideWindows();
        } catch (Exception e) {
            Gdx.app.error("PlanetUI", "Error resetting gravity UI: " + e.getMessage());
        }
    }

    // ==================== Text Helpers ====================

    private String getHintText() {
        if (physics == null) return "Jump over obstacles to reach the goal!";

        switch (physics.getPlanetType()) {
            case LOW_GRAVITY:
                return "Can't jump over the wall? Try " + planet.name + "'s gravity!";
            case HIGH_GRAVITY:
                return "Can't avoid the spikes? Try " + planet. name + "'s gravity!";
            default:
                return "Jump over obstacles to reach the goal!";
        }
    }

    private String getDeathMessage() {
        if (physics == null) return "Oops! Try again!";

        switch (physics.getPlanetType()) {
            case LOW_GRAVITY:
                return "Wall too high!  Enable " + planet.name + "'s gravity to jump HIGHER!";
            case HIGH_GRAVITY:
                return "Hit the spikes! Enable " + planet.name + "'s gravity to jump LOWER!";
            default:
                return "Oops!  Try again!";
        }
    }

    private String Planet_Fact() {
        switch (planet.id) {
            case "mercury":
                return "Temperature: 427 to -173 Celcius degree.\n" +
                    "Weather: No atmosphere, no wind or rain.\n" +
                    "Rocks: Hard silicate rocks, heavily cratered.\n" +
                    "Gravity: 0.38x Earth. Jump 3x higher!\n" +
                    "Fun Fact: Mercury is closest to Sun but not the hottest.";

            case "venus":
                return "Temperature: Hottest planet (~464 Celcius degree).\n" +
                    "Weather: Thick clouds, sulfuric acid rain.\n" +
                    "Rocks: Volcanic basalt, active volcanoes.\n" +
                    "Gravity: 0.91x Earth (almost same as Earth).\n" +
                    "Fun Fact: Sun rises in the West on Venus.";

            case "moon":
                return "Temperature: -173 to 127 Celcius degree.\n" +
                    "Weather: Silent, no wind, no atmosphere.\n" +
                    "Rocks: Covered in regolith (rocky dust).\n" +
                    "Gravity: Just 1/6 Earth's gravity, you can lift heavy rocks easily.\n" +
                    "Fun Fact: Footprints from 50 years ago still stay preserved.";

            case "mars":
                return "Temperature: Average -62 Celcius degree. Red dust storms.\n" +
                    "Weather: CO2 snow falls occasionally.\n" +
                    "Rocks: Rusted iron-bearing basalt (Red dust).\n" +
                    "Gravity: 0.38x Earth. Throw balls 3x farther.\n" +
                    "Fun Fact: Features blue sunsets unlike Earth.";
            case "jupiter":
                return "Temperature: -145 Celcius degree in upper clouds.\n" +
                    "Weather: Violent storms. Great Red Spot raging 300+ yrs.\n" +
                    "Structure: Massive gas giant.\n" +
                    "Gravity: 2.53x Earth. 50kg feels like 126kg.\n" +
                    "Fun Fact: Deflects asteroids, protecting Earth.";

            case "saturn":
                return "Temperature: Average -178 Celcius degree.\n" +
                    "Weather: Winds faster than jets. Hexagon storm at North Pole.\n" +
                    "Gravity: 1.07x Earth (slightly heavier).\n" +
                    "Fun Fact: Low density - would float on water!";

            case "uranus":
                return "Temperature: Coldest planet (-224 Celcius degree).\n" +
                    "Weather: Methane atmosphere (pale blue), cold winds.\n" +
                    "Rocks: No solid surface. Compressed ice/gas.\n" +
                    "Gravity: 0.89x Earth.\n" + // [cite: 95]
                    "Fun Fact: Rotates on its side like a rolling ball.";

            case "neptune":
                return "Temperature: Average -214 Celcius degree.\n" +
                    "Weather: Strongest winds (2,000 km/h).\n" +
                    "Rocks: Ice giant with small rocky core.\n" +
                    "Gravity: 1.14x Earth.\n" +
                    "Fun Fact: Immense pressure may create diamond rain.";

            default:
                return "Gravity conquered! Ready for the next mission.";
        }
    }
    }
