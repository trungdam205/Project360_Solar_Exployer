package com.solar;

import com.badlogic.gdx.Game;
import com.solar.screens.GalaxyScreen;

public class MainGame extends Game {

    @Override
    public void create() {
        setScreen(new GalaxyScreen());
    }
}
