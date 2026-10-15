package com.example

import com.example.core.localization.AppLanguage
import com.example.core.localization.LocalizationManager
import com.example.core.utils.IslamicCalendarUtils
import com.example.data.api.AlQuranApiResponse
import com.example.data.api.ApiAyahItem
import com.example.data.api.ApiSurahDetailData
import com.example.data.local.QuranPrepopulatedData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuranKarimUnitTest {

    @Test
    fun testAll114SurahsCompleteness() {
        val surahs = QuranPrepopulatedData.getAll114Surahs()
        assertEquals("There must be exactly 114 surahs in the Quran", 114, surahs.size)

        // Verify sequential numbering from 1 to 114
        surahs.forEachIndexed { index, surah ->
            assertEquals(index + 1, surah.number)
            assertTrue("Arabic name must not be blank", surah.nameArabic.isNotBlank())
            assertTrue("Transliteration must not be blank", surah.nameTransliteration.isNotBlank())
            assertTrue("English meaning must not be blank", surah.englishMeaning.isNotBlank())
            assertTrue("Somali meaning must not be blank", surah.somaliMeaning.isNotBlank())
            assertTrue("Ayah count must be greater than 0", surah.ayahCount > 0)
            assertTrue(
                "Revelation type must be Meccan or Medinan",
                surah.revelationType == "Meccan" || surah.revelationType == "Medinan"
            )
        }

        // Verify key surahs
        assertEquals("Al-Faatiha", surahs[0].nameTransliteration)
        assertEquals(7, surahs[0].ayahCount)
        assertEquals("Al-Baqarah", surahs[1].nameTransliteration)
        assertEquals(286, surahs[1].ayahCount)
        assertEquals("An-Naas", surahs[113].nameTransliteration)
        assertEquals(6, surahs[113].ayahCount)
    }

    @Test
    fun testPrepopulatedAyahsIntegrity() {
        val ayahs = QuranPrepopulatedData.getInitialPrepopulatedAyahs()
        assertTrue("Prepopulated ayahs must not be empty", ayahs.isNotEmpty())

        val fatihaAyahs = ayahs.filter { it.surahNumber == 1 }
        assertEquals("Surah Al-Fatiha must have 7 ayahs", 7, fatihaAyahs.size)
        assertEquals("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", fatihaAyahs[0].arabicText)

        val ikhlasAyahs = ayahs.filter { it.surahNumber == 112 }
        assertEquals("Surah Al-Ikhlas must have 4 ayahs", 4, ikhlasAyahs.size)

        val falaqAyahs = ayahs.filter { it.surahNumber == 113 }
        assertEquals("Surah Al-Falaq must have 5 ayahs", 5, falaqAyahs.size)

        val nasAyahs = ayahs.filter { it.surahNumber == 114 }
        assertEquals("Surah An-Nas must have 6 ayahs", 6, nasAyahs.size)
    }

    @Test
    fun testLocalizationStringsResolution() {
        val en = LocalizationManager.getStrings(AppLanguage.ENGLISH)
        assertEquals("Quran Karim", en.appName)
        assertEquals("Settings", en.settings)
        assertEquals("Read, Reflect, Remember", en.subtitle)

        val so = LocalizationManager.getStrings(AppLanguage.SOMALI)
        assertEquals("Qur'aan Kariim", so.appName)
        assertEquals("Dejinta", so.settings)

        val ar = LocalizationManager.getStrings(AppLanguage.ARABIC)
        assertEquals("القرآن الكريم", ar.appName)
        assertEquals("الإعدادات", ar.settings)
    }

    @Test
    fun testIslamicHijriCalendar() {
        val hijriDate = IslamicCalendarUtils.getTodayHijri()
        assertTrue("Hijri day must be between 1 and 30", hijriDate.day in 1..30)
        assertTrue("Hijri month must be between 1 and 12", hijriDate.month in 1..12)
        assertTrue("Hijri year must be reasonable", hijriDate.year >= 1445)
        assertTrue("Hijri Arabic month name must not be blank", hijriDate.monthNameArabic.isNotBlank())
        assertTrue("Hijri English month name must not be blank", hijriDate.monthNameEnglish.isNotBlank())
        assertTrue("Hijri Somali month name must not be blank", hijriDate.monthNameSomali.isNotBlank())
    }

    @Test
    fun testApiJsonParsing() {
        val sampleJson = """
            {
                "code": 200,
                "status": "OK",
                "data": {
                    "number": 1,
                    "name": "سُورَةُ ٱلْفَاتِحَةِ",
                    "englishName": "Al-Faatiha",
                    "englishNameTranslation": "The Opening",
                    "revelationType": "Meccan",
                    "numberOfAyahs": 7,
                    "ayahs": [
                        {
                            "number": 1,
                            "text": "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                            "numberInSurah": 1,
                            "juz": 1,
                            "manzil": 1,
                            "page": 1,
                            "audio": "https://cdn.islamic.network/quran/audio/128/ar.alafasy/1.mp3",
                            "audioSecondary": []
                        }
                    ]
                }
            }
        """.trimIndent()

        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(AlQuranApiResponse::class.java, ApiSurahDetailData::class.java)
        val adapter = moshi.adapter<AlQuranApiResponse<ApiSurahDetailData>>(type)

        val response = adapter.fromJson(sampleJson)
        assertNotNull("Response should parse successfully", response)
        assertEquals(200, response?.code)
        assertEquals("OK", response?.status)
        assertEquals(1, response?.data?.number)
        assertEquals("Al-Faatiha", response?.data?.englishName)
        assertEquals(1, response?.data?.ayahs?.size)
        assertEquals(1, response?.data?.ayahs?.get(0)?.numberInSurah)
        assertEquals("https://cdn.islamic.network/quran/audio/128/ar.alafasy/1.mp3", response?.data?.ayahs?.get(0)?.audio)
    }

    @Test
    fun testSurahFilteringLogic() {
        val surahs = QuranPrepopulatedData.getAll114Surahs()

        val makkiSurahs = surahs.filter { it.revelationType.equals("Meccan", ignoreCase = true) }
        val madaniSurahs = surahs.filter { it.revelationType.equals("Medinan", ignoreCase = true) }

        assertEquals(114, makkiSurahs.size + madaniSurahs.size)
        assertTrue("Makki surahs must be majority", makkiSurahs.size > madaniSurahs.size)

        // Search by query "Yaseen"
        val query = "yaseen"
        val matched = surahs.filter { it.nameTransliteration.lowercase().contains(query) }
        assertEquals(1, matched.size)
        assertEquals(36, matched[0].number)
    }
}
