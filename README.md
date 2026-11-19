# Pizza Cooking World — Android

Trò chơi nấu ăn Pizza dành cho trẻ em (Kids Cooking Game) được phát triển bằng **Native Kotlin** và **Jetpack Compose**. Game hoạt động **100% Offline**, không yêu cầu kết nối mạng, không quảng cáo, và bảo đảm an toàn cho trẻ nhỏ.

---

## 🌟 Tính Năng Nổi Bật

- **20 Món Pizza Đa Dạng (R-01 đến R-20):**
  - Danh mục công thức đầy đủ từ các loại Pizza truyền thống (Margherita, Pepperoni, Hawaii, BBQ Chicken, Hải sản...) đến các món tráng miệng & sáng tạo độc đáo (Chocolate Berry, Marshmallow, Rainbow Unicorn, Trái cây mùa hè...).
  - Menu lựa chọn công thức trực quan với hình minh họa hoạt hình bắt mắt, tên tiếng Việt thân thiện với trẻ nhỏ.
- **Hệ Thống Phân Cấp Độ (Difficulty Levels):**
  - **Level 1 (Dễ):** Dành cho bé nhỏ tuổi với hướng dẫn vòng sáng mục tiêu (Sparkling rings), thao tác chạm nhanh gọn, tự động canh lò chuẩn 3 sao.
  - **Level 2 (Vừa):** Cần phủ đều sốt phô mai $\ge 75\%$, phân bổ topping đúng vị trí theo công thức.
  - **Level 3 (Thử thách):** Phủ sốt $\ge 85\%$ và minigame **Canh Lửa Lò Nướng (Heat Needle Gauge)**: người chơi bấm "Lấy bánh ra ngay!" khi kim nhiệt độ rơi vào vùng sao vàng để đạt điểm tuyệt đối.
  - **No-Bake:** Nhóm món tráng miệng mát lạnh không cần nướng (Watermelon Pizza, Berry Tart).
- **Giao Diện Hoạt Hình (Cartoon Visuals):**
  - Khung cảnh tiệm bánh Pizzeria phong cách hoạt hình với mái che sọc đỏ trắng, mây trời rực rỡ.
  - Màn hình nhà bếp nhìn ra cửa sổ trời xanh, chậu cây húng tây xanh mát, bàn gỗ sồi ấm áp.
  - Thớt và đế pizza tròn đều tỷ lệ 1:1, các loại topping (xúc xích, nấm, ớt chuông, ô liu, tôm, phô mai...) vẽ bằng Compose Canvas sinh động.
- **Nhạc Nền & Hiệu Ứng Âm Thanh BGM Tự Sinh (Procedural BGM Synthesizer):**
  - Tự động tổng hợp âm thanh đa âm phong cách Marimba/Accordion Ý tươi vui qua `AudioTrack` native.
  - Hoạt động offline 100%, không cần tải file âm thanh nặng từ bên ngoài.
  - Nút bật/tắt âm thanh (Mute/Unmute) tiện lợi ở mọi màn hình.

---

## 🛠️ Công Nghệ & Kiến Trúc

- **Ngôn ngữ:** Kotlin 2.0.0
- **UI Framework:** Jetpack Compose (Compose BOM 2024.06.00, Material 3)
- **Kiến trúc:** Clean Architecture + Unidirectional Data Flow (UDF) với `StateFlow`
- **Âm thanh:** `AudioTrack` Hardware Static Loop Synthesizer
- **Min SDK:** 26 (Android 8.0 Oreo) | **Target SDK:** 34 (Android 14)
- **Kiểm thử:** JUnit 4 Unit Tests (100% passed)

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### 1. Yêu cầu hệ thống
- Android Studio Iguana / Jellyfish / Koala hoặc mới hơn.
- JDK 17.
- Thiết bị Android hoặc Emulator chạy Android 8.0+ (API 26+).

### 2. Mở dự án trong Android Studio
1. Clone repository về máy:
   ```bash
   git clone https://github.com/minhlai1123/pizzacookking-androin.git
   ```
2. Mở Android Studio, chọn **Open** và chọn thư mục vừa clone.
3. Chờ Gradle đồng bộ (Sync Project with Gradle Files).

### 3. Chạy Unit Tests
```bash
./gradlew testDebugUnitTest
```

### 4. Build & Cài Đặt APK
```bash
./gradlew assembleDebug
# Cài đặt trực tiếp lên thiết bị cắm qua USB:
./gradlew installDebug
```

---

## 📁 Cấu Trúc Thư Mục

```
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/data/               # Catalog JSON (nguyên liệu, 20 công thức, dụng cụ)
│   │   │   ├── java/com/antigravity/pizzacooking/
│   │   │   │   ├── audio/SoundEffects.kt  # Procedural BGM Synthesizer
│   │   │   │   ├── data/                  # Repositories & Engine Data Contracts
│   │   │   │   ├── engine/                # Pizza Cooking State Machine & Logic
│   │   │   │   ├── ui/                    # Jetpack Compose Screens & Components
│   │   │   │   │   ├── CartoonVisuals.kt  # Cartoon Graphics Canvas
│   │   │   │   │   ├── KitchenScreen.kt   # Kitchen Screen & Oven Minigame
│   │   │   │   │   ├── MainMenuScreen.kt  # Pizzeria Cartoon Shopfront
│   │   │   │   │   └── RecipeSelectionScreen.kt # 20 Recipes & Difficulty Tiers
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   └── test/                          # Comprehensive Unit Test Suite
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

---

## 📄 License
Dự án được bảo hộ bản quyền cho Pizza Cooking World.
