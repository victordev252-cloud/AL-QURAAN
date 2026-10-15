package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AyahEntity
import com.example.ui.theme.AmiriQuranFontFamily
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldLight

@Composable
fun AyahCard(
    ayah: AyahEntity,
    surahNameArabic: String,
    surahNameEnglish: String,
    isPlaying: Boolean,
    isBookmarked: Boolean,
    arabicFontSizeSp: Float,
    translationFontSizeSp: Float,
    lineSpacingSp: Float,
    showSomali: Boolean,
    showEnglish: Boolean,
    onPlayClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onCopyArabic: () -> Unit,
    onCopyTranslation: () -> Unit,
    onShareClick: () -> Unit,
    onTafsirClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isPlaying) QuranGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        label = "ayah_border"
    )

    val cardBgColor by animateColorAsState(
        targetValue = if (isPlaying) QuranGold.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
        label = "ayah_bg"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ayah_card_${ayah.ayahNumber}")
            .border(
                width = if (isPlaying) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Ayah number badge & Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IslamicStarBadge(
                    number = ayah.ayahNumber,
                    sizeDp = 38,
                    badgeColor = if (isPlaying) PrimaryGreen else QuranGold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Play Recitation Button
                    IconButton(
                        onClick = onPlayClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("play_ayah_${ayah.ayahNumber}")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.PauseCircle else Icons.Filled.PlayCircle,
                            contentDescription = if (isPlaying) "Pause Ayah" else "Play Ayah",
                            tint = if (isPlaying) PrimaryGreen else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Bookmark Button
                    IconButton(
                        onClick = onBookmarkClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("bookmark_ayah_${ayah.ayahNumber}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) QuranGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Tafsir Button
                    IconButton(
                        onClick = onTafsirClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("tafsir_ayah_${ayah.ayahNumber}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = "Tafsir",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Copy Arabic
                    IconButton(
                        onClick = onCopyArabic,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Copy Arabic",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Quran Uthmani Text
            Text(
                text = ayah.arabicText,
                fontFamily = AmiriQuranFontFamily,
                fontSize = arabicFontSizeSp.sp,
                lineHeight = lineSpacingSp.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("arabic_text_${ayah.ayahNumber}")
            )

            // Somali Translation
            if (showSomali && ayah.somaliTranslation.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryGreen.copy(alpha = 0.06f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text(
                            text = "Af-Soomaali",
                            fontFamily = OutfitFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ayah.somaliTranslation,
                            fontFamily = OutfitFontFamily,
                            fontSize = translationFontSizeSp.sp,
                            lineHeight = (translationFontSizeSp * 1.4f).sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // English Translation
            if (showEnglish && ayah.englishTranslation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        Text(
                            text = "English (Saheeh Intl)",
                            fontFamily = OutfitFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = ayah.englishTranslation,
                            fontFamily = OutfitFontFamily,
                            fontSize = translationFontSizeSp.sp,
                            lineHeight = (translationFontSizeSp * 1.4f).sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }
    }
}
