package com.solar.lwjgl3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {
    public static void main(String[] args) {
        // 1. CẤU HÌNH (SETTINGS)
        TexturePacker.Settings settings = new TexturePacker.Settings();

        // Kích thước tối đa của 1 file ảnh atlas (2048x2048 là chuẩn an toàn cho mobile)
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;

        // QUAN TRỌNG CHO PIXEL ART:
        // Chỉnh lọc ảnh thành Nearest để không bị nhòe (mờ) khi phóng to
        settings.filterMin = Texture.TextureFilter.Nearest;
        settings.filterMag = Texture.TextureFilter.Nearest;

        // Khoảng cách giữa các hình (tránh bị hiện viền trắng lạ khi game chạy)
        settings.paddingX = 2;
        settings.paddingY = 2;

        // Có xoay hình để tiết kiệm chỗ không? (Game 2D đơn giản nên để false cho dễ code)
        settings.rotation = false;

        // 2. ĐƯỜNG DẪN (PATHS)
        // Dấu chấm "." nghĩa là bắt đầu từ thư mục gốc của dự án

        // Đầu vào: Nơi chứa ảnh gốc (Folder bạn vừa chuyển ra ngoài)
        String inputDir = "./raw-asset/player";

        // Đầu ra: Nơi game sẽ đọc asset
        String outputDir = "./assets/images/atlas";

        // Tên file xuất ra (sẽ tạo thành player.atlas và player.png)
        String packFileName = "player";

        // 3. THỰC THI (RUN)
        try {
            System.out.println("⏳ Đang đóng gói assets...");
            TexturePacker.process(settings, inputDir, outputDir, packFileName);
            System.out.println("✅ THÀNH CÔNG! Đã xuất file vào: " + outputDir);
        } catch (Exception e) {
            System.err.println("❌ LỖI RỒI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
//
