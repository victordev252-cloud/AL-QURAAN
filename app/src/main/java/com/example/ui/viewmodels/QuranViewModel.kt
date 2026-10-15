package com.example.ui.viewmodels

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AyahEntity
import com.example.data.local.BookmarkEntity
import com.example.data.local.ReadingProgressEntity
import com.example.data.local.SurahEntity
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class SurahDetailUiState {
    object Idle : SurahDetailUiState()
    object Loading : SurahDetailUiState()
    data class Success(val surah: SurahEntity, val ayahs: List<AyahEntity>) : SurahDetailUiState()
    data class Error(val message: String, val cachedAyahs: List<AyahEntity>) : SurahDetailUiState()
}

class QuranViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuranRepository(application)

    // Surahs list
    val allSurahs: StateFlow<List<SurahEntity>> = repository.getAllSurahsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filter tab: "ALL", "MAKKI", "MADANI"
    private val _surahFilter = MutableStateFlow("ALL")
    val surahFilter: StateFlow<String> = _surahFilter.asStateFlow()

    // Surah list search query
    private val _surahSearchQuery = MutableStateFlow("")
    val surahSearchQuery: StateFlow<String> = _surahSearchQuery.asStateFlow()

    // Global Quran Search query & results
    private val _globalSearchQuery = MutableStateFlow("")
    val globalSearchQuery: StateFlow<String> = _globalSearchQuery.asStateFlow()

    private val _globalSearchResults = MutableStateFlow<List<AyahEntity>>(emptyList())
    val globalSearchResults: StateFlow<List<AyahEntity>> = _globalSearchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Bookmarks
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.getAllBookmarksFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Reading Progress
    val readingProgress: StateFlow<ReadingProgressEntity?> = repository.getReadingProgressFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Surah Detail
    private val _selectedSurahState = MutableStateFlow<SurahDetailUiState>(SurahDetailUiState.Idle)
    val selectedSurahState: StateFlow<SurahDetailUiState> = _selectedSurahState.asStateFlow()

    // Audio download progress per surah: map of surahNumber -> Float (0..1)
    private val _downloadProgressMap = MutableStateFlow<Map<Int, Float>>(emptyMap())
    val downloadProgressMap: StateFlow<Map<Int, Float>> = _downloadProgressMap.asStateFlow()

    // Tafsir dialog state
    private val _tafsirAyah = MutableStateFlow<AyahEntity?>(null)
    val tafsirAyah: StateFlow<AyahEntity?> = _tafsirAyah.asStateFlow()

    // Daily Ayah
    private val _dailyAyah = MutableStateFlow(repository.getDailyAyah())
    val dailyAyah: StateFlow<AyahEntity> = _dailyAyah.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeDatabaseIfNeeded()
        }
    }

    fun setSurahFilter(filter: String) {
        _surahFilter.value = filter
    }

    fun setSurahSearchQuery(query: String) {
        _surahSearchQuery.value = query
    }

    fun setGlobalSearchQuery(query: String) {
        _globalSearchQuery.value = query
        if (query.length >= 2) {
            executeGlobalSearch(query)
        } else {
            _globalSearchResults.value = emptyList()
        }
    }

    fun executeGlobalSearch(query: String) {
        viewModelScope.launch {
            _isSearching.value = true
            val results = repository.search(query)
            _globalSearchResults.value = results
            _isSearching.value = false
        }
    }

    fun openSurah(surahNumber: Int, reciterId: String = "ar.alafasy") {
        viewModelScope.launch {
            _selectedSurahState.value = SurahDetailUiState.Loading
            val surah = repository.getSurahByNumber(surahNumber)
            if (surah == null) {
                _selectedSurahState.value = SurahDetailUiState.Error("Surah not found", emptyList())
                return@launch
            }

            // First check local DB
            val cachedAyahs = repository.getAyahsBySurah(surahNumber)
            if (cachedAyahs.size >= surah.ayahCount) {
                _selectedSurahState.value = SurahDetailUiState.Success(surah, cachedAyahs)
            } else {
                // Fetch from AlQuran Cloud API
                val result = repository.fetchAndCacheSurahDetail(surahNumber, reciterId)
                result.fold(
                    onSuccess = { ayahs ->
                        _selectedSurahState.value = SurahDetailUiState.Success(surah, ayahs)
                    },
                    onFailure = { error ->
                        val fallback = if (cachedAyahs.isNotEmpty()) cachedAyahs else emptyList()
                        _selectedSurahState.value = SurahDetailUiState.Error(
                            error.localizedMessage ?: "Failed to load surah verses",
                            fallback
                        )
                    }
                )
            }
        }
    }

    fun toggleBookmark(ayah: AyahEntity, surahNameArabic: String, surahNameEnglish: String) {
        viewModelScope.launch {
            val isNowBookmarked = repository.toggleBookmark(
                surahNumber = ayah.surahNumber,
                ayahNumber = ayah.ayahNumber,
                surahArabic = surahNameArabic,
                surahEnglish = surahNameEnglish,
                arabicText = ayah.arabicText,
                translationText = if (ayah.somaliTranslation.isNotBlank()) ayah.somaliTranslation else ayah.englishTranslation
            )
            val msg = if (isNowBookmarked) "Saved to bookmarks" else "Removed from bookmarks"
            Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun removeBookmark(surahNumber: Int, ayahNumber: Int) {
        viewModelScope.launch {
            repository.removeBookmark(surahNumber, ayahNumber)
            Toast.makeText(getApplication(), "Bookmark removed", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveProgress(surahNumber: Int, ayahNumber: Int, surahArabic: String, surahEnglish: String, totalAyahs: Int) {
        viewModelScope.launch {
            repository.saveReadingProgress(surahNumber, ayahNumber, surahArabic, surahEnglish, totalAyahs)
        }
    }

    fun copyAyahToClipboard(ayah: AyahEntity, includeTranslation: Boolean = false) {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = if (includeTranslation) {
            "${ayah.arabicText}\n\n${ayah.somaliTranslation.ifBlank { ayah.englishTranslation }}\n\n(Quran ${ayah.surahNumber}:${ayah.ayahNumber})"
        } else {
            "${ayah.arabicText}\n(Quran ${ayah.surahNumber}:${ayah.ayahNumber})"
        }
        val clip = ClipData.newPlainText("Quran Ayah", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(getApplication(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun shareAyah(ayah: AyahEntity, surahName: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Holy Quran: $surahName (${ayah.surahNumber}:${ayah.ayahNumber})")
            putExtra(
                Intent.EXTRA_TEXT,
                "\"${ayah.arabicText}\"\n\n${ayah.somaliTranslation}\n\n${ayah.englishTranslation}\n\n— Surah $surahName [${ayah.surahNumber}:${ayah.ayahNumber}]\nShared via Quran Karim"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        getApplication<Application>().startActivity(Intent.createChooser(shareIntent, "Share Quran Ayah").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun openTafsir(ayah: AyahEntity) {
        _tafsirAyah.value = ayah
    }

    fun closeTafsir() {
        _tafsirAyah.value = null
    }

    fun downloadSurahAudio(surahNumber: Int, reciterId: String) {
        viewModelScope.launch {
            _downloadProgressMap.value = _downloadProgressMap.value.toMutableMap().apply { put(surahNumber, 0.01f) }
            val result = repository.downloadSurahAudio(surahNumber, reciterId) { progress ->
                _downloadProgressMap.value = _downloadProgressMap.value.toMutableMap().apply { put(surahNumber, progress) }
            }
            result.fold(
                onSuccess = {
                    Toast.makeText(getApplication(), "Surah $surahNumber audio downloaded", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(getApplication(), "Audio download failed: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            )
            _downloadProgressMap.value = _downloadProgressMap.value.toMutableMap().apply { remove(surahNumber) }
        }
    }
}
