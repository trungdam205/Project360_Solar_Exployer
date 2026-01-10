🎮 CHARACTER DEMO - HƯỚNG DẪN KIỂM TRA

=== VẤNĐỀ VÀ GIẢI PHÁP ===

Nếu click vào planet không làm gì:

1. **Kiểm tra Build**
   - Chạy: .\gradlew build -x test
   - Đảm bảo BUILD SUCCESS

2. **Debug Click Event**
   - Xem console log khi click planet
   - Sẽ thấy: "Clicked on planet - Entering map"
   - Nếu không thấy → Input không được nhận

3. **Cấu Hình Input**
   - PlanetScreen setup InputMultiplexer
   - HUD stage có ưu tiên trước
   - Nút background được tạo để nhận click

=== CÁC FILE CHÍNH ===

1. PlanetMapScreenSimple.java
   - Screen đơn giản (không dùng Tiled map)
   - Có player + ground + test platforms
   - Debug Box2D renderer bật

2. PlanetScreen.java (cập nhật)
   - Invisible button để nhận click
   - Chuyển sang PlanetMapScreenSimple

3. PlayerEntity.java
   - Xử lý input (left, right, jump)
   - Raycast để kiểm tra chạm đất

=== CÓ LỖI? ===

**Lỗi 1: "Touchable" không tìm được**
   → Dã sửa: dùng TextButton thay vì setTouchable()

**Lỗi 2: Build không thành công**
   → Chạy: .\gradlew clean
   → Sau đó: .\gradlew build -x test

**Lỗi 3: Player không hiển thị**
   → Kiểm tra atlas có "idle" texture không
   → Xem console log: "Player texture loaded successfully"

**Lỗi 4: Player rơi qua nền**
   → Tăng gravity hoặc check createGround() position

=== TEST FLOW ===

1. Launch game
2. Chọn planet từ Solar System
3. Click vào background planet → Vào map
4. Dùng phím:
   - A/← = Left
   - D/→ = Right
   - SPACE/W/↑ = Jump
5. Test nhảy giữa platforms

=== CONSOLE LOGS SẼ THẤY ===

```
========== PLANET MAP TEST (Simple) ==========
Planet: MERCURY
Controls: A/D or LEFT/RIGHT = Move, SPACE/W/UP = Jump
==============================================
✓ Player texture loaded successfully
✓ Player created at position (5, 10)
✓ Ground created
✓ Test platforms created
✓ PlanetMapScreenSimple initialized successfully
```

=== TIẾP THEO ===

Khi character demo chạy được:
- Thêm animations (idle, run, jump)
- Thêm real Tiled map tiles
- Thêm enemies/items
- Thêm level progression
