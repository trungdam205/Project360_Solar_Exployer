package com.solar.actor;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.solar.data.PlanetData;

public class PlanetActor extends Image {

    public PlanetActor(PlanetData data, Runnable onClick) {
        super(new Texture(data.texturePath));
        setSize(data.size, data.size);
        this.setOrigin(Align.center);
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
