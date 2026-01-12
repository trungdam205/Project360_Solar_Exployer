package com.solar. solarsystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx. scenes.scene2d.InputEvent;
import com.badlogic. gdx.scenes. scene2d.Stage;
import com. badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d. ui.*;
import com.badlogic.gdx.scenes.scene2d. utils.ChangeListener;
import com.badlogic. gdx.scenes.scene2d. utils. ClickListener;

/**
 * UI management for Solar System screen
 */
public class SolarSystemUI {

    private final Stage stage;
    private final Skin skin;
    private final float worldWidth;
    private final float worldHeight;

    // Callbacks
    private Runnable onBack;
    private Runnable onReset;
    private Runnable onPauseToggle;
    private Runnable onOrbitsToggle;

    // UI Elements
    private Slider speedSlider;
    private Slider zoomSlider;
    private Label speedValueLabel;
    private Label zoomValueLabel;
    private TextButton pauseButton;
    private TextButton orbitsButton;
    private Label eduLabel;

    // State references (để cập nhật UI)
    private SolarSystemState state;

    // Educational facts
    private static final String[] EDUCATIONAL_FACTS = {
        "Earth orbits the Sun at 107,000 km/h! ",
        "The Moon is spiraling away at 3.8 cm/year",
        "A year on Mars is almost twice as long as Earth's",
        "The Sun loses 4 million tons of mass every second",
        "Light takes 8 minutes to reach Earth from the Sun",
        "Jupiter's Great Red Spot has raged for 350+ years",
        "Saturn's rings are made of billions of ice particles",
        "Neptune has the fastest winds at 2,100 km/h",
        "Uranus rotates on its side from an ancient collision",
        "Mercury swings from -173C to 427C",
        "Venus rotates so slowly that a day is longer than a year",
        "Pluto's heart-shaped region is frozen nitrogen"
    };
    private float eduTimer = 0;
    private int currentFactIndex = 0;

    public SolarSystemUI(Stage stage, Skin skin, float worldWidth, float worldHeight) {
        this.stage = stage;
        this.skin = skin;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void init(SolarSystemState state, Runnable onBack, Runnable onReset,
                     Runnable onPauseToggle, Runnable onOrbitsToggle) {
        this. state = state;
        this.onBack = onBack;
        this.onReset = onReset;
        this. onPauseToggle = onPauseToggle;
        this.onOrbitsToggle = onOrbitsToggle;

        stage.clear();
        createTopBar();
        createControlPanel();
        createEducationalFacts();
        createHintLabel();
    }

    private void createTopBar() {
        Table topBar = new Table();
        topBar. setFillParent(true);
        topBar.top().pad(30);

        // Title section (left)
        Table titleSection = new Table();
        Label titleLabel = new Label("Solar System Explorer", skin);
        titleLabel. setFontScale(2.5f);
        titleLabel.setColor(Color.WHITE);

        Label subtitleLabel = new Label("Click on a planet to explore its surface!", skin);
        subtitleLabel.setFontScale(1.2f);
        subtitleLabel.setColor(Color. LIGHT_GRAY);

        titleSection.add(titleLabel).left().row();
        titleSection.add(subtitleLabel).left().padTop(5);

        // Back button (right)
        TextButton backButton = new TextButton("Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onBack != null) onBack.run();
            }
        });

