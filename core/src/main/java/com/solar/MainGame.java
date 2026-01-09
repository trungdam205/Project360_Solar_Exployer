package com.solar;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.ArrayList;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    ArrayList<PlanetData> planets; // Danh sách chứa dữ liệu hành tinh
    ArrayList<Texture> planetTextures; // Danh sách chứa ảnh đã load

    OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        planets = new ArrayList<>();
        planetTextures = new ArrayList<>();

        Json json = new Json();
        try {
            // Đọc file từ thư mục assets/data/galaxy.json
            // Lưu ý: ArrayList.class để bảo nó là một danh sách
            planets = json.fromJson(ArrayList.class, PlanetData.class, Gdx.files.internal("data/galaxy.json"));

            System.out.println("Load thành công: " + planets.size() + " hành tinh.");
        } catch (Exception e) {
            System.out.println("Lỗi đọc file JSON: " + e.getMessage());
            e.printStackTrace();
        }

        for (PlanetData p : planets) {
            try {
                // p.texturePath lấy từ json (ví dụ: "planets/earth.png")
                Texture tex = new Texture(Gdx.files.internal(p.texturePath));
                planetTextures.add(tex);
            } catch (Exception e) {
                System.out.println("Không tìm thấy ảnh: " + p.texturePath);
                }
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        // Duyệt qua danh sách và vẽ
        for (int i = 0; i < planets.size(); i++) {
            PlanetData p = planets.get(i);
            Texture tex = planetTextures.get(i);

            // Vẽ tại tọa độ x, y lấy từ JSON
            // Có thể chia tỉ lệ (scale) nếu tọa độ quá lớn so với màn hình
            // Ví dụ: p.x * 0.1f để thu nhỏ khoảng cách
            batch.draw(tex, p.x, p.y);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        for (Texture t : planetTextures) {
            t.dispose();
        }
    }
}
