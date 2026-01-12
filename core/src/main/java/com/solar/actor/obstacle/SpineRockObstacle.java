package com.solar.actor.obstacle;

import com.badlogic.gdx.graphics.Texture;
import com.solar.actor.PlayerActor;

public class SpineRockObstacle extends Obstacle {

    private float respawnX;
    private float respawnY;

    public SpineRockObstacle(
        Texture texture,
        float x, float y,
        float width, float height,
        float respawnX, float respawnY
    ) {
        super(texture, x, y, width, height);
        this.respawnX = respawnX;
        this.respawnY = respawnY;
    }

    @Override
    public void onPlayerHit(PlayerActor player) {
        player.setPosition(respawnX, respawnY);
        player.resetState();
    }
}
