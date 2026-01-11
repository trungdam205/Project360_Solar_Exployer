package com.solar.ui;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color; // <--- Import Màu
import com.badlogic.gdx.graphics.g2d.BitmapFont; // <--- Import Font

public class GameHud extends BaseHud {
    // --- Cấu hình HUD ---
    public static final float HUD_WIDTH = 420f;
    public static final float HUD_INFO_PAD = 6f;
    public static final float HUD_ROOT_PAD = 10f;

    // Info Labels
    private Label gravityLabel, weatherLabel, atmosLabel, surfaceLabel, resourceLabel;
    private float infoFontScale;

    public GameHud(SpriteBatch batch, BitmapFont font, float fontScale) {
        super(batch);
        this.infoFontScale = fontScale;
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE; // Màu chữ trắng

        // --- Layout Chính ---
        Table root = new Table();
        root.setFillParent(true);
        root.top().left(); // Căn gốc ở trên-trái

        // 1. Bảng Thông Tin (Góc trên trái)
        Table infoTable = new Table();
        infoTable.defaults().left().pad(HUD_INFO_PAD); // Căn trái, cách nhau xíu

        gravityLabel = new Label("Gravity: ---", labelStyle);
        gravityLabel.setFontScale(infoFontScale);
        weatherLabel = new Label("Temp: ---", labelStyle);
        weatherLabel.setFontScale(infoFontScale);
        atmosLabel = new Label("Atmos: ---", labelStyle);
        atmosLabel.setFontScale(infoFontScale);
        surfaceLabel = new Label("Surface: ---", labelStyle);
        surfaceLabel.setFontScale(infoFontScale);
        resourceLabel = new Label("Primary Res: ---", labelStyle);
        resourceLabel.setFontScale(infoFontScale);

        infoTable.add(gravityLabel).row();
        infoTable.add(weatherLabel).row();
        infoTable.add(atmosLabel).row();
        infoTable.add(surfaceLabel).row();
        infoTable.add(resourceLabel).row();

        // --- Ráp vào Root ---
        // Dòng 1: Info panel
        root.add(infoTable).top().left().width(HUD_WIDTH).pad(HUD_ROOT_PAD);
        root.row();

        // Dòng 2: Khoảng trống (đẩy inventory xuống đáy)
        root.add().expand().fill();
        root.row();

        stage.addActor(root);
    }

    // Hàm cập nhật data (Gọi từ Screen)
    public void updateInfo(double g, String w, String at, String sur, String res) {
        gravityLabel.setText(String.format("Gravity: %.2f m/s2", g));
        gravityLabel.setColor(1, 1, 1, 1);
        weatherLabel.setText("Weather: " + w);
        weatherLabel.setColor(1, 1, 1, 1);
        atmosLabel.setText("Atmosphere: " + at);
        atmosLabel.setColor(1, 1, 1, 1);
        surfaceLabel.setText("Surface: " + sur);
        surfaceLabel.setColor(1, 1, 1, 1);
        resourceLabel.setText("Primary Resource: " + res);
        resourceLabel.setColor(1,1,1,1);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    // Giữ lại constructor cũ để không lỗi các nơi khác
    public GameHud(SpriteBatch batch, BitmapFont font) {
        this(batch, font, 1.2f);
    }
}