        topBar.add(titleSection).expandX().left();
        topBar. add(backButton).width(150).height(60).right();
        stage.addActor(topBar);
    }

    private void createControlPanel() {
        Table controlPanel = new Table();
        controlPanel.setFillParent(true);
        controlPanel.bottom().left().pad(30);

        Table controlsTable = new Table();
        controlsTable. setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.6f)));
        controlsTable.pad(20);

        // Speed slider
        Table speedRow = new Table();
        Label speedLabel = new Label("Speed:", skin);
        speedLabel. setFontScale(1.2f);
        speedRow.add(speedLabel).width(100).left();

        speedSlider = new Slider(0, 100, 1, false, skin);
        speedSlider.setValue(50);
        speedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float speed = speedSlider. getValue() / 50f;
                state.setSpeed(speed);
                speedValueLabel.setText(String.format("%.1fx", speed));
            }
        });
        speedRow.add(speedSlider).width(200).padLeft(20);

        speedValueLabel = new Label("1.0x", skin);
        speedValueLabel.setFontScale(1.2f);
        speedValueLabel.setColor(Color.WHITE);
        speedRow.add(speedValueLabel).width(80).padLeft(10);
        controlsTable. add(speedRow).left().row();

        // Zoom slider
        Table zoomRow = new Table();
        Label zoomLabel = new Label("Zoom:", skin);
        zoomLabel.setFontScale(1.2f);
        zoomRow. add(zoomLabel).width(100).left();

        zoomSlider = new Slider(20, 200, 1, false, skin);
        zoomSlider. setValue(100);
        zoomSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float zoom = zoomSlider.getValue() / 100f;
                state. setZoom(zoom);
                zoomValueLabel.setText((int)(zoom * 100) + "%");
            }
        });
        zoomRow.add(zoomSlider).width(200).padLeft(20);

        zoomValueLabel = new Label("100%", skin);
        zoomValueLabel. setFontScale(1.2f);
        zoomValueLabel.setColor(Color.WHITE);
        zoomRow. add(zoomValueLabel).width(80).padLeft(10);
        controlsTable.add(zoomRow).left().padTop(10).row();

        // Control buttons
        Table btnRow = new Table();

        TextButton resetButton = new TextButton("Reset", skin);
        resetButton. addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onReset != null) onReset.run();
                resetSliders();
            }
        });

        pauseButton = new TextButton("Pause", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onPauseToggle != null) onPauseToggle.run();
                pauseButton.setText(state.isPaused() ? "Play" : "Pause");
            }
        });

        orbitsButton = new TextButton("Orbits", skin);
        orbitsButton.setChecked(true);
        orbitsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onOrbitsToggle != null) onOrbitsToggle.run();
            }
        });

        btnRow. add(resetButton).width(120).height(50).padRight(20);
        btnRow.add(pauseButton).width(120).height(50);
        btnRow. add(orbitsButton).width(120).height(50).padLeft(20);
        controlsTable.add(btnRow).left().padTop(20);

        controlPanel.add(controlsTable);
        stage.addActor(controlPanel);
    }

    private void createEducationalFacts() {
        Table eduTable = new Table();
        eduTable. setFillParent(true);
        eduTable.bottom().padBottom(120);

        Table eduBackground = new Table();
        eduBackground.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.7f)));
        eduBackground.pad(15, 30, 15, 30);

        eduLabel = new Label(EDUCATIONAL_FACTS[0], skin);
        eduLabel.setFontScale(1.1f);
        eduLabel.setColor(1f, 0.9f, 0.5f, 1f);
        eduBackground.add(eduLabel);

        eduTable.add(eduBackground);
        stage.addActor(eduTable);
    }

    private void createHintLabel() {
        Table hintTable = new Table();
        hintTable.setFillParent(true);
        hintTable.bottom().right().pad(30);

        Table hintBg = new Table();
        hintBg.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        hintBg.pad(10, 15, 10, 15);

        Label hintLabel = new Label("Drag to pan | Scroll to zoom", skin);
        hintLabel.setFontScale(1.0f);
        hintLabel.setColor(Color. LIGHT_GRAY);
        hintBg.add(hintLabel);

        hintTable.add(hintBg);
        stage.addActor(hintTable);
    }

    /**
     * Update educational facts timer
     */
    public void update(float delta) {
        eduTimer += delta;
        if (eduTimer > 10) {
            eduTimer = 0;
            currentFactIndex = (currentFactIndex + 1) % EDUCATIONAL_FACTS. length;
            eduLabel.setText(EDUCATIONAL_FACTS[currentFactIndex]);
        }
    }

    /**
     * Update zoom slider from external input (scroll)
     */
    public void updateZoomSlider(float zoom) {
        zoomSlider.setValue(zoom * 100);
        zoomValueLabel.setText((int)(zoom * 100) + "%");
    }

    /**
     * Reset sliders to default values
     */
    private void resetSliders() {
        zoomSlider.setValue(100);
        zoomValueLabel.setText("100%");
        speedSlider. setValue(50);
        speedValueLabel. setText("1.0x");
    }

    /**
     * Show message for non-explorable bodies
     */
    public void showNotExplorableMessage(String bodyName) {
        final Label messageLabel = new Label(bodyName + " cannot be explored!", skin);
        messageLabel.setFontScale(1.5f);
        messageLabel.setColor(1f, 0.5f, 0.3f, 1f);

        Table messageTable = new Table();
        messageTable.setFillParent(true);
        messageTable.center();

        Table messageBg = new Table();
        messageBg.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.8f)));
        messageBg.pad(20, 40, 20, 40);
        messageBg. add(messageLabel);

        messageTable. add(messageBg);
        stage.addActor(messageTable);

        // Fade out and remove after 1. 5 seconds
        messageTable.addAction(
            Actions.sequence(
                Actions.delay(1.5f),
                Actions. fadeOut(0.5f),
                Actions.removeActor()
            )
        );
    }
}
