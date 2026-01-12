package com.solar.actor.obstacle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.solar.actor.PlayerActor;

public abstract class Obstacle extends Actor {

    protected Image image;

    public Obstacle(Texture texture, float x, float y, float width, float height) {
        image = new Image(texture);
        setBounds(x, y, width, height);
        image.setBounds(0, 0, width, height);
        image.setScaling(com.badlogic.gdx.utils.Scaling.stretch);
    }

    public Rectangle getRect() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        image.setPosition(getX(), getY());
        image.draw(batch, parentAlpha);
    }

    public abstract void onPlayerHit(PlayerActor player);
}
