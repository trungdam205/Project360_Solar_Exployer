package com.solar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
// Import các thư viện cần thiết cho Data
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import com.solar.data.models.PlanetData;

public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");

        // --- BẮT ĐẦU ĐOẠN CODE TEST (ĐÃ SỬA) ---
        System.out.println("--- QA TEST: Đang đọc file JSON... ---");
        try {
            Json json = new Json();

            // 1. Đọc file và đóng ngoặc lệnh ngay tại đây (QUAN TRỌNG)
            ArrayList<PlanetData> planets = json.fromJson(
                ArrayList.class,
                PlanetData.class,
                Gdx.files.internal("data/planets.json")
            ); // <--- Dấu chấm phẩy kết thúc lệnh nằm ở đây!

            // 2. Sau khi đọc xong mới kiểm tra
            if (planets != null && !planets.isEmpty()) {
                System.out.println("✅ ĐỌC THÀNH CÔNG!");
                System.out.println("Tìm thấy hành tinh: " + planets.get(0).name);
                System.out.println("Trọng lực: " + planets.get(0).gravity);
            } else {
                System.err.println("❌ File rỗng hoặc đọc thất bại!");
            }

        } catch (Exception e) {
            System.err.println("❌ LỖI: " + e.getMessage());
            e.printStackTrace();
        }
        // --- KẾT THÚC TEST ---
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
