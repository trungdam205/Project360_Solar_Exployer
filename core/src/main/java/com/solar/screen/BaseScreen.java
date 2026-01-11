package com.solar.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.solar.MainGame;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public abstract class BaseScreen implements Screen {

    protected static final float WORLD_WIDTH = 1920;
    protected static final float WORLD_HEIGHT = 1200;

    protected MainGame game;
    protected Stage stage;
    protected Skin skin;
    protected Texture bgTexture;
    protected Image bgImage; // Biến này cần được gán giá trị

    public BaseScreen(MainGame game) {
        this.game = game;
        this.stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), game.batch);

        // Lấy Skin đã cấu hình từ MainGame
        this.skin = game.skin;
    }

    protected void setBackground(String texturePath) {
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        bgTexture = new Texture(Gdx.files.internal(texturePath));

        // --- SỬA LỖI 1: Gán vào biến toàn cục (this.bgImage) ---
        // Code cũ: Image bgImage = ... (Đây là biến cục bộ, biến toàn cục vẫn null)
        this.bgImage = new Image(bgTexture);

        // Luôn add background vào đầu danh sách (index 0) để nó nằm dưới cùng
        stage.addActor(this.bgImage);
        this.bgImage.toBack();
    }

    /*
    Add "Back" Button
    @param onBack: a runnable containing the logic to execute when button is pressed
     */
    protected void addBackButton(Runnable onBack) {
        // 1. Lấy style mặc định từ skin
        TextButton.TextButtonStyle originalStyle = skin.get(TextButton.TextButtonStyle.class);

        // 2. Copy ra style mới
        TextButton.TextButtonStyle customStyle = new TextButton.TextButtonStyle(originalStyle);

        // 3. SỬA LỖI TẠI ĐÂY:
        // Thay vì dùng: customStyle.font = game.font; (Lỗi vì game không có biến font)
        // Hãy dùng:
        customStyle.font = skin.getFont("title-font"); // Lấy font tên "title-font" mà bạn đã add trong MainGame

        customStyle.fontColor = Color.WHITE;

        // 4. Tạo nút
        TextButton back = new TextButton("BACK", customStyle);
        back.setSize(160, 60);
        back.setPosition(40, 40);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBack.run();
            }
        });
        stage.addActor(back);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        if (bgImage != null) {
            // Kéo dãn background phủ kín màn hình (extended)
            bgImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
            bgImage.setPosition(0, 0);
            bgImage.toBack();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (bgTexture != null) bgTexture.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void show() {
        // Đặt InputProcessor mỗi khi màn hình hiện lên
        Gdx.input.setInputProcessor(stage);
    }

    @Override public void render(float delta) {
        // Cần gọi render stage để thấy hình ảnh
        stage.act(delta);
        stage.draw();
    }
}
