package com.solar.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class InventorySlot extends Stack {
    private Image itemImage;
    private Label amountLabel;

    public InventorySlot(Skin skin, TextureRegion bgRegion) {
        // 1. Layer nền (Khung ô)
        Image bg = new Image(bgRegion);
        this.add(bg);

        // 2. Layer Item (Mặc định ẩn)
        itemImage = new Image();
        itemImage.setVisible(false);
        this.add(itemImage);

        // 3. Layer Số lượng (Góc dưới phải)
        amountLabel = new Label("", skin);
        amountLabel.setAlignment(Align.bottomRight);
        amountLabel.setFontScale(0.8f);
        this.add(amountLabel);
    }

    public void setItem(TextureRegion region, int amount) {
        if (region == null) {
            clearSlot();
            return;
        }
        itemImage.setDrawable(new TextureRegionDrawable(region));
        itemImage.setVisible(true);

        if (amount > 1) {
            amountLabel.setText(String.valueOf(amount));
            amountLabel.setVisible(true);
        } else {
            amountLabel.setVisible(false);
        }
    }

    public void clearSlot() {
        itemImage.setVisible(false);
        amountLabel.setVisible(false);
    }
}
