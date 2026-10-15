package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.api.AlQuranApiService
import com.example.data.api.ApiClient
import com.example.data.local.AppDatabase
import com.example.data.local.AyahEntity
import com.example.data.local.BookmarkEntity
import com.example.data.local.DownloadedAudioEntity
import com.example.data.local.QuranPrepopulatedData
import com.example.data.local.ReadingProgressEntity
import com.example.data.local.SurahEntity
import com.example.data.local.TasbihSessionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class QuranRepository(
    private val context: Context,
    private val database: AppDatabase = AppDatabase.getInstance(context),
    private val api: AlQuranApiService = ApiClient.apiService
) {
    private val quranDao = database.quranDao()
    private val tasbihDao = database.tasbihDao()

    suspend fun initializeDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val count = quranDao.getAllSurahs().size
        if (count < 114) {
            val prepopulated = QuranPrepopulatedData.getAll114Surahs()
            quranDao.insertSurahs(prepopulated)
        }
        val initialAyahs = QuranPrepopulatedData.getInitialPrepopulatedAyahs()
        quranDao.insertAyahs(initialAyahs)
    }

    fun getAllSurahsFlow(): Flow<List<SurahEntity>> = quranDao.getAllSurahsFlow()

    suspend fun getSurahByNumber(number: Int): SurahEntity? = withContext(Dispatchers.IO) {
        quranDao.getSurahByNumber(number)
    }

    fun getAyahsBySurahFlow(surahNumber: Int): Flow<List<AyahEntity>> =
        quranDao.getAyahsBySurahFlow(surahNumber)

    suspend fun getAyahsBySurah(surahNumber: Int): List<AyahEntity> = withContext(Dispatchers.IO) {
        quranDao.getAyahsBySurah(surahNumber)
    }

    /**
     * Fetches surah text (Arabic Uthmani, Somali translation, English translation)
     * and audio links from AlQuran Cloud API, merges them, and saves to Room.
     */
    suspend fun fetchAndCacheSurahDetail(surahNumber: Int, reciterId: String = "ar.alafasy"): Result<List<AyahEntity>> =
        withContext(Dispatchers.IO) {
            try {
                // Fetch Arabic Uthmani text
                val arabicRes = api.getSurahArabic(surahNumber)
                val arabicAyahs = arabicRes.data?.ayahs ?: emptyList()

                // Fetch Somali translation (so.abduh)
                var somaliAyahs: List<com.example.data.api.ApiAyahItem> = emptyList()
                try {
                    val somaliRes = api.getSurahSomali(surahNumber)
                    if (somaliRes.code == 200 && somaliRes.data?.ayahs != null) {
                        somaliAyahs = somaliRes.data.ayahs
                    }
                } catch (e: Exception) {
                    Log.w("QuranRepository", "Somali translation unavailable for surah $surahNumber", e)
                }

                // Fetch English translation (en.sahih)
                var englishAyahs: List<com.example.data.api.ApiAyahItem> = emptyList()
                try {
                    val englishRes = api.getSurahEnglish(surahNumber)
                    if (englishRes.code == 200 && englishRes.data?.ayahs != null) {
                        englishAyahs = englishRes.data.ayahs
                    }
                } catch (e: Exception) {
                    Log.w("QuranRepository", "English translation unavailable for surah $surahNumber", e)
                }

                // Fetch Audio metadata for precise verse-level audio URLs
                var audioAyahs: List<com.example.data.api.ApiAyahItem> = emptyList()
                try {
                    val audioRes = if (reciterId == "ar.abdulbasitmurattal") {
                        api.getSurahAbdulbasitAudio(surahNumber)
                    } else {
                        api.getSurahAlafasyAudio(surahNumber)
                    }
                    if (audioRes.code == 200 && audioRes.data?.ayahs != null) {
                        audioAyahs = audioRes.data.ayahs
                    }
                } catch (e: Exception) {
                    Log.w("QuranRepository", "Audio edition unavailable for surah $surahNumber", e)
                }

                val mergedAyahs = arabicAyahs.mapIndexed { index, arabicAyah ->
                    val ayahNum = arabicAyah.numberInSurah
                    val somaliText = somaliAyahs.getOrNull(index)?.text ?: ""
                    val englishText = englishAyahs.getOrNull(index)?.text ?: ""
                    val audioUrl = audioAyahs.getOrNull(index)?.audio
                        ?: "https://cdn.islamic.network/quran/audio/128/$reciterId/${arabicAyah.number}.mp3"

                    AyahEntity(
                        surahNumber = surahNumber,
                        ayahNumber = ayahNum,
                        arabicText = arabicAyah.text,
                        somaliTranslation = somaliText,
                        englishTranslation = englishText,
                        juzNumber = arabicAyah.juz,
                        pageNumber = arabicAyah.page,
                        audioUrl = audioUrl
                    )
                }

                if (mergedAyahs.isNotEmpty()) {
                    quranDao.insertAyahs(mergedAyahs)
                    quranDao.updateSurahDownloadStatus(surahNumber, true)
                    Result.success(mergedAyahs)
                } else {
                    // Check if we have cached ayahs
                    val cached = quranDao.getAyahsBySurah(surahNumber)
                    if (cached.isNotEmpty()) {
                        Result.success(cached)
                    } else {
                        Result.failure(Exception("No verses received from API"))
                    }
                }
            } catch (e: Exception) {
                Log.e("QuranRepository", "Error fetching surah $surahNumber", e)
                val cached = quranDao.getAyahsBySurah(surahNumber)
                if (cached.isNotEmpty()) {
                    Result.success(cached)
                } else {
                    Result.failure(e)
                }
            }
        }

    suspend fun search(query: String): List<AyahEntity> = withContext(Dispatchers.IO) {
        if (query.isBlank()) emptyList() else quranDao.searchAyahs(query.trim())
    }

    // Bookmarks
    fun getAllBookmarksFlow(): Flow<List<BookmarkEntity>> = quranDao.getAllBookmarksFlow()

    suspend fun isBookmarked(surahNumber: Int, ayahNumber: Int): Boolean = withContext(Dispatchers.IO) {
        quranDao.isBookmarked(surahNumber, ayahNumber)
    }

    suspend fun toggleBookmark(
        surahNumber: Int,
        ayahNumber: Int,
        surahArabic: String,
        surahEnglish: String,
        arabicText: String,
        translationText: String
    ): Boolean = withContext(Dispatchers.IO) {
        val alreadyBookmarked = quranDao.isBookmarked(surahNumber, ayahNumber)
        if (alreadyBookmarked) {
            quranDao.removeBookmark(surahNumber, ayahNumber)
            false
        } else {
            val bookmark = BookmarkEntity(
                surahNumber = surahNumber,
                ayahNumber = ayahNumber,
                surahNameArabic = surahArabic,
                surahNameEnglish = surahEnglish,
                arabicText = arabicText,
                translationText = translationText
            )
            quranDao.addBookmark(bookmark)
            true
        }
    }

    suspend fun removeBookmark(surahNumber: Int, ayahNumber: Int) = withContext(Dispatchers.IO) {
        quranDao.removeBookmark(surahNumber, ayahNumber)
    }

    // Reading Progress
    fun getReadingProgressFlow(): Flow<ReadingProgressEntity?> = quranDao.getReadingProgressFlow()

    suspend fun saveReadingProgress(
        surahNumber: Int,
        ayahNumber: Int,
        surahArabic: String,
        surahEnglish: String,
        totalAyahs: Int
    ) = withContext(Dispatchers.IO) {
        val percent = if (totalAyahs > 0) ayahNumber.toFloat() / totalAyahs.toFloat() else 0f
        val progress = ReadingProgressEntity(
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            surahNameArabic = surahArabic,
            surahNameEnglish = surahEnglish,
            progressPercent = percent.coerceIn(0f, 1f)
        )
        quranDao.saveReadingProgress(progress)
    }

    // Daily Ayah
    fun getDailyAyah(): AyahEntity {
        val pool = QuranPrepopulatedData.dailyAyahsPool
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear % pool.size).coerceIn(0, pool.size - 1)
        return pool[index]
    }

    // Tasbih
    fun getRecentTasbihSessionsFlow(): Flow<List<TasbihSessionEntity>> =
        tasbihDao.getRecentSessionsFlow()

    fun getTotalDhikrCountFlow(): Flow<Int> = tasbihDao.getTotalDhikrCountFlow()

    suspend fun saveTasbihSession(dhikrName: String, count: Int, target: Int) =
        withContext(Dispatchers.IO) {
            tasbihDao.insertSession(
                TasbihSessionEntity(
                    dhikrName = dhikrName,
                    count = count,
                    target = target
                )
            )
        }

    // Audio Downloads
    suspend fun downloadSurahAudio(
        surahNumber: Int,
        reciterId: String,
        onProgress: (Float) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val audioDir = File(context.filesDir, "audio/$reciterId").apply { mkdirs() }
            val outputFile = File(audioDir, "surah_$surahNumber.mp3")

            if (outputFile.exists() && outputFile.length() > 1024) {
                return@withContext Result.success(outputFile)
            }

            // Surah audio endpoint or first ayah audio
            val audioUrlString = "https://cdn.islamic.network/quran/audio-surah/128/$reciterId/$surahNumber.mp3"
            val url = URL(audioUrlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 15000
                readTimeout = 30000
                requestMethod = "GET"
            }

            if (connection.responseCode !in 200..299) {
                connection.disconnect()
                return@withContext Result.failure(Exception("Audio server returned code ${connection.responseCode}"))
            }

            val totalLength = connection.contentLength
            val inputStream = connection.inputStream
            val outputStream = FileOutputStream(outputFile)

            val buffer = ByteArray(8192)
            var bytesRead: Int
            var downloadedBytes = 0L

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                downloadedBytes += bytesRead
                if (totalLength > 0) {
                    val progress = (downloadedBytes.toFloat() / totalLength.toFloat()).coerceIn(0f, 1f)
                    withContext(Dispatchers.Main) { onProgress(progress) }
                }
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
            connection.disconnect()

            val entity = DownloadedAudioEntity(
                surahNumber = surahNumber,
                reciterId = reciterId,
                localFilePath = outputFile.absolutePath,
                fileSize = outputFile.length()
            )
            quranDao.insertDownloadedAudio(entity)

            Result.success(outputFile)
        } catch (e: Exception) {
            Log.e("QuranRepository", "Error downloading audio for surah $surahNumber", e)
            Result.failure(e)
        }
    }

    suspend fun getDownloadedAudio(surahNumber: Int, reciterId: String): DownloadedAudioEntity? =
        withContext(Dispatchers.IO) {
            quranDao.getDownloadedAudio(surahNumber, reciterId)
        }

    suspend fun getAudioCacheSize(): Long = withContext(Dispatchers.IO) {
        val audioDir = File(context.filesDir, "audio")
        calculateDirSize(audioDir)
    }

    suspend fun clearAudioCache() = withContext(Dispatchers.IO) {
        val audioDir = File(context.filesDir, "audio")
        audioDir.deleteRecursively()
        quranDao.clearNonessentialCache()
    }

    private fun calculateDirSize(dir: File): Long {
        if (!dir.exists()) return 0L
        var size = 0L
        dir.listFiles()?.forEach { file ->
            size += if (file.isDirectory) calculateDirSize(file) else file.length()
        }
        return size
    }
}
