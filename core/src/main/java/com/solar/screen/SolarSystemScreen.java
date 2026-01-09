package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.solar.MainGame;
import com.solar.actor.PlanetActor;
import com.solar.data.PlanetData;
import com.solar.data.PlanetDatabase;
import com.solar.data.PlanetType;
// import com.solar.screen.planet.*;

public class SolarSystemScreen extends BaseScreen {

    private Group solarGroup;
    private Image backgroundImage;
    private Texture bgTexture;
    private Label planetNameLabel; // Biến mới để hiện tên

    public SolarSystemScreen(MainGame game) {
        super(game);
        setBackground("background/background.png");
        createSolarSystem();
        createUI();
    }

    // ===== UI: Back button =====
    private void createUI() {
        addBackButton(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new MenuScreen(game));
            }
        });
            Label titleLabel = new Label("SOLAR SYSTEM", skin);

            // Chỉnh kích thước font (Menu là 5f, ở đây để 3f hoặc 4f là vừa đẹp)
        titleLabel.setFontScale(4f);

            // Căn giữa nội dung text (phòng trường hợp text có xuống dòng)
        titleLabel.setAlignment(Align.center);

            // Dùng Table để căn bố cục
            Table titleTable = new Table();
        titleTable.setFillParent(true); // Table này sẽ to bằng đúng màn hình
        titleTable.top(); // Đẩy nội dung lên trên cùng

            // Thêm label vào table, cách lề trên 50px cho thoáng
        titleTable.add(titleLabel).padTop(50);

            // Thêm vào stage
        stage.addActor(titleTable);

    }

    // ===== Tạo tất cả hành tinh =====
    private void createSolarSystem() {
        solarGroup = new Group();

        // 1. LẤY TÂM MÀN HÌNH (Cái này cực quan trọng)
        float centerX = stage.getViewport().getWorldWidth() / 2;
        float centerY = stage.getViewport().getWorldHeight() / 2;

        for (final PlanetData data : PlanetDatabase.getAllPlanets()) {

            // Logic tạo Actor giữ nguyên
            PlanetActor p = new PlanetActor(data, new Runnable() {
                @Override
                public void run() {
                    if (data.canEnter) {
                        game.setScreen(new PlanetScreen(game, data.type));
                    } else {
                        Gdx.app.log("Game", "Cannot enter: " + data.displayName);
                    }
                }


            });

            // --- QUAN TRỌNG: SET VỊ TRÍ TỪ DATA ---
// 2. SỬA LẠI DÒNG NÀY: CỘNG THÊM TÂM
            // Công thức: Tâm màn hình + Tọa độ Database - Nửa kích thước ảnh

            float drawX = centerX + data.x - (data.size / 2);
            float drawY = centerY + data.y - (data.size / 2);

            p.setPosition(drawX, drawY);

            p.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                    // Khi chuột đi vào vùng của hành tinh này -> Hiện tên
                    planetNameLabel.setText(data.displayName);
                    planetNameLabel.setVisible(true);

                    // (Tùy chọn) Hiệu ứng phóng to nhẹ khi trỏ vào
                    p.setScale(1.1f);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                    // Khi chuột đi ra -> Xóa tên (hoặc ẩn đi)
                    planetNameLabel.setText("");
                    planetNameLabel.setVisible(false);

                    // Trả lại kích thước cũ
                    p.setScale(1.0f);
                }
            });
            solarGroup.addActor(p);

        }
        createNameLabel();


        stage.addActor(solarGroup);
    }

    // Thêm hàm khởi tạo UI hiển thị tên
    private void createNameLabel() {
        // Tạo label với skin mặc định (hoặc skin của bạn)
        planetNameLabel = new Label("", skin);

        // Chỉnh màu chữ cho nổi (ví dụ màu vàng)
        planetNameLabel.setColor(1, 1, 0, 1);
        planetNameLabel.setFontScale(1.5f); // Chữ to lên xíu

        // Đặt vị trí: Giữa màn hình theo chiều ngang, sát đáy màn hình
        // Lưu ý: Chúng ta sẽ cập nhật vị trí này trong resize() để luôn căn giữa
        stage.addActor(planetNameLabel);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

// =================DElETE LATER====================
        Vector2 mouseLoc = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // 2. Lấy tâm màn hình
        float centerX = stage.getViewport().getWorldWidth() / 2;
        float centerY = stage.getViewport().getWorldHeight() / 2;

        // 3. Tính tọa độ tương đối so với TÂM
        // (Đây chính là số X, Y bạn cần điền vào Database)
        float dbX = mouseLoc.x - centerX;
        float dbY = mouseLoc.y - centerY;

        // 4. In ra (Ép kiểu int cho gọn)
        Gdx.graphics.setTitle("Solar System - Database X: " + (int)dbX + " | Y: " + (int)dbY);

    }

    @Override
    public void dispose() {
        super.dispose(); // Dispose stage
        if (bgTexture != null) {
            bgTexture.dispose();
        }
    }
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        // Căn giữa Label mỗi khi màn hình thay đổi kích thước
        if (planetNameLabel != null) {
            float labelX = (stage.getViewport().getWorldWidth() - planetNameLabel.getWidth()) / 2;
            float labelY = 50; // Cách đáy 50px
            planetNameLabel.setPosition(labelX, labelY);
        }
    }
}
