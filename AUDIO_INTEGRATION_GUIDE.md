# Audio Integration Guide

## ✅ Đã Hoàn Thành

### 1. Package Structure
```
com.solar.audio/
├── AudioManager.java      // Quản lý music/sound effects
└── AudioAssets.java       // Constants cho audio paths
```

### 2. Files Đã Tạo/Chỉnh Sửa

**Mới:**
- `AudioManager.java` - Quản lý toàn bộ audio (music + sound effects)
- `AudioAssets.java` - Định nghĩa paths cho audio files

**Đã Cập Nhật:**
- `MainGame.java` - Thêm AudioManager instance, khởi tạo và dispose

### 3. Tính Năng

✅ **Background Music:**
- Tự động phát khi game load xong
- Loop vô hạn
- Volume mặc định: 50%

✅ **Sound Effects (Sẵn sàng để dùng):**
- Jump sound
- Collect sound
- Death sound
- Victory sound

✅ **Volume Control:**
- Điều chỉnh riêng music và sound
- Range: 0.0 - 1.0

✅ **Enable/Disable:**
- Bật/tắt music
- Bật/tắt sound effects

✅ **Resource Management:**
- Cache audio files (không load lại)
- Dispose khi thoát game

---

## 📁 Cấu Trúc Assets Folder

Đặt file audio vào:
```
assets/
└── audio/
    ├── music.mp3          ← Background music (REQUIRED)
    ├── jump.mp3           ← Sound effects (Optional)
    ├── collect.mp3
    ├── death.mp3
    └── victory.mp3
```

---

## 🎵 Cách Sử Dụng

### 1. Music đã tự động phát
Khi game khởi động, background music tự động play ở `MainGame.create()`

### 2. Thêm Sound Effects vào Game

**Ví dụ: PlanetScreen.java**
```java
// Khi player nhảy
private void handleJump() {
    if (canJump) {
        player.jump();
        game.getAudioManager().playSound(AudioAssets.JUMP_SOUND);
    }
}

// Khi player chết
private void checkCollision() {
    if (hitObstacle) {
        game.getAudioManager().playSound(AudioAssets.DEATH_SOUND);
        player.die();
    }
}

// Khi thắng
private void checkGoalReached() {
    if (reachedGoal) {
        game.getAudioManager().playSound(AudioAssets.VICTORY_SOUND);
        ui.showVictory();
    }
}
```

### 3. Điều Chỉnh Volume (MenuScreen)

```java
// Thêm slider vào menu
Slider musicSlider = new Slider(0, 100, 1, false, skin);
musicSlider.setValue(50);
musicSlider.addListener(new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        float volume = musicSlider.getValue() / 100f;
        game.getAudioManager().setMusicVolume(volume);
    }
});
```

### 4. Bật/Tắt Music

```java
TextButton musicToggle = new TextButton("Music: ON", skin);
musicToggle.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        AudioManager audio = game.getAudioManager();
        audio.setMusicEnabled(!audio.isMusicEnabled());
        musicToggle.setText("Music: " + (audio.isMusicEnabled() ? "ON" : "OFF"));
    }
});
```

---

## 🎮 API Reference

### AudioManager Methods

| Method | Mô Tả |
|--------|-------|
| `playMusic(String path, boolean loop)` | Phát nhạc nền |
| `stopMusic()` | Dừng nhạc |
| `pauseMusic()` | Tạm dừng nhạc |
| `resumeMusic()` | Tiếp tục nhạc |
| `playSound(String path)` | Phát sound effect |
| `setMusicVolume(float volume)` | Set volume nhạc (0-1) |
| `setSoundVolume(float volume)` | Set volume sound (0-1) |
| `setMusicEnabled(boolean)` | Bật/tắt nhạc |
| `setSoundEnabled(boolean)` | Bật/tắt sound |

---

## ⚡ Performance Tips

1. **Audio files nên nhẹ:**
   - Music: MP3, OGG (< 5MB)
   - Sound effects: WAV, MP3 (< 500KB)

2. **Sử dụng cache:**
   - AudioManager tự động cache
   - Không cần load lại mỗi lần play

3. **Dispose đúng cách:**
   - MainGame tự động dispose khi thoát
   - Không cần dispose thủ công

---

## 🐛 Troubleshooting

**Lỗi: "Failed to load music"**
→ Kiểm tra file `audio/music.mp3` có tồn tại trong `assets/`

**Không có âm thanh:**
→ Kiểm tra `musicEnabled` và `soundEnabled` = true

**Music không loop:**
→ Gọi `playMusic(path, true)` với tham số thứ 2 = true

---

## 📝 TODO (Tương Lai)

- [ ] Thêm nhiều background music cho từng planet
- [ ] Fade in/out effects
- [ ] Sound effects cho UI clicks
- [ ] Settings screen để điều chỉnh audio
- [ ] Playlist system

---

**Status:** ✅ HOÀN TẤT - Game đã có nhạc nền!

