package com.solar.actor.obstacle;

import com.badlogic.gdx.graphics.Texture;
import com.solar.actor.PlayerActor;

public class RockObstacle extends Obstacle {

    public RockObstacle(
        Texture texture,
        float x, float y,
        float width, float height
    ) {
        super(texture, x, y, width, height);
    }

    @Override
    public void onPlayerHit(PlayerActor player) {
        // chặn & đẩy player
        if (player.getX() + player.getWidth() / 2 < getX() + getWidth() / 2) {
            player.setX(getX() - player.getWidth());
        } else {
            player.setX(getX() + getWidth());
        }
    }
}
