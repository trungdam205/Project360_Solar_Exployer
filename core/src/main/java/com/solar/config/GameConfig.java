package com.solar.config;

public class GameConfig {

    // --- CẤU HÌNH MÀN HÌNH ---
    // Kích thước cửa sổ mặc định (Desktop)
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    // Kích thước thế giới ảo (Viewport) - Nên khớp với BaseScreen của bạn
    public static final float WORLD_WIDTH = 1920f;
    public static final float WORLD_HEIGHT = 1200f;

    // --- CẤU HÌNH VẬT LÝ (BOX2D) ---
    // PPM = Pixels Per Meter.
    // Box2D dùng mét, LibGDX dùng pixel.
    // 32 pixel = 1 mét (hoặc 100 tùy bạn chọn, 32 là chuẩn Mario/Platformer)
    public static final float PPM = 32.0f;

    // Tần số cập nhật vật lý (60 lần/giây)
    public static final float TIME_STEP = 1 / 60f;

    // Chế độ Debug (Bật lên để thấy khung dây xanh lá của Box2D)
    public static final boolean DEBUG_MODE = true;

    // --- CẤU HÌNH VA CHẠM (COLLISION BITS) ---
    // Dùng để lọc va chạm (Cái nào đụng được cái nào)
    // Ví dụ: Player ăn được Item, nhưng không đi xuyên qua Tường
    public static final short BIT_GROUND = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_ITEM   = 4;
    public static final short BIT_PLANET = 8;
    public static final short BIT_ENEMY  = 16;
}
