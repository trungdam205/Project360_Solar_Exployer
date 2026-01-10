CHARACTER DEMO - Hướng dẫn kiểm tra

=== CÁCH SỬ DỤNG ===

1. VÀO PLANET MAP:
   - Click vào một hành tinh từ màn hình hệ mặt trời
   - Bây giờ màn hình planet có thể click vào background để vào map

2. TẢI MAP (TMX):
   - Hệ thống sẽ tự động tải "maps/demo_planet.tmx"
   - Map có 32x18 tiles, với một nền đất ở phía dưới

3. LÀM CHUYỂN ĐỘNG NHÂN VẬT:

   PHÍM ĐIỀU KHIỂN:
   - A hoặc LEFT ARROW  → Di chuyển trái
   - D hoặc RIGHT ARROW → Di chuyển phải
   - SPACE/W/UP ARROW   → Nhảy lên
   - ESC hoặc nút Back  → Quay lại

4. CHỊ TIẾT KỸ THUẬT:
   
   File tạo mới:
   - PlanetMapScreen.java: Screen chứa logic map + player
   - demo_planet.tmx: File Tiled Map (32x18 tiles)
   
   Thay đổi:
   - PlanetScreen.java: Thêm listener click để vào map

5. CẶP HÀNH ĐỘNG:
   - Player có collision với đất → Có thể nhảy
   - Camera bám theo player
   - Debug mode hiển thị khung Box2D (có thể tắt ở GameConfig)

=== LỖI TIỀM ẨN ===

Nếu map không hiển thị:
- Tạo tileset.png thực tế thay vì placeholder text
- Hoặc disable map rendering (bỏ comment mapRenderer.render())

Nếu player rơi qua nền:
- Kiểm tra createGround() position và size
- Hoặc tăng world gravity từ -9.8f lên -15f

=== MỞ RỘNG ===

Bây giờ bạn có thể:
- Thêm obstacles (cây cối, đá, v.v.)
- Thêm collision layers từ Tiled
- Thêm animation cho player (idle, run, jump)
- Thêm items/mobs trên map

Các method quan trọng:
- PlayerEntity.update(delta) - Xử lý input + physics
- player.moveLeft() / moveRight() / jump() - API điều khiển
- createPlayer() / createGround() - Tạo bodies
