package com.solar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.solar.data.DataManager;

public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        DataManager.getInstance().loadStaticData();
        DataManager.getInstance().loadGame();

        System.out.println("--- QA TEST: Đang đọc file JSON... ---");
        try {
            String planetName = DataManager.getInstance().getPlanet("earth").name;
            System.out.println("✅ TEST DataManager: Load thành công hành tinh -> " + planetName);
        } catch (Exception e) {
            System.err.println("❌ TEST DataManager: Lỗi, chưa load được dữ liệu!");
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
