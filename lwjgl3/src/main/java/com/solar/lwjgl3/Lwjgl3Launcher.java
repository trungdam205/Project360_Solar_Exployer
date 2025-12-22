package com.solar.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.solar.MainGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MainGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("SOLAR Exployer");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        // [OPTIONAL] Tăng kích thước cửa sổ lên HD để nhìn cho rõ
        configuration.setWindowedMode(1280, 720);

        // LibGDX sẽ tự chọn icon phù hợp kích thước từ danh sách này
        configuration.setWindowIcon("planets/earth.png", "planets/enceladus.png", "planets/europa.png", "planets/jupiter.png", "planets/mars.png", "planets/mercury.png", "planets/moon.png", "planets/neptune.png", "planets/saturn.png", "planets/sun.png", "planets/titan.png", "planets/uranus.png", "planets/venus.png");

        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0);

        return configuration;
    }
}
