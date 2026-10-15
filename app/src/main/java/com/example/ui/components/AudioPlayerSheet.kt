package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.services.AudioPlaybackState
import com.example.ui.theme.AmiriQuranFontFamily
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerSheet(
    state: AudioPlaybackState,
    onDismiss: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onNextAyah: () -> Unit,
    onPrevAyah: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onToggleRepeatAyah: () -> Unit,
    onToggleRepeatSurah: () -> Unit,
    onSelectReciter: (id: String, name: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sheet Header Drag Handle
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Surah & Ayah Title
            IslamicStarBadge(
                number = state.currentAyahNumber,
                sizeDp = 58,
                badgeColor = QuranGold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.surahName.ifBlank { "Surah ${state.surahNumber}" },
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Ayah ${state.currentAyahNumber} of ${state.totalAyahsInSurah}",
                fontFamily = OutfitFontFamily,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Reciter Switcher Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val isAlafasy = state.reciterId == "ar.alafasy"
                val isAbdulbasit = state.reciterId == "ar.abdulbasitmurattal"

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isAlafasy) PrimaryGreen else Color.Transparent)
                        .clickable { onSelectReciter("ar.alafasy", "Mishary Rashid Alafasy") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mishary Alafasy",
                        fontFamily = OutfitFontFamily,
                        fontWeight = if (isAlafasy) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        color = if (isAlafasy) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isAbdulbasit) PrimaryGreen else Color.Transparent)
                        .clickable { onSelectReciter("ar.abdulbasitmurattal", "Abdul Basit Abdus Samad") }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Abdul Basit",
                        fontFamily = OutfitFontFamily,
                        fontWeight = if (isAbdulbasit) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        color = if (isAbdulbasit) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Progress Slider
            val currentPos = state.currentPositionMs.toFloat()
            val totalDur = if (state.durationMs > 0) state.durationMs.toFloat() else 1f

            Slider(
                value = currentPos.coerceIn(0f, totalDur),
                onValueChange = { onSeekTo(it.toInt()) },
                valueRange = 0f..totalDur,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = QuranGold,
                    activeTrackColor = PrimaryGreen,
                    inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            // Timestamps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatMs(state.currentPositionMs),
                    fontFamily = OutfitFontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatMs(state.durationMs),
                    fontFamily = OutfitFontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Playback Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Repeat Ayah
                IconButton(
                    onClick = onToggleRepeatAyah,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.RepeatOne,
                        contentDescription = "Repeat Ayah",
                        tint = if (state.repeatAyah) QuranGold else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Previous
                IconButton(
                    onClick = onPrevAyah,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Previous Ayah",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Big Play / Pause Button
                if (state.isBuffering) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(68.dp),
                        color = QuranGold,
                        strokeWidth = 3.dp
                    )
                } else {
                    IconButton(
                        onClick = onTogglePlayPause,
                        modifier = Modifier
                            .size(72.dp)
                            .testTag("sheet_play_pause")
                    ) {
                        Icon(
                            imageVector = if (state.isPlaying) Icons.Filled.PauseCircle else Icons.Filled.PlayCircle,
                            contentDescription = if (state.isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(68.dp),
                            tint = PrimaryGreen
                        )
                    }
                }

                // Next
                IconButton(
                    onClick = onNextAyah,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Next Ayah",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Repeat Surah
                IconButton(
                    onClick = onToggleRepeatSurah,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Repeat,
                        contentDescription = "Repeat Surah",
                        tint = if (state.repeatSurah) QuranGold else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Playback Speed Selector (0.75x, 1.0x, 1.25x, 1.5x)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { speed ->
                    val isSelected = state.playbackSpeed == speed
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) QuranGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .clickable { onSetSpeed(speed) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${speed}x",
                            fontFamily = OutfitFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            color = if (isSelected) QuranGold else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun formatMs(ms: Int): String {
    if (ms <= 0) return "0:00"
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return String.format("%d:%02d", min, sec)
}
