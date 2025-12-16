# 🚀 Project 360: Solar Explorer

Chào mừng đến với dự án Game 2D Space Exploration. Dự án được xây dựng trên nền tảng **Java** sử dụng **LibGDX Framework** và **Box2D Physics**.

---

## 📂 Phân chia Module & Trách nhiệm (PBI Assignment)

[cite_start]Dựa trên bảng Product Backlog[cite: 1], dự án được chia thành 5 Module chính. Các thành viên vui lòng code đúng thư mục được giao:

| Module | Phụ trách chính | Thư mục làm việc (Package) | Phạm vi công việc (PBI) |
| :--- | :--- | :--- | :--- |
| **A - Core & Physics** | (Tên Member A) | `managers/WorldManager`, `physics/` | [cite_start]Setup Box2D World, Game Loop, xử lý va chạm, Gravity[cite: 1]. |
| **B - Gameplay Logic** | (Tên Member B) | `entities/`, `entities/items/` | [cite_start]Player controller, Entity System, Item, Logic game chính[cite: 1]. |
| **C - UI & UX** | (Tên Member C) | `ui/`, `screens/`, `assets/skins/` | [cite_start]Menu, HUD, Inventory View, Scene2D, chuyển cảnh[cite: 1]. |
| **D - Assets & Graphics** | (Tên Member D) | `managers/AssetManager`, `assets/` | [cite_start]Texture Packing, Tilemap, quản lý tải tài nguyên, Animation[cite: 1]. |
| **E - Data & QA** | (Tên Member E) | `data/`, `managers/DataManager` | [cite_start]JSON Parsing, Lưu/Load game, Git Convention, Test[cite: 1]. |

---

## ⚠️ Quy tắc Git (Git Workflow) - ĐỌC KỸ ĐỂ TRÁNH CONFLICT

Để tránh việc code người này đè mất code người kia, tuyệt đối **KHÔNG** làm việc trực tiếp trên nhánh `main`. Hãy tuân thủ quy trình sau:

### 1. Quy tắc đặt tên nhánh (Branch Naming)
Khi bắt đầu một tính năng mới, hãy tạo nhánh với cú pháp:
`module-<tên_module>/<tên_tính_năng>`

*Ví dụ:*
* Module A làm vật lý: `module-a/setup-box2d`
* Module B làm nhân vật: `module-b/player-movement`
* Module C làm menu: `module-c/main-menu-ui`

### 2. Quy trình làm việc (Step-by-Step)

**Bước 1: Cập nhật code mới nhất từ main**
Trước khi bắt đầu code ngày mới, luôn luôn kéo code về:
```bash
git checkout main
git pull origin main
