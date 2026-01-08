package com.solar.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.solar.data.GalaxyDataLoader;
import com.solar.entities.Planet;

public class GalaxyScreen implements Screen {

    private SpriteBatch batch;
    private Array<Planet> planets;

    @Override
    public void show() {
        batch = new SpriteBatch();
        planets = GalaxyDataLoader.loadPlanets();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for (Planet planet : planets) {
            planet.render(batch);
        }
        batch.end();
    }



    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        for (Planet p : planets) {
            p.dispose();
        }
    }
}
