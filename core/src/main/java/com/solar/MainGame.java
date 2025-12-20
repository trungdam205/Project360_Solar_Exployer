package com.solar;

import com.solar.entities.Player; // Khanh: test player:D
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch; // Khanh: test player
    private Player player; // Khanh: test player


    @Override
    public void create() {
        batch = new SpriteBatch(); // Khanh: test player
        player = new Player(); // Khanh: test player
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        player.update(Gdx.graphics.getDeltaTime());

        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose(); // Khanh: test player
        player.dispose(); // Khanh: test player
    }
}
