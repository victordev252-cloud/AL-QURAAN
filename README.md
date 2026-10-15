# Quran Karim (القرآن الكريم) — Android Application

A complete, production-grade Islamic Holy Quran Android application built natively with **Kotlin**, **Jetpack Compose**, **Room Database (SQLite)**, **Retrofit**, and **Material Design 3**.

---

## 🌟 Key Features

1. **All 114 Surahs Catalog**:
   - Complete metadata for all 114 Surahs (Arabic name, English transliteration, English meaning, Somali meaning, Ayah count, and Makki/Madani classification).
   - Fast instant search and filtering by revelation type (All, Makki, Madani).

2. **Holy Quran Reading & Typography**:
   - Authentic Arabic Uthmani text rendered with the **Amiri Quran** typeface.
   - Somali translation by **Sheikh Abu Bakr Mahmoud Gumi (Abduh)**.
   - English translation by **Saheeh International**.
   - Adjustable Arabic font size, translation font size, and line spacing.
   - Independent toggles to show or hide Somali and English translations.
   - Proper right-to-left (RTL) text direction for Arabic and LTR for Somali/English.
   - Respectful handling of Surah At-Tawbah (no Bismillah) and Surah Al-Fatihah (verse 1 is Bismillah).

3. **Audio Recitation Player**:
   - Reciters: **Sheikh Mishary Rashid Alafasy** & **Sheikh Abdul Basit Abdus Samad**.
   - Controls: Play, Pause, Resume, Stop, Next Ayah, Previous Ayah, Seek Scrubber, Playback Speed (0.75x, 1.0x, 1.25x, 1.5x), Repeat Ayah, Repeat Surah, and Continuous Play.
   - Dynamic verse-level highlighting synchronized with audio playback and auto-scroll.
   - Persistent mini floating audio bar and full modal audio player sheet.
   - Background audio playback supported via Android Foreground Service with media notification.

4. **Offline Mode & Downloads**:
   - Room SQLite persistence (`quran_karim.db`) storing surahs, verses, bookmarks, and audio paths.
   - Surah audio download manager with real-time download progress for full offline listening.
   - Works seamlessly offline with bundled baseline chapters and local caching.

5. **Bookmarks & Reading Progress**:
   - One-tap ayah bookmarking with Arabic text, translations, and timestamps.
   - Continue Reading card on Home screen displaying last-read position, percentage progress, and quick-continue button.

6. **Global Quran Search**:
   - Multilingual search across Arabic Quranic text, English transliteration/meaning, Somali translation, and Surah numbers.

7. **Interactive Digital Tasbih (المسبحة)**:
   - Large circular tap counter with radial progress animation.
   - Preset Dhikrs: *SubhanAllah*, *Alhamdulillah*, *Allahu Akbar*, *Astaghfirullah*, *La ilaha illallah*, *Allahumma Salli Ala Muhammad*.
   - Configurable target count (33, 99, 100), undo, reset, haptic vibration feedback, and session history saved to database.

8. **Tafsir Ibn Kathir**:
   - Accessible authentic commentary sheet explaining verses, with direct links to verified resources on Quran.com.

9. **Daily Ayah & Notifications**:
   - Daily inspirational verse card with copy and share actions.
   - Configurable scheduled daily notification using Android AlarmManager.

10. **Multilingual & Theming**:
    - Full UI localization in **Somali (Af-Soomaali)**, **English**, and **Arabic (العربية)**.
    - Full **Dark Mode**, **Light Mode**, and **System Default** themes based on the premium Islamic color palette (#087F5B, #064E3B, #D4AF37).

---

## 🛠️ Architecture & Tech Stack

- **UI Layer**: Jetpack Compose, Material Design 3, Compose Navigation & StateFlow
- **Architecture**: MVVM with Clean Repository pattern
- **Local Database**: Android Room SQLite with Coroutines Flow
- **Network**: Retrofit 2 + OkHttp + Moshi (AlQuran Cloud API)
- **Audio Engine**: Android MediaPlayer + Foreground Service + Media Notification
- **Fonts**: Amiri Quran (bundled locally in `res/font/amiri.ttf`), Outfit (`res/font/outfit.ttf`)
- **Icon**: Custom adaptive gold Quran emblem on `#064E3B` emerald background

---

## 🚀 Build Instructions

### Prerequisites
- Android Studio Ladybug / Meerkat or Gradle 8.x+
- JDK 17+
- Android SDK 34+

### Routine Build & Test Commands
```bash
# Run unit tests
gradle :app:testDebugUnitTest

# Assemble Debug APK
gradle :app:assembleDebug

# Assemble Release APK
gradle :app:assembleRelease
```

The resulting release APK will be located at:
```
app/build/outputs/apk/release/app-release.apk
```

---

## 🔏 Release Signing Configuration

To sign the APK for Google Play Store release:
1. Generate an upload keystore:
   ```bash
   keytool -genkey -v -keystore my-upload-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
   ```
2. Configure environment variables or CI/CD secrets:
   - `KEYSTORE_PATH`: path to `my-upload-key.jks`
   - `STORE_PASSWORD`: keystore password
   - `KEY_PASSWORD`: alias private key password
3. Run `gradle :app:assembleRelease` to produce a signed production APK.
4. gradle wrapper --gradle-version 8.6
chmod +x gradlew
git add gradlew gradlew.bat gradle/wrapper/
git commit -m "Add Gradle wrapper"
git push
