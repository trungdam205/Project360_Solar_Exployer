package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas; // 1. Import Atlas
import com.badlogic.gdx.graphics.g2d.TextureRegion; // 2. Import Region
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.solar.MainGame;
import com.solar.actor.PlanetActor;
import com.solar.data.PlanetData;
import com.solar.data.PlanetDatabase;

public class SolarSystemScreen extends BaseScreen {

    private Group solarGroup;
    private Label planetNameLabel;

    // 3. Khai báo biến Atlas
    private TextureAtlas atlas;

    public SolarSystemScreen(MainGame game) {
        super(game);
        createSolarSystem();
        createUI();
    }

    private void createUI() {
        addBackButton(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new MenuScreen(game));
            }
        });

        Label titleLabel = new Label("SOLAR SYSTEM", skin);
        titleLabel.setFontScale(4f);
        titleLabel.setAlignment(Align.center);

        Table titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.top();
        titleTable.add(titleLabel).padTop(50);
        stage.addActor(titleTable);
    }

    private void createSolarSystem() {
        solarGroup = new Group();
        float centerX = stage.getViewport().getWorldWidth() / 2;
        float centerY = stage.getViewport().getWorldHeight() / 2;

        // 4. LẤY ATLAS TỪ ASSET MANAGER (Đã load ở LoadingScreen)
        // Đảm bảo đường dẫn đúng y hệt lúc load (có thể là "images/assets.atlas")
        if (game.assetManager.isLoaded("images/assets.atlas")) {
            atlas = game.assetManager.get("images/assets.atlas", TextureAtlas.class);
        } else {
            // Fallback nếu chưa load (chỉ để debug, thực tế không nên xảy ra)
            atlas = new TextureAtlas(Gdx.files.internal("images/assets.atlas"));
        }

        for (final PlanetData data : PlanetDatabase.getAllPlanets()) {

            // 5. TÌM VÙNG ẢNH TRONG ATLAS
            // data.texturePath bây giờ là tên (vd: "earth"), không phải đường dẫn file
            TextureRegion planetRegion = atlas.findRegion(data.texturePath);

            if (planetRegion == null) {
                Gdx.app.error("SolarSystem", "Không tìm thấy ảnh: " + data.texturePath);
                continue;
            }

            // 6. TRUYỀN REGION VÀO PLANET ACTOR
            // (Bạn cần sửa file PlanetActor.java để nhận tham số này, xem hướng dẫn bên dưới)
            PlanetActor p = new PlanetActor(data, planetRegion, new Runnable() {
                @Override
                public void run() {
                    if (data.canEnter) {

                        String message = "ENTERING " + data.displayName.toUpperCase() + "...";
                        game.setScreen(new LoadingScreen(game, message, false, new Runnable() {
                            @Override
                            public void run() {
                                // Chuyển sang màn hình hành tinh
                                game.setScreen(new PlanetScreen(game, data.type));
                                Gdx.app.log("Game", "Entered: " + data.displayName);
                            }
                        }));

                    } else {
                        Gdx.app.log("Game", "Cannot enter: " + data.displayName);
                    }
                }
            });

            // Tính toán vị trí
            float drawX = centerX + data.x - (data.size / 2);
            float drawY = centerY + data.y - (data.size / 2);
            p.setPosition(drawX, drawY);

            // Sự kiện chuột
            p.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                    planetNameLabel.setText(data.displayName);
                    planetNameLabel.setVisible(true);
                    p.setScale(1.1f);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                    planetNameLabel.setText("");
                    planetNameLabel.setVisible(false);
                    p.setScale(1.0f);
                }
            });
            solarGroup.addActor(p);
        }
        createNameLabel();
        stage.addActor(solarGroup);
    }

    private void createNameLabel() {
        planetNameLabel = new Label("", skin);
        planetNameLabel.setColor(1, 1, 0, 1);
        planetNameLabel.setFontScale(1.5f);
        stage.addActor(planetNameLabel);
    }

    // Khi người chơi click vào hành tinh Mars
    public void enterPlanet(PlanetData data) {

        // BƯỚC A: Dọn dẹp bộ nhớ cũ (Optional nhưng khuyên dùng)
        // Ví dụ: Đang ở Earth muốn sang Mars thì unload asset của Earth đi
        // game.assetManager.unload("textures/earth_map.png");

        // BƯỚC B: Xếp hàng asset MỚI cần cho màn chơi này
        // Ví dụ load map riêng của hành tinh đó
        game.assetManager.load("maps/" + data.texturePath + ".tmx", TiledMap.class);

        // BƯỚC C: Chuyển sang LoadingScreen
        String message = "ENTERING " + data.displayName.toUpperCase() + "...";
        game.setScreen(new LoadingScreen(game, message, false, new Runnable() {
            @Override
            public void run() {
                // Load xong map và nhạc rồi -> Vào chơi thôi!
                game.setScreen(new PlanetScreen(game, data.type));
            }
        }));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.1f, 1);

        stage.act(delta);
        stage.draw();

        // Debug tọa độ (Code cũ của bạn)
        Vector2 mouseLoc = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        float centerX = stage.getViewport().getWorldWidth() / 2;
        float centerY = stage.getViewport().getWorldHeight() / 2;
        float dbX = mouseLoc.x - centerX;
        float dbY = mouseLoc.y - centerY;
        Gdx.graphics.setTitle("Solar System - DB X: " + (int)dbX + " | Y: " + (int)dbY);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (planetNameLabel != null) {
            float labelX = (stage.getViewport().getWorldWidth() - planetNameLabel.getWidth()) / 2;
            float labelY = 50;
            planetNameLabel.setPosition(labelX, labelY);
        }
    }

    @Override
    public void show() {
        super.show();
        stage.getViewport().update(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            true
        );
    }

}
