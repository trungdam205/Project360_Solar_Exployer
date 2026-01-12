package com.solar. solarsystem;

import com.badlogic.gdx. Gdx;
import com.badlogic.gdx.InputAdapter;
import com. badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx. graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com. badlogic.gdx.scenes.scene2d. Stage;
import com. solar.data.CelestialData;

/**
 * Input handling for Solar System screen
 */
public class SolarSystemInput {

    private final float worldWidth;
    private final float worldHeight;

    // References
    private final OrthographicCamera camera;
    private final SolarSystemState state;
    private final SolarSystemRenderer renderer;

    // Callbacks
    private BodyClickListener bodyClickListener;
    private ZoomChangeListener zoomChangeListener;

    public interface BodyClickListener {
        void onBodyClicked(CelestialData body);
    }

    public interface ZoomChangeListener {
        void onZoomChanged(float zoom);
    }

    public SolarSystemInput(OrthographicCamera camera, SolarSystemState state,
                            SolarSystemRenderer renderer, float worldWidth, float worldHeight) {
        this.camera = camera;
        this. state = state;
        this.renderer = renderer;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void setBodyClickListener(BodyClickListener listener) {
        this.bodyClickListener = listener;
    }

    public void setZoomChangeListener(ZoomChangeListener listener) {
        this.zoomChangeListener = listener;
    }

    /**
     * Setup input processors
     */
    public void setup(Stage stage) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(createInputAdapter());
        Gdx.input. setInputProcessor(multiplexer);
    }

    private InputAdapter createInputAdapter() {
        return new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

                // Check if clicked on a celestial body
                CelestialData body = renderer.getBodyAtPosition(worldCoords. x, worldCoords.y);
                if (body != null) {
                    if (bodyClickListener != null) {
                        bodyClickListener.onBodyClicked(body);
                    }
                    return true;
                }

                // Start dragging
                state.setDragging(true);
                state.setLastMouse(screenX, screenY);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                state. setDragging(false);
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (state.isDragging()) {
                    float deltaX = (screenX - state. getLastMouseX()) * (worldWidth / Gdx.graphics. getWidth());
                    float deltaY = -(screenY - state.getLastMouseY()) * (worldHeight / Gdx. graphics.getHeight());
                    state.adjustOffset(deltaX, deltaY);
                    state.setLastMouse(screenX, screenY);
                }
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                state.adjustZoom(amountY);
                if (zoomChangeListener != null) {
                    zoomChangeListener.onZoomChanged(state.getZoom());
                }
                return true;
            }
        };
    }
}
