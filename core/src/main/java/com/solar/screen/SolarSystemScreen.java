package com.solar. screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com. badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com. badlogic.gdx.graphics.GL20;
import com.badlogic. gdx.graphics. OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer. ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com. badlogic.gdx.scenes.scene2d. Actor;
import com.badlogic. gdx.scenes. scene2d.InputEvent;
import com.badlogic. gdx.scenes. scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d. utils. ChangeListener;
import com.badlogic.gdx.scenes.scene2d. utils. ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic. gdx.utils. Array;
import com.solar.MainGame;
import com.solar.celestial.CelestialBodyRenderer;
import com. solar.data.CelestialData;
import com. solar.actor.Star;

public class SolarSystemScreen extends BaseScreen {

    private OrthographicCamera camera;

    // State
    private boolean paused = false;
    private float speed = 1f;
    private float zoom = 1f;
    private float offsetX = 0;
    private float offsetY = 0;
    private boolean showOrbits = true;
    private float time = 0;

    // Dragging
    private boolean dragging = false;
    private float lastMouseX, lastMouseY;

    // Stars
    private Array<Star> stars;

    // Celestial bodies
    private Array<CelestialData> bodies;
    private float[] bodyX;
    private float[] bodyY;
    private float[] bodySizes;

    // Renderer
    private CelestialBodyRenderer bodyRenderer;

    // UI elements
    private Slider speedSlider, zoomSlider;
    private Label speedValueLabel, zoomValueLabel;
    private TextButton pauseButton, orbitsButton;

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
    private Label eduLabel;
    private float eduTimer = 0;
    private int currentFactIndex = 0;

    public SolarSystemScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        bodyRenderer = new CelestialBodyRenderer();

        // Initialize stars
        stars = new Array<>();
        for (int i = 0; i < 300; i++) {
            stars.add(new Star(
                MathUtils. random(-500, WORLD_WIDTH + 500),
                MathUtils.random(-500, WORLD_HEIGHT + 500),
                MathUtils.random(1f, 3f),
                MathUtils.random(2f, 5f)
            ));
        }

        // Initialize celestial bodies
        bodies = CelestialData. createAllBodies();
        bodyX = new float[bodies. size];
        bodyY = new float[bodies.size];
        bodySizes = new float[bodies. size];

        // Initialize UI
        initUI();

