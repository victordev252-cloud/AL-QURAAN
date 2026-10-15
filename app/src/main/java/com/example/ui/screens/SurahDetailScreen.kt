package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.localization.Strings
import com.example.data.local.AyahEntity
import com.example.data.local.BookmarkEntity
import com.example.data.local.SurahEntity
import com.example.ui.components.AyahCard
import com.example.ui.components.BismillahBanner
import com.example.ui.components.IslamicStarBadge
import com.example.ui.theme.AmiriQuranFontFamily
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.QuranGold
import com.example.ui.viewmodels.SurahDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    state: SurahDetailUiState,
    strings: Strings,
    playingAyahNumber: Int,
    isAudioPlaying: Boolean,
    bookmarks: List<BookmarkEntity>,
    downloadProgress: Float?,
    arabicFontSizeSp: Float,
    translationFontSizeSp: Float,
    lineSpacingSp: Float,
    showSomali: Boolean,
    showEnglish: Boolean,
    onBackClick: () -> Unit,
    onPlayAyah: (List<AyahEntity>, Int, String) -> Unit,
    onToggleBookmark: (AyahEntity, String, String) -> Unit,
    onCopyArabic: (AyahEntity) -> Unit,
    onCopyTranslation: (AyahEntity) -> Unit,
    onShareAyah: (AyahEntity, String) -> Unit,
    onOpenTafsir: (AyahEntity) -> Unit,
    onDownloadSurahAudio: (Int) -> Unit,
    onRetry: () -> Unit,
    onFontSizeChange: (Float) -> Unit,
    onToggleShowSomali: () -> Unit,
    onToggleShowEnglish: () -> Unit,
    onSaveProgress: (surahNumber: Int, ayahNumber: Int, surahArabic: String, surahEnglish: String, totalAyahs: Int) -> Unit
) {
    BackHandler { onBackClick() }

    val listState = rememberLazyListState()
    var showQuickSettings by remember { mutableStateOf(false) }

    // Auto-scroll to currently playing ayah
    LaunchedEffect(playingAyahNumber) {
        if (playingAyahNumber > 0 && state is SurahDetailUiState.Success) {
            val idx = state.ayahs.indexOfFirst { it.ayahNumber == playingAyahNumber }
            if (idx >= 0) {
                // Offset by 2 items (header + bismillah)
                listState.animateScrollToItem(idx + 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state is SurahDetailUiState.Success) {
                        Column {
                            Text(
                                text = state.surah.nameTransliteration,
                                fontFamily = OutfitFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${state.surah.englishMeaning} • ${state.surah.ayahCount} ${strings.ayahsCount}",
                                fontFamily = OutfitFontFamily,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Text(text = strings.quran, fontFamily = OutfitFontFamily)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (state is SurahDetailUiState.Success) {
                        // Quick Reading Settings Toggle
                        IconButton(onClick = { showQuickSettings = !showQuickSettings }) {
                            Icon(
                                imageVector = Icons.Filled.FormatSize,
                                contentDescription = "Font Settings",
                                tint = if (showQuickSettings) PrimaryGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Download Audio Button
                        if (downloadProgress != null) {
                            CircularProgressIndicator(
                                progress = { downloadProgress },
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(6.dp),
                                color = QuranGold,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            IconButton(onClick = { onDownloadSurahAudio(state.surah.number) }) {
                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    contentDescription = "Download Audio",
                                    tint = QuranGold
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Collapsible Quick Settings Toolbar
            if (showQuickSettings) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    tonalElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Font Size: ${arabicFontSizeSp.toInt()}sp",
                                fontFamily = OutfitFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = onToggleShowSomali,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (showSomali) PrimaryGreen else MaterialTheme.colorScheme.surface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Somali",
                                        fontFamily = OutfitFontFamily,
                                        fontSize = 11.sp,
                                        color = if (showSomali) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Button(
                                    onClick = onToggleShowEnglish,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (showEnglish) PrimaryGreen else MaterialTheme.colorScheme.surface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "English",
                                        fontFamily = OutfitFontFamily,
                                        fontSize = 11.sp,
                                        color = if (showEnglish) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Slider(
                            value = arabicFontSizeSp,
                            onValueChange = onFontSizeChange,
                            valueRange = 20f..38f,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryGreen,
                                activeTrackColor = PrimaryGreen
                            )
                        )
                    }
                }
            }

            when (state) {
                is SurahDetailUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = PrimaryGreen)
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Loading verses...",
                                fontFamily = OutfitFontFamily,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                is SurahDetailUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            fontFamily = OutfitFontFamily,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                        ) {
                            Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = strings.retry, fontFamily = OutfitFontFamily)
                        }
                    }
                }

                is SurahDetailUiState.Success -> {
                    val surah = state.surah
                    val ayahs = state.ayahs

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("surah_detail_list"),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Surah Header Banner
                        item {
                            SurahHeroBanner(
                                surah = surah,
                                onPlaySurah = { onPlayAyah(ayahs, 0, surah.nameTransliteration) }
                            )
                        }

                        // Bismillah (Omit for Surah 9 At-Tawbah, and omit if Surah 1 since verse 1 is Bismillah)
                        if (surah.number != 9 && surah.number != 1) {
                            item {
                                BismillahBanner()
                            }
                        }

                        // Ayahs List
                        itemsIndexed(ayahs, key = { _, ayah -> "${ayah.surahNumber}_${ayah.ayahNumber}" }) { index, ayah ->
                            val isPlaying = isAudioPlaying && playingAyahNumber == ayah.ayahNumber
                            val isBookmarked = bookmarks.any {
                                it.surahNumber == ayah.surahNumber && it.ayahNumber == ayah.ayahNumber
                            }

                            AyahCard(
                                ayah = ayah,
                                surahNameArabic = surah.nameArabic,
                                surahNameEnglish = surah.nameTransliteration,
                                isPlaying = isPlaying,
                                isBookmarked = isBookmarked,
                                arabicFontSizeSp = arabicFontSizeSp,
                                translationFontSizeSp = translationFontSizeSp,
                                lineSpacingSp = lineSpacingSp,
                                showSomali = showSomali,
                                showEnglish = showEnglish,
                                onPlayClick = {
                                    onPlayAyah(ayahs, index, surah.nameTransliteration)
                                    onSaveProgress(surah.number, ayah.ayahNumber, surah.nameArabic, surah.nameTransliteration, surah.ayahCount)
                                },
                                onBookmarkClick = {
                                    onToggleBookmark(ayah, surah.nameArabic, surah.nameTransliteration)
                                },
                                onCopyArabic = { onCopyArabic(ayah) },
                                onCopyTranslation = { onCopyTranslation(ayah) },
                                onShareClick = { onShareAyah(ayah, surah.nameTransliteration) },
                                onTafsirClick = { onOpenTafsir(ayah) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(110.dp))
                        }
                    }
                }

                SurahDetailUiState.Idle -> {}
            }
        }
    }
}

@Composable
fun SurahHeroBanner(
    surah: SurahEntity,
    onPlaySurah: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Arabic Surah Name Calligraphy
                Text(
                    text = surah.nameArabic,
                    fontFamily = AmiriQuranFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = surah.nameTransliteration,
                    fontFamily = OutfitFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = QuranGold
                )

                Text(
                    text = "${surah.englishMeaning} • ${surah.somaliMeaning}",
                    fontFamily = OutfitFontFamily,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isMakki = surah.revelationType.equals("Meccan", ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isMakki) "Makki" else "Madani",
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "•  ${surah.ayahCount} Ayahs",
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onPlaySurah,
                    colors = ButtonDefaults.buttonColors(containerColor = QuranGold),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Play Surah Recitation",
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DarkGreen
                    )
                }
            }
        }
    }
}
