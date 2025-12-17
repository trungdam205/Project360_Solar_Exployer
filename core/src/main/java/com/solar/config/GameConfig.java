package com.solar.config;

public class GameConfig {
    // Tên cửa sổ hoặc cấu hình chung
    public static final String TITLE = "Solar Game Box2D";

    // Kích thước màn hình ảo (World Units)
    // Ví dụ: Chiều rộng world là 8 mét, chiều cao 5 mét
    public static final float WORLD_WIDTH = 16f;
    public static final float WORLD_HEIGHT = 9f;

    // PPM (Pixels Per Meter): Tỷ lệ quy đổi từ mét sang pixel.
    // Ví dụ: 1 mét trong Box2D = 32 pixels trên màn hình (hoặc 100 tùy chọn).
    // Tuy nhiên, khi dùng Viewport với đơn vị mét trực tiếp, ta thường không cần nhân PPM thủ công khi render,
    // nhưng vẫn cần nó nếu chuyển đổi tọa độ chuột/input [1][2].
    public static final float PPM = 32f;
}