        // Setup input
        setupInput();
    }

    private void initUI() {
        stage.clear();

        // ========== TOP BAR ==========
        Table topBar = new Table();
        topBar.setFillParent(true);
        topBar.top().pad(30);

        // Title section (left)
        Table titleSection = new Table();
        Label titleLabel = new Label("Solar System Explorer", skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setColor(Color. WHITE);

        Label subtitleLabel = new Label("Click on a planet to explore its surface!", skin);
        subtitleLabel.setFontScale(1.2f);
        subtitleLabel.setColor(Color. LIGHT_GRAY);

        titleSection.add(titleLabel).left().row();
        titleSection.add(subtitleLabel).left().padTop(5);

        // Buttons section (right)
        Table buttonsSection = new Table();

        orbitsButton = new TextButton("Orbits", skin);
        orbitsButton. setChecked(true);
        orbitsButton. addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showOrbits = !showOrbits;
            }
        });

        // Back button
        TextButton backButton = new TextButton("Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreenWithFade(new MenuScreen(game));
            }
        });

        buttonsSection.add(backButton).width(150).height(60).pad(5).padLeft(20);

        topBar.add(titleSection).expandX().left();
        topBar. add(buttonsSection).right();
        stage.addActor(topBar);

        // ========== BOTTOM LEFT CONTROLS ==========
        Table controlPanel = new Table();
        controlPanel.setFillParent(true);
        controlPanel.bottom().left().pad(30);

        Table controlsTable = new Table();
        controlsTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.6f)));
        controlsTable. pad(20);

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
                speed = speedSlider. getValue() / 50f;
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
        zoomRow.add(zoomLabel).width(100).left();

        zoomSlider = new Slider(20, 200, 1, false, skin);
        zoomSlider.setValue(100);
        zoomSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                zoom = zoomSlider.getValue() / 100f;
                zoomValueLabel. setText((int)(zoom * 100) + "%");
            }
        });
        zoomRow. add(zoomSlider).width(200).padLeft(20);

        zoomValueLabel = new Label("100%", skin);
        zoomValueLabel. setFontScale(1.2f);
        zoomValueLabel.setColor(Color. WHITE);
        zoomRow. add(zoomValueLabel).width(80).padLeft(10);
        controlsTable.add(zoomRow).left().padTop(10).row();

        // Control buttons
        Table btnRow = new Table();
        TextButton resetButton = new TextButton("Reset", skin);
        resetButton. addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetView();
            }
        });

        pauseButton = new TextButton("Pause", skin);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
                pauseButton.setText(paused ? "Play" : "Pause");
            }
        });

        btnRow.add(resetButton).width(120).height(50).padRight(20);
        btnRow.add(pauseButton).width(120).height(50);
        btnRow.add(orbitsButton).width(120).height(50).padLeft(20);
        controlsTable.add(btnRow).left().padTop(20);

        controlPanel.add(controlsTable);
        stage.addActor(controlPanel);

        // ========== EDUCATIONAL FACT LABEL ==========
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

        eduTable. add(eduBackground);
        stage.addActor(eduTable);

        // ========== HINT LABEL ==========
        Table hintTable = new Table();
        hintTable. setFillParent(true);
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

    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

                // Check if clicked on a celestial body
                for (int i = 0; i < bodies.size; i++) {
                    CelestialData body = bodies.get(i);
                    float dx = worldCoords. x - bodyX[i];
                    float dy = worldCoords.y - bodyY[i];
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);

                    float clickRadius = Math.max(bodySizes[i], 25f);

                    if (dist <= clickRadius) {
                        onBodyClicked(body);
                        return true;
                    }
                }

                // Start dragging
                dragging = true;
                lastMouseX = screenX;
                lastMouseY = screenY;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                dragging = false;
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (dragging) {
                    float deltaX = (screenX - lastMouseX) * (WORLD_WIDTH / Gdx.graphics. getWidth());
                    float deltaY = -(screenY - lastMouseY) * (WORLD_HEIGHT / Gdx.graphics.getHeight());
                    offsetX += deltaX;
                    offsetY += deltaY;
                    lastMouseX = screenX;
                    lastMouseY = screenY;
                }
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                zoom = MathUtils.clamp(zoom - amountY * 0.1f, 0.2f, 2f);
                zoomSlider.setValue(zoom * 100);
                zoomValueLabel.setText((int)(zoom * 100) + "%");
                return true;
            }
        });
        Gdx.input. setInputProcessor(multiplexer);
    }

    /**
     * Called when a celestial body is clicked
     */
    private void onBodyClicked(CelestialData body) {
        Gdx.app. log("SolarSystem", "Clicked on: " + body. name);

        // Check if explorable
        if (body.explorable && body.exploration != null) {
            // Go directly to PlanetScreen
            goToPlanet(body);
        } else {
            // Show a quick message for non-explorable bodies (like the Sun)
            showNotExplorableMessage(body);
        }
    }

    /**
     * Navigate to planet exploration screen
     */
    private void goToPlanet(CelestialData body) {
        Gdx.app. log("SolarSystem", "Exploring: " + body. name);
        setScreenWithFade(new PlanetScreen(game, body, this));
    }

    /**
     * Show message for non-explorable bodies
     */
    private void showNotExplorableMessage(CelestialData body) {
        // Create a temporary label that fades out
        final Label messageLabel = new Label(body.name + " cannot be explored!", skin);
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

        // Fade out and remove after 2 seconds
        messageTable.addAction(
            com.badlogic. gdx.scenes. scene2d.actions.Actions.sequence(
                com. badlogic.gdx.scenes.scene2d.actions. Actions.delay(1.5f),
        com.badlogic.gdx.scenes.scene2d. actions.Actions.fadeOut(0.5f),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor()
            )
        );
    }

    private void resetView() {
        offsetX = 0;
        offsetY = 0;
        zoom = 1f;
        time = 0;
        zoomSlider.setValue(100);
        zoomValueLabel.setText("100%");
    }

    @Override
    public void render(float delta) {
        // Update
        if (! paused) {
            time += speed * delta * 60;
        }

        bodyRenderer.update(delta);

        // Update educational facts
        eduTimer += delta;
        if (eduTimer > 10) {
            eduTimer = 0;
            currentFactIndex = (currentFactIndex + 1) % EDUCATIONAL_FACTS.length;
            eduLabel. setText(EDUCATIONAL_FACTS[currentFactIndex]);
        }

        // Update positions
        updatePositions();

        // Clear screen
        Gdx.gl. glClearColor(0.02f, 0.02f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl. glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        camera.update();

        // ========== RENDER WITH SHAPE RENDERER ==========
        game.shapeRenderer.setProjectionMatrix(camera. combined);

        // Render stars
        game.shapeRenderer. begin(ShapeType.Filled);
        for (Star star : stars) {
            star.update(delta);
            star.render(game.shapeRenderer);
        }
        game.shapeRenderer.end();

        // Render orbits
        if (showOrbits) {
            renderOrbits();
        }

        // Render sun glow
        game.shapeRenderer. begin(ShapeType. Filled);
        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies.get(i);
            if (body.id. equals("sun")) {
                bodyRenderer.renderSunGlow(game.shapeRenderer, bodyX[i], bodyY[i], bodySizes[i]);
            }
        }

        // Render fallback shapes for planets without textures
        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies.get(i);
            if (!bodyRenderer.hasTexture(body.id)) {
                bodyRenderer.renderFallbackShape(game.shapeRenderer, body, bodyX[i], bodyY[i], bodySizes[i]);
            }
        }
        game.shapeRenderer. end();

        // ========== RENDER WITH SPRITE BATCH ==========
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Render celestial bodies with textures
        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies. get(i);
            if (bodyRenderer.hasTexture(body.id)) {
                bodyRenderer.render(game.batch, body, bodyX[i], bodyY[i], bodySizes[i]);
            }
        }

        game.batch.end();

        // Render planet names
        renderPlanetNames();

        // Render UI
        stage.act(delta);
        stage.draw();
    }

    private void updatePositions() {
        float centerX = WORLD_WIDTH / 2f + offsetX;
        float centerY = WORLD_HEIGHT / 2f + offsetY;

        float earthX = 0, earthY = 0;

        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies. get(i);

            if (body.id. equals("sun")) {
                bodyX[i] = centerX;
                bodyY[i] = centerY;
                bodySizes[i] = body.baseSize * zoom;
            } else if (body.id. equals("moon")) {
                float angle = time * 0.01f * body.orbitSpeed;
                float orbitRadius = body.orbitRadius * zoom;
                bodyX[i] = earthX + MathUtils.cos(angle) * orbitRadius;
                bodyY[i] = earthY + MathUtils.sin(angle) * orbitRadius;
                bodySizes[i] = body.baseSize * zoom;
            } else {
                float angle = time * 0.01f * body.orbitSpeed + body.orbitPhase;
                float orbitRadius = body.orbitRadius * zoom;
                bodyX[i] = centerX + MathUtils.cos(angle) * orbitRadius;
                bodyY[i] = centerY + MathUtils. sin(angle) * orbitRadius;
                bodySizes[i] = body. baseSize * zoom;

                if (body.id.equals("earth")) {
                    earthX = bodyX[i];
                    earthY = bodyY[i];
                }
            }
        }
    }

    private void renderOrbits() {
        float centerX = WORLD_WIDTH / 2f + offsetX;
        float centerY = WORLD_HEIGHT / 2f + offsetY;

        game.shapeRenderer.begin(ShapeType. Line);

        Color orbitColor = new Color(0.4f, 0.5f, 0.8f, 0.25f);

        for (CelestialData body : bodies) {
            if (body. orbitRadius > 0 && ! body.id.equals("moon")) {
                float orbitRadius = body.orbitRadius * zoom;
                game.shapeRenderer. setColor(orbitColor);
                game. shapeRenderer.circle(centerX, centerY, orbitRadius, 128);
            }
        }

        // Moon orbit
        float earthX = 0, earthY = 0;
        for (int i = 0; i < bodies.size; i++) {
            if (bodies.get(i).id.equals("earth")) {
                earthX = bodyX[i];
                earthY = bodyY[i];
                break;
            }
        }

        for (CelestialData body : bodies) {
            if (body. id.equals("moon")) {
                game.shapeRenderer. setColor(0.5f, 0.5f, 0.6f, 0.2f);
                game.shapeRenderer. circle(earthX, earthY, body.orbitRadius * zoom, 48);
            }
        }

        game.shapeRenderer. end();
    }


    private void renderPlanetNames() {
        game.batch.begin();

        for (int i = 0; i < bodies.size; i++) {
            CelestialData body = bodies.get(i);

            // Skip sun for name rendering or render it differently
            if (! body.id.equals("sun")) {
                // You can add BitmapFont rendering here if needed
                // For now, names are shown on hover/click
            }
        }

        game. batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (bodyRenderer != null) bodyRenderer.dispose();
    }
}
