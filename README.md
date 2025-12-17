# 🚀 Project 360: Solar Explorer

Chào mừng đến với dự án Game 2D Space Exploration. Dự án được xây dựng trên nền tảng **Java** sử dụng **LibGDX Framework** và **Box2D Physics**.

---

## 📂 1. Phân chia Module & Trách nhiệm (PBI Assignment)

Dựa trên bảng Product Backlog, dự án được chia thành 5 Module chính. Các thành viên vui lòng code đúng thư mục và bám sát các đầu việc (PBI) được giao:

| Module | Phụ trách chính | Thư mục làm việc (Package) | Phạm vi PBI & Công việc |
| :--- | :--- | :--- | :--- |
| **A - Core & Physics** | (Tên Member A) | `managers/WorldManager`<br>`physics/`<br>`config/` | [cite_start]**PBI 01 - 07**: Setup Box2D World, Game Loop, xử lý va chạm (Collision), Debug Render, tối ưu Memory body[cite: 1]. |
| **B - Gameplay Logic** | (Tên Member B) | `entities/`<br>`entities/items/` | [cite_start]**PBI 08 - 15**: Player Controller, Animation, State Machine, Hệ thống Item & Inventory logic, Tương tác môi trường[cite: 1]. |
| **C - UI & UX** | (Tên Member C) | `ui/`<br>`screens/`<br>`assets/skins/` | [cite_start]**PBI 16 - 23**: Menu Screen, HUD, Inventory View, Scene2D, Chuyển cảnh (ScreenManager), Hiệu ứng UI[cite: 1]. |
| **D - Assets & Graphics** | (Tên Member D) | `managers/AssetManager`<br>`assets/`<br>`utils/` | [cite_start]**PBI 24 - 31**: Texture Packing (Atlas), TiledMap, Load/Unload tài nguyên, Asset Pipeline, Animation data[cite: 1]. |
| **E - Data & QA** | (Tên Member E) | `data/`<br>`managers/DataManager` | [cite_start]**PBI 32 - 41**: Thiết kế JSON Schema (Planet/Item), Load/Save Game, Quản lý Git Convention, Testing & QA toàn dự án[cite: 1]. |

---

## 🏗 2. Cấu trúc Thư mục (Project Skeleton)

Để đảm bảo code không bị lộn xộn, mọi người tuân thủ cấu trúc sau:

```text
core/src/com/mygame/space
├── config/             # Config chung (Gravity, Screen size)
├── data/               # Models (POJO) để map với JSON (Module E)
├── entities/           # Player, Items, Enemies (Module B)
├── managers/           # Các lớp quản lý: World, Asset, Data (Module A, D, E)
├── physics/            # Xử lý va chạm Box2D (Module A)
├── screens/            # Các màn hình Game, Menu (Module C)
├── ui/                 # Các thành phần giao diện Scene2D (Module C)
└── utils/              # Các hàm tiện ích chung

# Chuyển về nhánh main
git checkout main

# Kéo code mới nhất từ trên mạng về
git pull origin main

# Tạo nhánh mới và chuyển sang nhánh đó luôn
git checkout -b module-b/add-player-jump

# Xem các file đã thay đổi
git status

# Thêm tất cả thay đổi vào vùng chờ
git add .

# Lưu lại mốc lịch sử (Message cần rõ ràng)
git commit -m "PBI-08: Implement player jump logic"

# Đẩy nhánh của bạn lên server
git push -u origin module-b/add-player-jump
