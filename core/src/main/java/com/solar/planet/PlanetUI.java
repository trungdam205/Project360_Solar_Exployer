package com.solar.planet;

import com.badlogic.gdx. Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d. InputEvent;
import com.badlogic. gdx.scenes. scene2d.Stage;
import com. badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d. utils. ClickListener;
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

        Label titleLabel = new Label(planet.name + " Surface", skin);
        titleLabel. setFontScale(2.0f);
        titleLabel.setColor(Color. CYAN);

        TextButton backButton = new TextButton("< Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onBack != null) onBack.run();
            }
        });

        topBar.add(titleLabel).expandX().left();
        topBar. add(backButton).width(120).height(50).right();
        stage.addActor(topBar);
    }

    private void createGravityPanel() {
        Table panel = new Table();
        panel.setFillParent(true);
        panel.bottom().left().pad(25);

        Table box = new Table();
        box.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.85f)));
        box.pad(15);

        gravityStatusLabel = new Label("Gravity: Earth (1.00g)", skin);
        gravityStatusLabel. setFontScale(1.1f);
        gravityStatusLabel.setColor(Color.WHITE);

        gravityButton = new TextButton("Try " + planet.name + "'s Gravity!", skin);
        gravityButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onToggleGravity != null) {
                    onToggleGravity. run();
                }
            }
        });

        hintLabel = new Label(getHintText(), skin);
        hintLabel.setFontScale(0.9f);
        hintLabel.setColor(Color. YELLOW);
        hintLabel. setWrap(true);

        box.add(gravityStatusLabel).left().row();
        box.add(gravityButton).width(320).height(55).padTop(10).row();
        box.add(hintLabel).width(320).padTop(8);

        panel.add(box);
        stage.addActor(panel);
    }

    private void createControlsHint() {
        Table hint = new Table();
        hint.setFillParent(true);
        hint.bottom().padBottom(20);

        Table bg = new Table();
        bg.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.6f)));
        bg.pad(10, 20, 10, 20);

        Label label = new Label("Arrows/WASD: Move | SPACE: Jump | G: Toggle Gravity", skin);
        label.setFontScale(0.95f);
        label.setColor(1f, 1f, 1f, 0.85f);
        bg.add(label);

        hint.add(bg);
        stage.addActor(hint);
    }

    private void createVictoryWindow() {
        victoryWindow = new Window("", skin);
        victoryWindow.setSize(650, 320);
        victoryWindow.setPosition(worldWidth / 2 - 275, worldHeight / 2 - 160);
        victoryWindow.setVisible(false);
        victoryWindow.setMovable(false);

        Table content = new Table();

        Label title = new Label("MISSION COMPLETE!", skin);
        title.setFontScale(2.2f);
        title.setColor(Color.GOLD);

        Label message = new Label("You mastered " + planet.name + "'s gravity!", skin);
        message.setFontScale(1.3f);

        Label fact = new Label(getGravityFact(), skin);
        fact.setWrap(true);
        fact.setFontScale(1.05f);
        fact.setColor(0.5f, 0.9f, 1f, 1f);

        TextButton continueBtn = new TextButton("Back to Space", skin);
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
        content.add(fact).colspan(2).width(500).padBottom(18).row();
        content.add(retryBtn).width(170).height(50).padRight(15);
        content.add(continueBtn).width(200).height(50);

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
     * Called from PlanetScreen after physics. toggleGravity()
     */
    public void updateGravityDisplay() {
        try {
            if (physics == null) {
                Gdx.app. error("PlanetUI", "Physics is null!");
                return;
            }

            if (gravityStatusLabel == null || gravityButton == null) {
                Gdx.app. error("PlanetUI", "UI elements not initialized!");
                return;
            }

            // Update label text
            String gravityText = "Gravity: " + physics.getGravityString();
            gravityStatusLabel.setText(gravityText);

            // Update label color
            updateGravityStatusColor();

            // Update button text
            if (physics.isPlanetGravityEnabled()) {
                gravityButton.setText("Use Earth's Gravity");
            } else {
                gravityButton. setText("Try " + planet.name + "'s Gravity!");
            }

            Gdx.app. log("PlanetUI", "Gravity display updated:  " + gravityText);

        } catch (Exception e) {
            Gdx. app.error("PlanetUI", "Error updating gravity display:  " + e.getMessage());
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
            if (gravityStatusLabel != null) {
                gravityStatusLabel. setText("Gravity:  Earth (1.00g)");
                gravityStatusLabel.setColor(Color.WHITE);
            }
            if (gravityButton != null) {
                gravityButton.setText("Try " + planet.name + "'s Gravity!");
            }
            hideWindows();
        } catch (Exception e) {
            Gdx.app. error("PlanetUI", "Error resetting gravity UI: " + e.getMessage());
        }
    }

    // ==================== Text Helpers ====================

    private String getHintText() {
        if (physics == null) return "Jump over obstacles to reach the goal!";

        switch (physics.getPlanetType()) {
            case LOW_GRAVITY:
                return "Walls too high?  " + planet.name + "'s low gravity = HIGHER jumps!";
            case HIGH_GRAVITY:
                return "Spikes above? " + planet. name + "'s high gravity = LOWER jumps!";
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

    private void updateGravityStatusColor() {
        if (physics == null || gravityStatusLabel == null) return;

        if (! physics.isPlanetGravityEnabled()) {
            gravityStatusLabel.setColor(Color.WHITE);
            return;
        }

        switch (physics.getPlanetType()) {
            case LOW_GRAVITY:
                gravityStatusLabel.setColor(0.3f, 1f, 0.5f, 1f); // Green
                break;
            case HIGH_GRAVITY:
                gravityStatusLabel.setColor(1f, 0.5f, 0.3f, 1f); // Orange
                break;
            default:
                gravityStatusLabel. setColor(Color. WHITE);
                break;
        }
    }

    private String getGravityFact() {
        switch (planet.id) {
            case "mercury":
                return "On Mercury (0.38g), you could jump almost 3 times higher than on Earth!";
            case "venus":
                return "Venus has 91% of Earth's gravity - you'd barely notice the difference!";
            case "mars":
                return "On Mars (0.38g), astronauts could easily leap over obstacles!";
            case "moon":
                return "On the Moon (0.17g), Apollo astronauts could hop around like kangaroos!";
            case "jupiter":
                return "Jupiter's gravity (2.53g) would make you feel 2.5x heavier! ";
            case "saturn":
                return "Despite its size, Saturn's gravity (1.07g) is close to Earth's!";
            case "uranus":
                return "Uranus has 89% of Earth's gravity - surprisingly light for a giant!";
            case "neptune":
                return "Neptune's gravity (1.14g) would make jumping harder than on Earth!";
            case "pluto":
                return "On Pluto (0.06g), you could jump over a building!";
            default:
                return "Earth's gravity (1g) is what we call 'normal' gravity!";
        }
    }
}
