package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic. gdx.graphics. OrthographicCamera;
import com.solar.MainGame;
import com.solar.data.CelestialData;
import com.solar.solarsystem.*;

/**
 * Solar System exploration screen - Refactored
 */
public class SolarSystemScreen extends BaseScreen {

    // Camera
    private OrthographicCamera camera;

    // Components
    private SolarSystemState state;
    private SolarSystemRenderer renderer;
    private SolarSystemUI ui;
    private SolarSystemInput input;

    public SolarSystemScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        // Initialize components
        state = new SolarSystemState();
        renderer = new SolarSystemRenderer(WORLD_WIDTH, WORLD_HEIGHT);

        ui = new SolarSystemUI(stage, skin, WORLD_WIDTH, WORLD_HEIGHT);
        ui.init(
            state,
            () -> setScreenWithFade(new MenuScreen(game)),  // onBack
            this::resetView,                                  // onReset
            this::togglePause,                               // onPauseToggle
            this::toggleOrbits                               // onOrbitsToggle
        );

        input = new SolarSystemInput(camera, state, renderer, WORLD_WIDTH, WORLD_HEIGHT);
        input.setBodyClickListener(this::onBodyClicked);
        input.setZoomChangeListener(zoom -> ui.updateZoomSlider(zoom));
        input.setup(stage);
    }

    // ==================== Callbacks ====================

    private void onBodyClicked(CelestialData body) {
        Gdx.app. log("SolarSystem", "Clicked on:  " + body.name);

        if (body.explorable && body.exploration != null) {
            goToPlanet(body);
        } else {
            ui.showNotExplorableMessage(body.name);
        }
    }

    private void goToPlanet(CelestialData body) {
        Gdx.app. log("SolarSystem", "Exploring: " + body. name);
        setScreenWithFade(new PlanetScreen(game, body, this));
    }

    private void resetView() {
        state.reset();
    }

    private void togglePause() {
        state.togglePause();
    }

    private void toggleOrbits() {
        state. toggleOrbits();
    }

    // ==================== Render ====================

    @Override
    public void render(float delta) {
        // Update state
        state. update(delta);
        renderer.update(delta, state);
        ui.update(delta);

        // Clear screen
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx. gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Update camera
        camera. update();

        // Render solar system
        renderer. render(game.getBatch(), game.getShapeRenderer(), camera, state, delta);

        // Render UI
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (renderer != null) renderer.dispose();
    }
}
