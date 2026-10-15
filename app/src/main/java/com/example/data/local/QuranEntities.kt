package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "surahs")
data class SurahEntity(
    @PrimaryKey val number: Int,
    val nameArabic: String,
    val nameTransliteration: String,
    val englishName: String,
    val englishMeaning: String,
    val somaliMeaning: String,
    val ayahCount: Int,
    val revelationType: String, // "Meccan" or "Medinan" (Makki / Madani)
    val isDownloaded: Boolean = false
)

@Entity(
    tableName = "ayahs",
    indices = [
        Index(value = ["surahNumber", "ayahNumber"], unique = true),
        Index(value = ["surahNumber"])
    ]
)
data class AyahEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val somaliTranslation: String,
    val englishTranslation: String,
    val juzNumber: Int? = null,
    val pageNumber: Int? = null,
    val audioUrl: String? = null
)

@Entity(
    tableName = "bookmarks",
    indices = [Index(value = ["surahNumber", "ayahNumber"], unique = true)]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameArabic: String,
    val surahNameEnglish: String,
    val arabicText: String,
    val translationText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey val id: Int = 1,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameArabic: String,
    val surahNameEnglish: String,
    val progressPercent: Float,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_sessions")
data class TasbihSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dhikrName: String,
    val count: Int,
    val target: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloaded_audio")
data class DownloadedAudioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val surahNumber: Int,
    val reciterId: String,
    val localFilePath: String,
    val fileSize: Long,
    val downloadedAt: Long = System.currentTimeMillis()
)
