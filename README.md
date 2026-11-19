# Pizza Cooking World — Android

A fun and engaging **Kids Pizza Cooking Game** built with **Native Kotlin** and **Jetpack Compose**. The game runs **100% Offline** with zero backend, zero external asset downloads, no advertisements, and zero tracking — completely safe for toddlers and children.

---

## 🌟 Key Features

- **20 Diverse Master Pizza Recipes (R-01 through R-20):**
  - Comprehensive recipe catalog ranging from classic savory pizzas (Margherita, Pepperoni, Hawaii, BBQ Chicken, Seafood...) to sweet dessert pizzas (Chocolate Berry, Marshmallow, Rainbow Unicorn, Summer Watermelon...).
  - Child-friendly visual menu with colorful 2D cartoon illustrations, clear English recipe titles, and detailed descriptions.
- **Progressive Difficulty Tiers:**
  - **Level 1 (Easy - 6 Recipes):** Designed for young toddlers with sparkling target rings, quick tap interactions, and automatic 3-star baking.
  - **Level 2 (Medium - 9 Recipes):** Requires $\ge 75\%$ sauce coverage, recipe-matching toppings, and standard prep steps.
  - **Level 3 (Hard / Challenge - 5 Recipes):** Requires $\ge 85\%$ sauce coverage, precision topping placement, and an interactive **Heat Needle Gauge Minigame**: players tap "Take Pizza Out Now!" when the needle enters the golden star zone.
  - **No-Bake Category (2 Recipes):** Chilled dessert treats (Summer Watermelon Pizza, Berry Fruit Tart) that skip oven baking.
- **Rich Cartoon Visual Art:**
  - Italian Pizzeria shopfront with red-and-white striped awning, sunny blue sky, and fluffy clouds.
  - Kitchen background featuring an outdoor window view, green potted basil plant, and warm polished oak countertops.
  - True 1:1 circular wooden cutting board and pizza dough, paired with vibrant custom Compose Canvas topping graphics.
- **Procedural BGM Synthesizer (100% Offline):**
  - Built-in polyphonic Italian marimba & accordion melody generated at runtime via native `AudioTrack` hardware static loop synthesis.
  - Zero external MP3/audio files required; ultra-low memory footprint.
  - Audio Mute / Unmute controls accessible across all screens.

---

## 🛠️ Architecture & Tech Stack

- **Language:** Kotlin 2.0.0
- **UI Framework:** Jetpack Compose (Compose BOM 2024.06.00, Material 3)
- **Architecture Pattern:** Clean Architecture + Unidirectional Data Flow (UDF) with `StateFlow`
- **Audio Engine:** `AudioTrack` Hardware Static Loop Procedural Synthesizer
- **SDK Support:** Min SDK 26 (Android 8.0 Oreo) | Target SDK 34 (Android 14)
- **Quality Assurance:** JUnit 4 Unit Tests (100% Green / Passed)

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Iguana / Jellyfish / Koala or newer.
- JDK 17.
- Android device or emulator running Android 8.0+ (API 26+).

### Building and Running
1. Clone the repository:
   ```bash
   git clone https://github.com/minhlai1123/pizzacookking-androin.git
   cd pizzacookking-androin
   ```
2. Open the project folder in Android Studio.
3. Allow Gradle to sync dependencies.
4. Run unit tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```
5. Build and install Debug APK on connected device:
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

---

## 📁 Directory Structure

```
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/data/               # Master JSON catalogs (112 ingredients, 20 recipes, tools)
│   │   │   ├── java/com/antigravity/pizzacooking/
│   │   │   │   ├── audio/SoundEffects.kt  # Procedural Audio & BGM Synthesizer
│   │   │   │   ├── data/                  # Repositories & Engine Data Contracts
│   │   │   │   ├── engine/                # Pizza Cooking State Machine & Logic
│   │   │   │   ├── ui/                    # Jetpack Compose Screens & Components
│   │   │   │   │   ├── CartoonVisuals.kt  # 2D Cartoon Canvas Rendering
│   │   │   │   │   ├── KitchenScreen.kt   # Kitchen Screen & Oven Minigame
│   │   │   │   │   ├── MainMenuScreen.kt  # Cartoon Pizzeria Shopfront
│   │   │   │   │   └── RecipeSelectionScreen.kt # 20 Master Recipes & Difficulty Filter
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   └── test/                          # Comprehensive Unit Test Suite
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

---

## 📄 License
Copyright © Pizza Cooking World. All rights reserved.
