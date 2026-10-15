package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.AyahEntity
import com.example.ui.theme.AmiriQuranFontFamily
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.QuranGold

@Composable
fun TafsirDialog(
    ayah: AyahEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with title and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Tafsir Ibn Kathir (تفسير ابن كثير)",
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Ayah Header Reference
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(QuranGold.copy(alpha = 0.12f))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "Surah ${ayah.surahNumber}, Ayah ${ayah.ayahNumber}",
                            fontFamily = OutfitFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = QuranGold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = ayah.arabicText,
                            fontFamily = AmiriQuranFontFamily,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tafsir Commentary Body
                Text(
                    text = "Source: Tafsir Ibn Kathir (Abridged)",
                    fontFamily = OutfitFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = PrimaryGreen
                )

                Spacer(modifier = Modifier.height(6.dp))

                val tafsirExplanation = when (ayah.surahNumber) {
                    1 -> when (ayah.ayahNumber) {
                        1 -> "Basmalah is a verse revealed at the start of every chapter except Surah At-Tawbah. It invokes Allah by His two greatest names of Mercy: Ar-Rahman (the Vastly Merciful to all creation) and Ar-Rahim (the Especially Merciful to believers)."
                        2 -> "Praise is due to Allah alone. 'Rabb al-Alamin' denotes that He is the Creator, Sustainer, Provider, and Owner of all beings across all worlds and creations."
                        5 -> "'Iyyaka Na'budu' (You alone we worship) embodies complete sincerity and detachment from Shirk, while 'wa Iyyaka Nasta'in' (You alone we ask for help) signifies complete reliance (Tawakkul) upon Allah."
                        else -> "This noble Surah encompasses the foundational tenets of Tawhid, guidance, supplication, and adherence to the Straight Path of the righteous."
                    }
                    2 -> when (ayah.ayahNumber) {
                        255 -> "Ayat Al-Kursi (The Throne Verse) is the greatest verse in the Book of Allah as confirmed by Prophet Muhammad (peace be upon him). It affirms Allah's eternal life (Al-Hayy), absolute sustenance of creation (Al-Qayyum), sovereign ownership of the heavens and the earth, and supreme transcendence."
                        else -> "Surah Al-Baqarah was revealed in Madinah and comprehensively outlines legislation, prayer, charity, fasting, pilgrimage, and stories of ancient nations as moral guidance."
                    }
                    112 -> "Surah Al-Ikhlas is equal to one-third of the Quran in reward and meaning, as it exclusively proclaims the oneness, indivisibility, and absolute perfection of Allah (Al-Ahad, As-Samad)."
                    113 -> "Surah Al-Falaq commands the believer to seek sanctuary in Allah, the Lord of daybreak, from the evils of darkness, malicious sorcery, and destructive envy."
                    114 -> "Surah An-Nas seeks refuge in the Creator, King, and True God of humanity from the subtle whispered insinuations of Shaytan that retreat when Allah is remembered."
                    else -> "This ayah belongs to Surah ${ayah.surahNumber}. Authentic scholars of Tafsir, notably Imam Ibn Kathir, emphasize reflecting upon its commands, divine signs, and heartfelt reminders."
                }

                Text(
                    text = tafsirExplanation,
                    fontFamily = OutfitFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Open verified comprehensive tafsir online button
                Button(
                    onClick = {
                        val url = "https://quran.com/${ayah.surahNumber}:${ayah.ayahNumber}/tafsirs/en-tafisr-ibn-kathir"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.OpenInBrowser,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Read Full Tafsir on Quran.com",
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
