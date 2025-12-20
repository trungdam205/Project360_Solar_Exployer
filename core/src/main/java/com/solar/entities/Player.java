package com.solar.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private float velocityY = 0;
    private float gravity = -800f;
    private float jumpForce = 350f;
    private boolean isGrounded = true;
    private float speed = 200f; // pixel / second
    private Texture texture;
    private Vector2 position;

    public Player() {
        texture = new Texture("player/astronaut.png");
        position = new Vector2(100, 100);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            velocityY = jumpForce;
            isGrounded = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += speed * delta;
        }

        velocityY += gravity * delta;
        position.y += velocityY * delta;

        if (position.y <= 100) {
            position.y = 100;
            velocityY = 0;
            isGrounded = true;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }
}
