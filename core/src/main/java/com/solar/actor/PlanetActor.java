package com.solar.actor;

import com.badlogic.gdx.graphics.g2d.TextureRegion; // 1. Import TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.solar.data.PlanetData;

public class PlanetActor extends Image {

    // 2. Sửa Constructor: Thêm tham số 'TextureRegion region'
    // Xóa bỏ logic "new Texture()" bên trong
    public PlanetActor(PlanetData data, TextureRegion region, final Runnable onClick) {
        // Gọi Constructor của lớp cha (Image) với region đã lấy từ Atlas
        super(region);

        // Các logic set size và căn chỉnh giữ nguyên
        setSize(data.size, data.size);
        this.setOrigin(Align.center);

        // Logic sự kiện click giữ nguyên
        if (data.canEnter && onClick != null) {
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onClick.run();
                }
            });
        }
    }
}
