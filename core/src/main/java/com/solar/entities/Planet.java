package com.solar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Planet {

    // ===== Data từ planets.json =====
    public String id;
    public String name;
    public float gravity;
    public float radius;
    public String texturePath;
    public String description;
    public float scale = 0.05f;

    // ===== Phần hiển thị (role B phụ trách) =====
    public float x;
    public float y;

    private Texture texture;

    // Bắt buộc cho LibGDX Json
    public Planet() {}

    public void loadTexture() {
        if (texturePath != null) {
            texture = new Texture(Gdx.files.internal(texturePath));
        }
    }

    public void render(SpriteBatch batch) {
        if (texture != null) {
            float width = texture.getWidth() * scale;
            float height = texture.getHeight() * scale;

            float drawX = x - width / 2f;
            float drawY = y - height / 2f;

            batch.draw(texture, drawX, drawY, width, height);
        }
    }


    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
