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

    // Info Labels
    private Label gravityLabel, weatherLabel, atmosLabel, surfaceLabel, resourceLabel;

    // Inventory
    private Array<InventorySlot> slots;
    private Texture slotBgTexture; // Cần dispose

    public GameHud(SpriteBatch batch, BitmapFont font) {
        super(batch);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE; // Màu chữ trắng

        // Tô màu nền (Màu xám đen bán trong suốt)
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();

        // 3. Vẽ viền (Màu trắng hoặc xám sáng)
        pixmap.setColor(1, 1, 1, 1);
        pixmap.drawRectangle(0, 0, 64, 64);
        pixmap.drawRectangle(1, 1, 62, 62); // Vẽ thêm 1 lớp cho viền dày hơn

        // 4. Chuyển Pixmap thành Texture để dùng
        slotBgTexture = new Texture(pixmap);

        // 5. Giải phóng pixmap vì không cần nữa (Texture đã lưu dữ liệu rồi)
        pixmap.dispose();

        TextureRegion slotBgRegion = new TextureRegion(slotBgTexture);
        // ---------------------------------------------

        // --- Layout Chính ---
        Table root = new Table();
        root.setFillParent(true);
        root.top().left(); // Căn gốc ở trên-trái

        // 1. Bảng Thông Tin (Góc trên trái)
        Table infoTable = new Table();
        infoTable.defaults().left().pad(2); // Căn trái, cách nhau xíu

        gravityLabel = new Label("Gravity: ---", labelStyle);
        weatherLabel = new Label("Temp: ---", labelStyle);
        atmosLabel = new Label("Atmos: ---", labelStyle);
        surfaceLabel = new Label("Surface: ---", labelStyle);
        resourceLabel = new Label("Primary Res: ---", labelStyle);

        infoTable.add(gravityLabel).row();
        infoTable.add(weatherLabel).row();
        infoTable.add(atmosLabel).row();
        infoTable.add(surfaceLabel).row();
        infoTable.add(resourceLabel).row();

        // 2. Thanh Inventory (Góc dưới giữa)
        Table invTable = new Table();
        slots = new Array<>();
        for(int i = 0; i < 8; i++) { // Tạo 8 ô
            InventorySlot slot = new InventorySlot(skin, slotBgRegion);
            slots.add(slot);
            invTable.add(slot).size(60, 60).pad(4); // Kích thước ô 60x60
        }

        // --- Ráp vào Root ---
        // Dòng 1: Info panel
        root.add(infoTable).top().left().expandX().pad(10);
        root.row();

        // Dòng 2: Khoảng trống (đẩy inventory xuống đáy)
        root.add().expand().fill();
        root.row();

        // Dòng 3: Inventory
        root.add(invTable).bottom().padBottom(10);

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

    // Hàm cập nhật Slot (index: 0 -> 7)
    public void updateSlot(int index, TextureRegion icon, int amount) {
        if(index >= 0 && index < slots.size) {
            slots.get(index).setItem(icon, amount);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        slotBgTexture.dispose();
    }
}
