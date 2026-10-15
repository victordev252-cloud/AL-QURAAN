package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.core.localization.AppLanguage
import com.example.ui.components.AudioFloatingBar
import com.example.ui.components.AudioPlayerSheet
import com.example.ui.components.NavTab
import com.example.ui.components.QuranBottomNavBar
import com.example.ui.components.TafsirDialog
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuranListScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SurahDetailScreen
import com.example.ui.screens.TasbihScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.QuranKarimTheme
import com.example.ui.viewmodels.AudioViewModel
import com.example.ui.viewmodels.QuranViewModel
import com.example.ui.viewmodels.SurahDetailUiState
import com.example.ui.viewmodels.TasbihViewModel
import com.example.ui.viewmodels.SettingsViewModel

class MainActivity : ComponentActivity() {

    private val quranViewModel: QuranViewModel by viewModels()
    private val audioViewModel: AudioViewModel by viewModels()
    private val tasbihViewModel: TasbihViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle notification launch intent
        val targetSurah = intent.getIntExtra("navigate_surah", -1)
        if (targetSurah > 0) {
            quranViewModel.openSurah(targetSurah, settingsViewModel.selectedReciterId.value)
        }

        setContent {
            val themeMode by settingsViewModel.themeMode.collectAsState()
            val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
            val isDarkTheme = when (themeMode) {
                "LIGHT" -> false
                "DARK" -> true
                else -> isSystemInDarkTheme()
            }

            val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                QuranKarimTheme(darkTheme = isDarkTheme) {
                    QuranAppRoot(
                        quranViewModel = quranViewModel,
                        audioViewModel = audioViewModel,
                        tasbihViewModel = tasbihViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun QuranAppRoot(
    quranViewModel: QuranViewModel,
    audioViewModel: AudioViewModel,
    tasbihViewModel: TasbihViewModel,
    settingsViewModel: SettingsViewModel
) {
    var showSplash by remember { mutableStateOf(true) }
    var currentTab by remember { mutableStateOf(NavTab.HOME) }
    var inSurahDetail by remember { mutableStateOf(false) }
    var inAboutScreen by remember { mutableStateOf(false) }
    var showAudioSheet by remember { mutableStateOf(false) }

    val strings by settingsViewModel.strings.collectAsState()
    val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
    val themeMode by settingsViewModel.themeMode.collectAsState()
    val arabicFontSize by settingsViewModel.arabicFontSize.collectAsState()
    val translationFontSize by settingsViewModel.translationFontSize.collectAsState()
    val lineSpacing by settingsViewModel.lineSpacing.collectAsState()
    val showSomali by settingsViewModel.showSomali.collectAsState()
    val showEnglish by settingsViewModel.showEnglish.collectAsState()
    val selectedReciterId by settingsViewModel.selectedReciterId.collectAsState()
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()
    val cacheSizeBytes by settingsViewModel.cacheSizeBytes.collectAsState()

    // Quran State
    val surahs by quranViewModel.allSurahs.collectAsState()
    val surahFilter by quranViewModel.surahFilter.collectAsState()
    val surahSearchQuery by quranViewModel.surahSearchQuery.collectAsState()
    val globalSearchQuery by quranViewModel.globalSearchQuery.collectAsState()
    val globalSearchResults by quranViewModel.globalSearchResults.collectAsState()
    val isSearching by quranViewModel.isSearching.collectAsState()
    val bookmarks by quranViewModel.bookmarks.collectAsState()
    val readingProgress by quranViewModel.readingProgress.collectAsState()
    val selectedSurahState by quranViewModel.selectedSurahState.collectAsState()
    val downloadProgressMap by quranViewModel.downloadProgressMap.collectAsState()
    val dailyAyah by quranViewModel.dailyAyah.collectAsState()
    val tafsirAyah by quranViewModel.tafsirAyah.collectAsState()

    // Audio State
    val audioState by audioViewModel.playbackState.collectAsState()

    // Tasbih State
    val tasbihCount by tasbihViewModel.count.collectAsState()
    val tasbihTarget by tasbihViewModel.target.collectAsState()
    val tasbihSelectedPreset by tasbihViewModel.currentDhikrIndex.collectAsState()
    val tasbihVibrationEnabled by tasbihViewModel.vibrationEnabled.collectAsState()
    val tasbihTotalCompleted by tasbihViewModel.totalDhikrCount.collectAsState()

    if (showSplash) {
        SplashScreen(onSplashFinished = { showSplash = false })
        return
    }

    // Surah Detail Screen
    if (inSurahDetail) {
        val currentSurahNum = (selectedSurahState as? SurahDetailUiState.Success)?.surah?.number ?: 1
        val downloadProgress = downloadProgressMap[currentSurahNum]

        SurahDetailScreen(
            state = selectedSurahState,
            strings = strings,
            playingAyahNumber = if (audioState.surahNumber == currentSurahNum) audioState.currentAyahNumber else -1,
            isAudioPlaying = audioState.isPlaying,
            bookmarks = bookmarks,
            downloadProgress = downloadProgress,
            arabicFontSizeSp = arabicFontSize,
            translationFontSizeSp = translationFontSize,
            lineSpacingSp = lineSpacing,
            showSomali = showSomali,
            showEnglish = showEnglish,
            onBackClick = { inSurahDetail = false },
            onPlayAyah = { ayahs, index, surahName ->
                audioViewModel.playAyahs(ayahs, index, surahName, selectedReciterId)
            },
            onToggleBookmark = { ayah, arName, enName ->
                quranViewModel.toggleBookmark(ayah, arName, enName)
            },
            onCopyArabic = { ayah -> quranViewModel.copyAyahToClipboard(ayah, false) },
            onCopyTranslation = { ayah -> quranViewModel.copyAyahToClipboard(ayah, true) },
            onShareAyah = { ayah, name -> quranViewModel.shareAyah(ayah, name) },
            onOpenTafsir = { ayah -> quranViewModel.openTafsir(ayah) },
            onDownloadSurahAudio = { num -> quranViewModel.downloadSurahAudio(num, selectedReciterId) },
            onRetry = { quranViewModel.openSurah(currentSurahNum, selectedReciterId) },
            onFontSizeChange = { settingsViewModel.setArabicFontSize(it) },
            onToggleShowSomali = { settingsViewModel.toggleShowSomali() },
            onToggleShowEnglish = { settingsViewModel.toggleShowEnglish() },
            onSaveProgress = { sNum, aNum, ar, en, total ->
                quranViewModel.saveProgress(sNum, aNum, ar, en, total)
            }
        )
    } else if (inAboutScreen) {
        AboutScreen(
            strings = strings,
            onBackClick = { inAboutScreen = false }
        )
    } else {
        Scaffold(
            bottomBar = {
                Column {
                    // Floating Audio Bar when audio playback is active
                    AnimatedVisibility(
                        visible = audioState.surahNumber > 0,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        AudioFloatingBar(
                            state = audioState,
                            onTogglePlayPause = { audioViewModel.togglePlayPause() },
                            onNext = { audioViewModel.nextAyah() },
                            onPrevious = { audioViewModel.previousAyah() },
                            onClose = { audioViewModel.stop() },
                            onBarClick = { showAudioSheet = true }
                        )
                    }

                    // Main Navigation Bar
                    QuranBottomNavBar(
                        selectedTab = currentTab,
                        onTabSelected = { currentTab = it },
                        strings = strings
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentTab) {
                    NavTab.HOME -> {
                        val isDailyBookmarked = bookmarks.any {
                            it.surahNumber == dailyAyah.surahNumber && it.ayahNumber == dailyAyah.ayahNumber
                        }
                        HomeScreen(
                            strings = strings,
                            currentLanguage = currentLanguage,
                            readingProgress = readingProgress,
                            dailyAyah = dailyAyah,
                            surahs = surahs,
                            onOpenSurah = { num ->
                                quranViewModel.openSurah(num, selectedReciterId)
                                inSurahDetail = true
                            },
                            onNavigateToSearch = { currentTab = NavTab.SEARCH },
                            onNavigateToSettings = { currentTab = NavTab.SETTINGS },
                            onNavigateToQuran = { currentTab = NavTab.QURAN },
                            onNavigateToTasbih = { currentTab = NavTab.TASBIH },
                            onCopyDailyAyah = { quranViewModel.copyAyahToClipboard(dailyAyah, true) },
                            onShareDailyAyah = { quranViewModel.shareAyah(dailyAyah, "Surah ${dailyAyah.surahNumber}") },
                            onBookmarkDailyAyah = {
                                quranViewModel.toggleBookmark(
                                    dailyAyah,
                                    "Surah ${dailyAyah.surahNumber}",
                                    "Surah ${dailyAyah.surahNumber}"
                                )
                            },
                            isDailyAyahBookmarked = isDailyBookmarked
                        )
                    }

                    NavTab.QURAN -> {
                        QuranListScreen(
                            strings = strings,
                            surahs = surahs,
                            searchQuery = surahSearchQuery,
                            onSearchQueryChange = { quranViewModel.setSurahSearchQuery(it) },
                            selectedFilter = surahFilter,
                            onFilterChange = { quranViewModel.setSurahFilter(it) },
                            onOpenSurah = { num ->
                                quranViewModel.openSurah(num, selectedReciterId)
                                inSurahDetail = true
                            }
                        )
                    }

                    NavTab.SEARCH -> {
                        SearchScreen(
                            strings = strings,
                            searchQuery = globalSearchQuery,
                            onSearchQueryChange = { quranViewModel.setGlobalSearchQuery(it) },
                            searchResults = globalSearchResults,
                            isSearching = isSearching,
                            onOpenAyah = { sNum, _ ->
                                quranViewModel.openSurah(sNum, selectedReciterId)
                                inSurahDetail = true
                            }
                        )
                    }

                    NavTab.TASBIH -> {
                        TasbihScreen(
                            strings = strings,
                            count = tasbihCount,
                            target = tasbihTarget,
                            presets = tasbihViewModel.presets,
                            selectedPresetIndex = tasbihSelectedPreset,
                            vibrationEnabled = tasbihVibrationEnabled,
                            totalDhikrCompleted = tasbihTotalCompleted,
                            onIncrement = { tasbihViewModel.increment() },
                            onUndo = { tasbihViewModel.undo() },
                            onReset = { tasbihViewModel.reset() },
                            onSelectDhikr = { tasbihViewModel.selectDhikr(it) },
                            onToggleVibration = { tasbihViewModel.toggleVibration() }
                        )
                    }

                    NavTab.BOOKMARKS -> {
                        BookmarksScreen(
                            strings = strings,
                            bookmarks = bookmarks,
                            onOpenAyah = { sNum, _ ->
                                quranViewModel.openSurah(sNum, selectedReciterId)
                                inSurahDetail = true
                            },
                            onRemoveBookmark = { sNum, aNum ->
                                quranViewModel.removeBookmark(sNum, aNum)
                            }
                        )
                    }

                    NavTab.SETTINGS -> {
                        SettingsScreen(
                            strings = strings,
                            currentLanguage = currentLanguage,
                            themeMode = themeMode,
                            arabicFontSize = arabicFontSize,
                            translationFontSize = translationFontSize,
                            lineSpacing = lineSpacing,
                            showSomali = showSomali,
                            showEnglish = showEnglish,
                            selectedReciterId = selectedReciterId,
                            notificationsEnabled = notificationsEnabled,
                            cacheSizeBytes = cacheSizeBytes,
                            onSelectLanguage = { settingsViewModel.setLanguage(it) },
                            onSelectTheme = { settingsViewModel.setThemeMode(it) },
                            onArabicFontSizeChange = { settingsViewModel.setArabicFontSize(it) },
                            onTranslationFontSizeChange = { settingsViewModel.setTranslationFontSize(it) },
                            onLineSpacingChange = { settingsViewModel.setLineSpacing(it) },
                            onToggleShowSomali = { settingsViewModel.toggleShowSomali() },
                            onToggleShowEnglish = { settingsViewModel.toggleShowEnglish() },
                            onSelectReciter = { id, name ->
                                settingsViewModel.setReciter(id, name)
                                audioViewModel.setReciter(id, name)
                            },
                            onToggleNotifications = { settingsViewModel.toggleNotifications() },
                            onClearCache = { settingsViewModel.clearCache() },
                            onNavigateToAbout = { inAboutScreen = true }
                        )
                    }
                }
            }
        }
    }

    // Audio Full Player Sheet
    if (showAudioSheet) {
        AudioPlayerSheet(
            state = audioState,
            onDismiss = { showAudioSheet = false },
            onTogglePlayPause = { audioViewModel.togglePlayPause() },
            onNextAyah = { audioViewModel.nextAyah() },
            onPrevAyah = { audioViewModel.previousAyah() },
            onSeekTo = { audioViewModel.seekTo(it) },
            onSetSpeed = { audioViewModel.setSpeed(it) },
            onToggleRepeatAyah = { audioViewModel.toggleRepeatAyah() },
            onToggleRepeatSurah = { audioViewModel.toggleRepeatSurah() },
            onSelectReciter = { id, name ->
                settingsViewModel.setReciter(id, name)
                audioViewModel.setReciter(id, name)
            }
        )
    }

    // Tafsir Modal Dialog
    tafsirAyah?.let { ayah ->
        TafsirDialog(
            ayah = ayah,
            onDismiss = { quranViewModel.closeTafsir() }
        )
    }
}
