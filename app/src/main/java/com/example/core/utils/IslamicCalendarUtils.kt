package com.example.core.utils

import com.example.core.localization.AppLanguage
import java.util.Calendar
import kotlin.math.floor

object IslamicCalendarUtils {

    data class HijriDate(
        val day: Int,
        val month: Int,
        val monthNameArabic: String,
        val monthNameEnglish: String,
        val monthNameSomali: String,
        val year: Int
    ) {
        fun formatted(language: AppLanguage): String {
            return when (language) {
                AppLanguage.ARABIC -> "$day $monthNameArabic $year هـ"
                AppLanguage.SOMALI -> "$day $monthNameSomali $year H"
                AppLanguage.ENGLISH -> "$day $monthNameEnglish $year AH"
            }
        }
    }

    private val ARABIC_MONTHS = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    private val ENGLISH_MONTHS = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Ula", "Jumada al-Akhirah", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    private val SOMALI_MONTHS = listOf(
        "Muxarram", "Safar", "Rabiicul Awal", "Rabiicul Aakhir",
        "Jumaada Hore", "Jumaada Dambe", "Rajab", "Shacbaan",
        "Ramadaan", "Shawaal", "Dul-Qacda", "Dul-Xijjah"
    )

    fun getTodayHijri(): HijriDate {
        val cal = Calendar.getInstance()
        val gYear = cal.get(Calendar.YEAR)
        val gMonth = cal.get(Calendar.MONTH) + 1
        val gDay = cal.get(Calendar.DAY_OF_MONTH)

        return gregorianToHijri(gYear, gMonth, gDay)
    }

    private fun gregorianToHijri(year: Int, month: Int, day: Int): HijriDate {
        // Julian day calculation
        var y = year
        var m = month
        if (m < 3) {
            y -= 1
            m += 12
        }

        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5

        // Approximate Hijri from Julian day
        val epoch = 1948439.5
        val l = jd - epoch + 10632.0
        val n = floor((l - 1) / 10631.0)
        val lPrime = l - 10631.0 * n + 354.0
        val j = (floor((10985.0 - lPrime) / 5316.0)) * (floor((50.0 * lPrime) / 17719.0)) +
                (floor(lPrime / 5670.0)) * (floor((43.0 * lPrime) / 15238.0))
        val lDoublePrime = lPrime - (floor((30.0 - j) / 15.0)) * (floor((17719.0 * j) / 50.0)) -
                (floor(j / 16.0)) * (floor((15238.0 * j) / 43.0)) + 29.0
        val hMonth = floor((24.0 * lDoublePrime) / 709.0).toInt()
        val hDay = (lDoublePrime - floor((709.0 * hMonth) / 24.0)).toInt()
        val hYear = (30.0 * n + j - 30.0).toInt()

        val safeMonth = (hMonth - 1).coerceIn(0, 11)
        val safeDay = hDay.coerceIn(1, 30)

        return HijriDate(
            day = safeDay,
            month = safeMonth + 1,
            monthNameArabic = ARABIC_MONTHS[safeMonth],
            monthNameEnglish = ENGLISH_MONTHS[safeMonth],
            monthNameSomali = SOMALI_MONTHS[safeMonth],
            year = hYear
        )
    }
}
