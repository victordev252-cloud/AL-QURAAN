package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmiriQuranFontFamily
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldLight
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Islamic 8-Point Star (Rub el Hizb) decorative badge for Ayah and Surah numbers.
 */
@Composable
fun IslamicStarBadge(
    number: Int,
    modifier: Modifier = Modifier,
    sizeDp: Int = 42,
    badgeColor: Color = QuranGold,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier.size(sizeDp.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val outerRadius = size.width / 2f - 2f
            val innerRadius = outerRadius * 0.65f

            val path = Path()
            val points = 16
            val angleStep = (2 * PI / points).toFloat()

            for (i in 0 until points) {
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val angle = i * angleStep - (PI / 2).toFloat()
                val x = center.x + r * cos(angle)
                val y = center.y + r * sin(angle)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            drawPath(
                path = path,
                color = badgeColor.copy(alpha = 0.2f)
            )
            drawPath(
                path = path,
                color = badgeColor,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        Text(
            text = "$number",
            fontFamily = OutfitFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (sizeDp * 0.36f).sp,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Bismillah Calligraphy Header Banner for Surah reading.
 */
@Composable
fun BismillahBanner(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryGreen.copy(alpha = 0.08f))
            .border(1.dp, QuranGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            fontFamily = AmiriQuranFontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )
    }
}
