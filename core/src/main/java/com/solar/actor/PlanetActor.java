package com.solar.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion; // 1. Import TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.solar.data.PlanetData;

public class PlanetActor extends Image {
    // Constructor: creates a planet actor with texture, size, and click handler
    public PlanetActor(PlanetData data, TextureRegion region, final Runnable onClick) {
        super(region);

        setSize(data.size, data.size); // Set planet size
        this.setOrigin(Align.center);

        // Add click listener if planet can be entered
        if (data.canEnter && onClick != null) {
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onClick.run();
                }
            });
        }
    }
}
