package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {

    // Surahs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahs: List<SurahEntity>)

    @Query("SELECT * FROM surahs ORDER BY number ASC")
    fun getAllSurahsFlow(): Flow<List<SurahEntity>>

    @Query("SELECT * FROM surahs ORDER BY number ASC")
    suspend fun getAllSurahs(): List<SurahEntity>

    @Query("SELECT * FROM surahs WHERE number = :number")
    suspend fun getSurahByNumber(number: Int): SurahEntity?

    @Query("UPDATE surahs SET isDownloaded = :isDownloaded WHERE number = :number")
    suspend fun updateSurahDownloadStatus(number: Int, isDownloaded: Boolean)

    // Ayahs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAyahs(ayahs: List<AyahEntity>)

    @Query("SELECT * FROM ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    fun getAyahsBySurahFlow(surahNumber: Int): Flow<List<AyahEntity>>

    @Query("SELECT * FROM ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    suspend fun getAyahsBySurah(surahNumber: Int): List<AyahEntity>

    @Query("SELECT * FROM ayahs WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber LIMIT 1")
    suspend fun getAyah(surahNumber: Int, ayahNumber: Int): AyahEntity?

    @Query("SELECT COUNT(*) FROM ayahs WHERE surahNumber = :surahNumber")
    suspend fun getAyahCountForSurah(surahNumber: Int): Int

    // Search
    @Query("""
        SELECT a.* FROM ayahs a
        JOIN surahs s ON a.surahNumber = s.number
        WHERE a.arabicText LIKE '%' || :query || '%'
           OR a.somaliTranslation LIKE '%' || :query || '%'
           OR a.englishTranslation LIKE '%' || :query || '%'
           OR s.nameArabic LIKE '%' || :query || '%'
           OR s.nameTransliteration LIKE '%' || :query || '%'
           OR s.englishName LIKE '%' || :query || '%'
           OR CAST(a.surahNumber AS TEXT) = :query
        ORDER BY a.surahNumber ASC, a.ayahNumber ASC
        LIMIT 50
    """)
    suspend fun searchAyahs(query: String): List<AyahEntity>

    // Bookmarks
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    suspend fun removeBookmark(surahNumber: Int, ayahNumber: Int)

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarksFlow(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber)")
    fun isBookmarkedFlow(surahNumber: Int, ayahNumber: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber)")
    suspend fun isBookmarked(surahNumber: Int, ayahNumber: Int): Boolean

    // Reading Progress
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReadingProgress(progress: ReadingProgressEntity)

    @Query("SELECT * FROM reading_progress WHERE id = 1")
    fun getReadingProgressFlow(): Flow<ReadingProgressEntity?>

    @Query("SELECT * FROM reading_progress WHERE id = 1")
    suspend fun getReadingProgress(): ReadingProgressEntity?

    // Downloaded Audio
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadedAudio(audio: DownloadedAudioEntity)

    @Query("SELECT * FROM downloaded_audio WHERE surahNumber = :surahNumber AND reciterId = :reciterId")
    suspend fun getDownloadedAudio(surahNumber: Int, reciterId: String): DownloadedAudioEntity?

    @Query("SELECT * FROM downloaded_audio")
    suspend fun getAllDownloadedAudio(): List<DownloadedAudioEntity>

    @Query("DELETE FROM downloaded_audio WHERE surahNumber = :surahNumber AND reciterId = :reciterId")
    suspend fun deleteDownloadedAudio(surahNumber: Int, reciterId: String)

    @Query("DELETE FROM downloaded_audio")
    suspend fun deleteAllDownloadedAudio()

    @Transaction
    suspend fun clearNonessentialCache() {
        // Keep bookmarks, progress, and surah metadata, clear cached audio
        deleteAllDownloadedAudio()
    }
}

@Dao
interface TasbihDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TasbihSessionEntity)

    @Query("SELECT * FROM tasbih_sessions ORDER BY createdAt DESC LIMIT 20")
    fun getRecentSessionsFlow(): Flow<List<TasbihSessionEntity>>

    @Query("SELECT COALESCE(SUM(count), 0) FROM tasbih_sessions")
    fun getTotalDhikrCountFlow(): Flow<Int>
}
