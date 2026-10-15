package com.example.data.local

object QuranPrepopulatedData {

    fun getAll114Surahs(): List<SurahEntity> = listOf(
        SurahEntity(1, "الفَاتِحة", "Al-Faatiha", "The Opening", "Furitaanka", "Furitaanka", 7, "Meccan"),
        SurahEntity(2, "البَقَرَة", "Al-Baqarah", "The Cow", "Sacdii", "Sacdii", 286, "Medinan"),
        SurahEntity(3, "آل عِمرَان", "Aal-i-Imraan", "The Family of Imraan", "Reer Cimraan", "Reer Cimraan", 200, "Medinan"),
        SurahEntity(4, "النِّسَاء", "An-Nisaa", "The Women", "Haweenka", "Haweenka", 176, "Medinan"),
        SurahEntity(5, "المَائدة", "Al-Maaida", "The Table", "Miiska", "Miiska Cuntada", 120, "Medinan"),
        SurahEntity(6, "الأنعَام", "Al-An'aam", "The Cattle", "Xoolaha", "Xoolaha", 165, "Meccan"),
        SurahEntity(7, "الأعرَاف", "Al-A'raaf", "The Heights", "Darbiyada Sare", "Darbiyada Sare", 206, "Meccan"),
        SurahEntity(8, "الأنفَال", "Al-Anfaal", "The Spoils of War", "Qaniimada", "Qaniimada Dagaalka", 75, "Medinan"),
        SurahEntity(9, "التَّوبَة", "At-Tawba", "The Repentance", "Toobada", "Toobada", 129, "Medinan"),
        SurahEntity(10, "يُونُس", "Yunus", "Jonah", "Nabi Yuunus", "Nabi Yuunus", 109, "Meccan"),
        SurahEntity(11, "هُود", "Hud", "Hud", "Nabi Huud", "Nabi Huud", 123, "Meccan"),
        SurahEntity(12, "يُوسُف", "Yusuf", "Joseph", "Nabi Yuusuf", "Nabi Yuusuf", 111, "Meccan"),
        SurahEntity(13, "الرَّعْد", "Ar-Ra'd", "The Thunder", "Onkodka", "Onkodka", 43, "Medinan"),
        SurahEntity(14, "إبراهِيم", "Ibrahim", "Abraham", "Nabi Ibraahim", "Nabi Ibraahim", 52, "Meccan"),
        SurahEntity(15, "الحِجْر", "Al-Hijr", "The Rocky Tract", "Dhagaxyada", "Magaalada Al-Hijr", 99, "Meccan"),
        SurahEntity(16, "النَّحْل", "An-Nahl", "The Bee", "Shinnida", "Shinnida", 128, "Meccan"),
        SurahEntity(17, "الإسْرَاء", "Al-Israa", "The Night Journey", "Socodkii Habeenka", "Dheelmankii Habeenkii", 111, "Meccan"),
        SurahEntity(18, "الكَهْف", "Al-Kahf", "The Cave", "Godka", "Godka", 110, "Meccan"),
        SurahEntity(19, "مَريَم", "Maryam", "Mary", "Baryam", "Baryam", 98, "Meccan"),
        SurahEntity(20, "طه", "Taa-Haa", "Ta-Ha", "Daa-Haa", "Daa-Haa", 135, "Meccan"),
        SurahEntity(21, "الأنبيَاء", "Al-Anbiyaa", "The Prophets", "Nabiyada", "Nabiyada", 112, "Meccan"),
        SurahEntity(22, "الحَجّ", "Al-Hajj", "The Pilgrimage", "Xajka", "Xajka", 78, "Medinan"),
        SurahEntity(23, "المُؤمِنُون", "Al-Muminoon", "The Believers", "Mu'miniinta", "Mu'miniinta", 118, "Meccan"),
        SurahEntity(24, "النُّور", "An-Noor", "The Light", "Nuunka", "Nuurka", 64, "Medinan"),
        SurahEntity(25, "الفُرْقَان", "Al-Furqaan", "The Criterion", "Kala-bixiyaha", "Kala-Saaraha", 77, "Meccan"),
        SurahEntity(26, "الشُّعَرَاء", "Ash-Shu'araa", "The Poets", "Gabayada", "Gabayaaga", 227, "Meccan"),
        SurahEntity(27, "النَّمْل", "An-Naml", "The Ant", "Qudhaanjada", "Qudhaanjada", 93, "Meccan"),
        SurahEntity(28, "القَصَص", "Al-Qasas", "The Stories", "Qisooyinka", "Qisooyinka", 88, "Meccan"),
        SurahEntity(29, "العَنكبُوت", "Al-Ankaboot", "The Spider", "Caarada", "Caarada", 69, "Meccan"),
        SurahEntity(30, "الرُّوم", "Ar-Room", "The Romans", "Roomaanka", "Roomaanka", 60, "Meccan"),
        SurahEntity(31, "لُقْمَان", "Luqman", "Luqman", "Luqmaan", "Luqmaan", 34, "Meccan"),
        SurahEntity(32, "السَّجْدَة", "As-Sajda", "The Prostration", "Sujuudda", "Sujuudda", 30, "Meccan"),
        SurahEntity(33, "الأحزَاب", "Al-Ahzaab", "The Combined Forces", "Xulafada", "Kooxaha Xulafada", 73, "Medinan"),
        SurahEntity(34, "سَبَأ", "Saba", "Sheba", "Saba'", "Reer Saba'", 54, "Meccan"),
        SurahEntity(35, "فَاطِر", "Faatir", "The Originator", "Abuuraha", "Abuuraha", 45, "Meccan"),
        SurahEntity(36, "يس", "Yaseen", "Ya-Sin", "Yaasiin", "Yaasiin", 83, "Meccan"),
        SurahEntity(37, "الصَّافَّات", "As-Saaffaat", "Those Drawn Up in Ranks", "Safafka", "Safafka", 182, "Meccan"),
        SurahEntity(38, "ص", "Saad", "The Letter Saad", "Saad", "Xarafka Saad", 88, "Meccan"),
        SurahEntity(39, "الزُّمَر", "Az-Zumar", "The Groups", "Kooxaha", "Kooxaha", 75, "Meccan"),
        SurahEntity(40, "غَافِر", "Ghaafir", "The Forgiver", "Dambi Dhaafaha", "Dambi-Dhaafaha", 85, "Meccan"),
        SurahEntity(41, "فُصِّلَت", "Fussilat", "Explained in Detail", "Kala Caddaynta", "Kala-Bixinta", 54, "Meccan"),
        SurahEntity(42, "الشُّورَى", "Ash-Shoora", "Consultation", "Wadatashiga", "Wadatashiga", 53, "Meccan"),
        SurahEntity(43, "الزُّخْرُف", "Az-Zukhruf", "The Ornaments of Gold", "Dahabka", "Qurxinta Dahabka", 89, "Meccan"),
        SurahEntity(44, "الدُّخَان", "Ad-Dukhaan", "The Smoke", "Qiiqa", "Qiiqa", 59, "Meccan"),
        SurahEntity(45, "الجَاثِية", "Al-Jaathiya", "Crouching", "Jilba-Joogsiga", "Jilba-Joogsiga", 37, "Meccan"),
        SurahEntity(46, "الأحْقَاف", "Al-Ahqaaf", "The Dunes", "Basaasiinta Ciidda", "Tuulmooyinka Ciidda", 35, "Meccan"),
        SurahEntity(47, "مُحَمَّد", "Muhammad", "Muhammad", "Nabi Muxammad", "Nabi Muxammad (SCW)", 38, "Medinan"),
        SurahEntity(48, "الفَتْح", "Al-Fath", "The Victory", "Guusha", "Guusha Cad", 29, "Medinan"),
        SurahEntity(49, "الحُجُرَات", "Al-Hujuraat", "The Rooms", "Qolalka", "Qolalka", 18, "Medinan"),
        SurahEntity(50, "ق", "Qaaf", "The Letter Qaf", "Qaaf", "Xarafka Qaaf", 45, "Meccan"),
        SurahEntity(51, "الذَّارِيَات", "Adh-Dhaariyaat", "The Winnowing Winds", "Dabaylaha Firdhiya", "Dabaylaha", 60, "Meccan"),
        SurahEntity(52, "الطُّور", "At-Toor", "The Mount", "Buurta Dhuur", "Buurta Dhuur", 49, "Meccan"),
        SurahEntity(53, "النَّجْم", "An-Najm", "The Star", "Xiddigta", "Xiddigta", 62, "Meccan"),
        SurahEntity(54, "القَمَر", "Al-Qamar", "The Moon", "Dayaxa", "Dayaxa", 55, "Meccan"),
        SurahEntity(55, "الرَّحْمَن", "Ar-Rahmaan", "The Beneficent", "Naxariistaha", "Naxariistaha Guud", 78, "Medinan"),
        SurahEntity(56, "الوَاقِعَة", "Al-Waaqia", "The Inevitable", "Waqtiga Weyn", "Dhacdada Qiyaame", 96, "Meccan"),
        SurahEntity(57, "الحَدِيد", "Al-Hadeed", "The Iron", "Birta", "Birta", 29, "Medinan"),
        SurahEntity(58, "المُجَادلَة", "Al-Mujaadila", "The Pleading Woman", "Muranta", "Haweentii Doodeysay", 22, "Medinan"),
        SurahEntity(59, "الحَشْر", "Al-Hashr", "The Exile", "Isu-Keenidda", "Qaxinta", 24, "Medinan"),
        SurahEntity(60, "المُمتَحَنَة", "Al-Mumtahana", "She that is to be examined", "Imtixaanka", "Haweenta La Imtixaano", 13, "Medinan"),
        SurahEntity(61, "الصَّفّ", "As-Saff", "The Ranks", "Safafka", "Safafka", 14, "Medinan"),
        SurahEntity(62, "الجُمُعَة", "Al-Jumu'a", "Friday", "Jimcada", "Maalinta Jimcada", 11, "Medinan"),
        SurahEntity(63, "المُنَافِقُون", "Al-Munaafiqoon", "The Hypocrites", "Munaafiqiinta", "Munaafiqiinta", 11, "Medinan"),
        SurahEntity(64, "التَّغَابُن", "At-Taghaabun", "Mutual Disillusion", "Khasaare-Sheegashada", "Khasaaraha Cad", 18, "Medinan"),
        SurahEntity(65, "الطَّلَاق", "At-Talaaq", "Divorce", "Furiinka", "Furiinka", 12, "Medinan"),
        SurahEntity(66, "التَّحْرِيم", "At-Tahreem", "The Prohibition", "Reebista", "Reebista", 12, "Medinan"),
        SurahEntity(67, "المُلْك", "Al-Mulk", "The Sovereignty", "Boqortooyada", "Boqortooyada", 30, "Meccan"),
        SurahEntity(68, "القَلَم", "Al-Qalam", "The Pen", "Qalinka", "Qalinka", 52, "Meccan"),
        SurahEntity(69, "الحَاقَّة", "Al-Haaqqa", "The Reality", "Runtii Sugnayd", "Xaqiiqada", 52, "Meccan"),
        SurahEntity(70, "المَعَارِج", "Al-Ma'aarij", "The Ascending Stairways", "Jaranjarooyinka", "Koreynta", 44, "Meccan"),
        SurahEntity(71, "نُوح", "Nooh", "Noah", "Nabi Nuux", "Nabi Nuux", 28, "Meccan"),
        SurahEntity(72, "الجِنّ", "Al-Jinn", "The Jinn", "Jinka", "Jinka", 28, "Meccan"),
        SurahEntity(73, "المُزَّمِّل", "Al-Muzzammil", "The Enshrouded One", "Isku-Duubaha", "Isku-Duubaha", 20, "Meccan"),
        SurahEntity(74, "المُدَّثِّر", "Al-Muddaththir", "The Cloaked One", "Isku-Dada", "Isku-Dada", 56, "Meccan"),
        SurahEntity(75, "القِيَامَة", "Al-Qiyaama", "The Resurrection", "Qiyaamaha", "Maalinta Qiyaame", 40, "Meccan"),
        SurahEntity(76, "الإنسَان", "Al-Insaan", "Man", "Aadamiga", "Aadanaha", 31, "Medinan"),
        SurahEntity(77, "المُرسَلات", "Al-Mursalaat", "The Emissaries", "Dabaylaha La Diray", "Dabaylaha La Diray", 50, "Meccan"),
        SurahEntity(78, "النَّبَأ", "An-Naba", "The Tidings", "Warka Weyn", "Warka Weyn", 40, "Meccan"),
        SurahEntity(79, "النَّازِعَات", "An-Naazi'aat", "Those who drag forth", "Kuwa Si Xoog Leh U Bixiya", "Malaa'igta", 46, "Meccan"),
        SurahEntity(80, "عَبَس", "Abasa", "He Frowned", "Wuu Waji-Dadbay", "Wajiga Ururinta", 42, "Meccan"),
        SurahEntity(81, "التَّكْوِير", "At-Takweer", "The Overthrowing", "Isku-Duubista", "Isku-Duubidda Qorraxda", 29, "Meccan"),
        SurahEntity(82, "الانفِطَار", "Al-Infitaar", "The Cleaving", "Dilaaca Samada", "Dillaaca Samada", 19, "Meccan"),
        SurahEntity(83, "المُطَفِّفِين", "Al-Mutaffifeen", "Defrauding", "Kuwa Miisaanka Dhima", "Kuwa Wax Dhima", 36, "Meccan"),
        SurahEntity(84, "الانشِقَاق", "Al-Inshiqaaq", "The Splitting Open", "Kala-Dillaaca", "Kala-Dillaaca Samada", 25, "Meccan"),
        SurahEntity(85, "البُرُوج", "Al-Burooj", "The Constellations", "Xiddigaha Weyn", "Burujyada Samada", 22, "Meccan"),
        SurahEntity(86, "الطَّارِق", "At-Taariq", "The Morning Star", "Xiddigta Habeenkii", "Habeen-Socodka", 17, "Meccan"),
        SurahEntity(87, "الأعْلَى", "Al-A'laa", "The Most High", "Kan Sareeya", "Kan Ugu Sareeya", 19, "Meccan"),
        SurahEntity(88, "الغَاشِيَة", "Al-Ghaashiya", "The Overwhelming", "Daboolaha", "Daboolaha Qiyaame", 26, "Meccan"),
        SurahEntity(89, "الفَجْر", "Al-Fajr", "The Dawn", "Waaberiga", "Waaberiga", 30, "Meccan"),
        SurahEntity(90, "البَلَد", "Al-Balad", "The City", "Magaalada", "Magaalada Barakeysan", 20, "Meccan"),
        SurahEntity(91, "الشَّمْس", "Ash-Shams", "The Sun", "Qorraxda", "Qorraxda", 15, "Meccan"),
        SurahEntity(92, "اللَّيْل", "Al-Layl", "The Night", "Habeenka", "Habeenka", 21, "Meccan"),
        SurahEntity(93, "الضُّحَى", "Ad-Dhuhaa", "The Morning Hours", "Barqada", "Barqada", 11, "Meccan"),
        SurahEntity(94, "الشَّرْح", "Ash-Sharh", "The Relief", "Fasaxa Laabta", "Furitaanka Laabta", 8, "Meccan"),
        SurahEntity(95, "التِّين", "At-Teen", "The Fig", "Tiinka", "Tiinka iyo Saytuunka", 8, "Meccan"),
        SurahEntity(96, "العَلَق", "Al-Alaq", "The Clot", "Calowga", "Xinjirta", 19, "Meccan"),
        SurahEntity(97, "القَدْر", "Al-Qadr", "The Power", "Laylatul Qadarka", "Habeenka Qadarka", 5, "Meccan"),
        SurahEntity(98, "البَيِّنَة", "Al-Bayyina", "The Clear Proof", "Caddaynta Cad", "Caddaynta Cad", 8, "Medinan"),
        SurahEntity(99, "الزَّلْزَلَة", "Az-Zalzala", "The Earthquake", "Dhulgariirka", "Dhulgariirka Weyn", 8, "Medinan"),
        SurahEntity(100, "العَادِيَات", "Al-Aadiyaat", "The Courser", "Fardaha Orodka Badan", "Fardaha Duleedka", 11, "Meccan"),
        SurahEntity(101, "القَارِعَة", "Al-Qaari'a", "The Calamity", "Dhawaaqa Weyn", "Dhawaaqa Qiyaame", 11, "Meccan"),
        SurahEntity(102, "التَّكَاثُر", "At-Takaathur", "The Rivalry in World Increase", "Tartanka Badanida", "Tartanka Adduunka", 8, "Meccan"),
        SurahEntity(103, "العَصْر", "Al-Asr", "The Declining Day", "Waqtiga Galabka", "Waqtiga Casirka", 3, "Meccan"),
        SurahEntity(104, "الهُمَزَة", "Al-Humaza", "The Traducer", "Caanaystaha", "Caytamaasha", 9, "Meccan"),
        SurahEntity(105, "الفِيل", "Al-Feel", "The Elephant", "Maroodiga", "Qoladii Maroodiga", 5, "Meccan"),
        SurahEntity(106, "قُرَيْش", "Quraysh", "Quraysh", "Reer Quraysh", "Reer Quraysh", 4, "Meccan"),
        SurahEntity(107, "المَاعُون", "Al-Maa'oon", "Small Kindnesses", "Caawinta Yaryar", "Weelka iyo Gargaarka", 7, "Meccan"),
        SurahEntity(108, "الكَوْثَر", "Al-Kawthar", "Abundance", "Kowthar", "Webiga Kowthar", 3, "Meccan"),
        SurahEntity(109, "الكَافِرُون", "Al-Kaafiroon", "The Disbelievers", "Gaalada", "Gaalada", 6, "Meccan"),
        SurahEntity(110, "النَّصْر", "An-Nasr", "The Divine Support", "Gargaarka Eebbe", "Guusha iyo Gargaarka", 3, "Medinan"),
        SurahEntity(111, "المَسَد", "Al-Masad", "The Palm Fibre", "Xadhigga Xillan", "Geedka Xillan", 5, "Meccan"),
        SurahEntity(112, "الإخْلَاص", "Al-Ikhlaas", "The Sincerity", "Daacadda", "Kelimadda Towxiidka", 4, "Meccan"),
        SurahEntity(113, "الفَلَق", "Al-Falaq", "The Daybreak", "Waaberiga", "Kala-Dillaaca Waaberiga", 5, "Meccan"),
        SurahEntity(114, "النَّاس", "An-Naas", "Mankind", "Dadka", "Dadka Dhammaan", 6, "Meccan")
    )

    fun getInitialPrepopulatedAyahs(): List<AyahEntity> = listOf(
        // Surah 1: Al-Faatiha (Complete 7 Ayahs)
        AyahEntity(
            surahNumber = 1,
            ayahNumber = 1,
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            somaliTranslation = "Magaca Eebe yaan ku bilaabaynaa ee Naxariis guud iyo mid gaaraba Naxariista.",
            englishTranslation = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
            juzNumber = 1,
            pageNumber = 1,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/1.mp3"
        ),
        AyahEntity(
            surahNumber = 1,
            ayahNumber = 2,
            arabicText = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            somaliTranslation = "Mahad Eebaa iska leh ee Barbaariyaha Caalamka ah.",
            englishTranslation = "[All] praise is [due] to Allah, Lord of the worlds -",
            juzNumber = 1,
            pageNumber = 1,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/2.mp3"
        ),
        AyahEntity(
            surahNumber = 1,
            ayahNumber = 3,
            arabicText = "الرَّحْمَٰنِ الرَّحِيمِ",
            somaliTranslation = "Ee Naxariis Guud iyo mid Gaaraba Naxariista.",
            englishTranslation = "The Entirely Merciful, the Especially Merciful,",
            juzNumber = 1,
            pageNumber = 1,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/3.mp3"
        ),
        AyahEntity(
            surahNumber = 1,
            ayahNumber = 4,
            arabicText = "مَالِكِ يَوْمِ الدِّينِ",
            somaliTranslation = "Eena Eebaha Maalinta Abaalmarinta ah.",
            englishTranslation = "Sovereign of the Day of Recompense.",
            juzNumber = 1,
            pageNumber = 1,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/4.mp3"
        ),
        AyahEntity(
            surahNumber = 1,
            ayahNumber = 5,
            arabicText = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            somaliTranslation = "Adiga unbaan ku caabudaynaa, Adigana unbaan Kaalmo ku weydiisanaynaa.",
            englishTranslation = "It is You we worship and You we ask for help.",
            juzNumber = 1,
            pageNumber = 1,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/5.mp3"
        ),
        AyahEntity(
            surahNumber = 1,
            ayahNumber = 6,
            arabicText = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            somaliTranslation = "Nagu Hanuuni Jidka Toosan.",
            englishTranslation = "Guide us to the straight path -",
            juzNumber = 1,
            pageNumber = 1,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6.mp3"
        ),
        AyahEntity(
            surahNumber = 1,
            ayahNumber = 7,
            arabicText = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
            somaliTranslation = "Jidka kuwii aad u Nicmaysay, kuwii aan loo Cadhoyin dhexdayadana Baadiyeysnayn.",
            englishTranslation = "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.",
            juzNumber = 1,
            pageNumber = 1,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/7.mp3"
        ),

        // Ayat Al-Kursi (Surah 2: Ayah 255)
        AyahEntity(
            surahNumber = 2,
            ayahNumber = 255,
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            somaliTranslation = "Eebe ma jiro Eebe kale xaq lagu caabudo Isaga mooyee, waana Noolaha Maamulaha ah, ma qabato Lulmo iyo Hurdo toona, waxaana u sugnaaday waxa Samada ku sugan iyo Dhulka. Waa kuma kan u shafeeco qaadaya agtiisa idankiisa mooyee? Wuxuu ogsoon yahay waxa hortooda ah iyo waxa gadaashooda ah, mana koobaan wax ka mid ah Cilmigiisa wuxuu doono mooyee. Kursigiisu wuxuu waasiciyey Samooyinka iyo Dhulka, dhibina mayso dhowridoodu, waana Sareeyaha Weyn.",
            englishTranslation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of [all] existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is [presently] before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            juzNumber = 3,
            pageNumber = 42,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/262.mp3"
        ),

        // Surah 112: Al-Ikhlas (Complete 4 Ayahs)
        AyahEntity(
            surahNumber = 112,
            ayahNumber = 1,
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ",
            somaliTranslation = "Waxaad dhahdaa: Eebahay waa Mid kaliya.",
            englishTranslation = "Say, \"He is Allah, [who is] One,",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6221.mp3"
        ),
        AyahEntity(
            surahNumber = 112,
            ayahNumber = 2,
            arabicText = "اللَّهُ الصَّمَدُ",
            somaliTranslation = "Eebaa loo baahan yahay dhamaanteed.",
            englishTranslation = "Allah, the Eternal Refuge.",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6222.mp3"
        ),
        AyahEntity(
            surahNumber = 112,
            ayahNumber = 3,
            arabicText = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
            somaliTranslation = "Wax ma dhalin, lamana dhalin Isaga.",
            englishTranslation = "He neither begets nor is born,",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6223.mp3"
        ),
        AyahEntity(
            surahNumber = 112,
            ayahNumber = 4,
            arabicText = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            somaliTranslation = "Eebana cidna uma dhigna mana jiro wax u dhigma.",
            englishTranslation = "Nor is there to Him any equivalent.\"",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6224.mp3"
        ),

        // Surah 113: Al-Falaq (Complete 5 Ayahs)
        AyahEntity(
            surahNumber = 113,
            ayahNumber = 1,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
            somaliTranslation = "Waxaad dhahdaa: waxaan ka magan galay Eebaha Waaberiga.",
            englishTranslation = "Say, \"I seek refuge in the Lord of daybreak",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6225.mp3"
        ),
        AyahEntity(
            surahNumber = 113,
            ayahNumber = 2,
            arabicText = "مِن شَرِّ مَا خَلَقَ",
            somaliTranslation = "Sharkii wuxuu abuuray oo dhan.",
            englishTranslation = "From the evil of that which He created",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6226.mp3"
        ),
        AyahEntity(
            surahNumber = 113,
            ayahNumber = 3,
            arabicText = "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            somaliTranslation = "Iyo sharka Habeenka mugdiga ah markuu dumo.",
            englishTranslation = "And from the evil of darkness when it settles",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6227.mp3"
        ),
        AyahEntity(
            surahNumber = 113,
            ayahNumber = 4,
            arabicText = "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
            somaliTranslation = "Iyo sharka kuwa xargaha ku tufaya (sixiroolayaasha).",
            englishTranslation = "And from the evil of the blowers in knots",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6228.mp3"
        ),
        AyahEntity(
            surahNumber = 113,
            ayahNumber = 5,
            arabicText = "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            somaliTranslation = "Iyo sharka xaasidka markuu wax xaasido.",
            englishTranslation = "And from the evil of an envier when he envies.\"",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6229.mp3"
        ),

        // Surah 114: An-Naas (Complete 6 Ayahs)
        AyahEntity(
            surahNumber = 114,
            ayahNumber = 1,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
            somaliTranslation = "Waxaad dhahdaa: waxaan ka magan galay Eebaha Dadka.",
            englishTranslation = "Say, \"I seek refuge in the Lord of mankind,",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6230.mp3"
        ),
        AyahEntity(
            surahNumber = 114,
            ayahNumber = 2,
            arabicText = "مَلِكِ النَّاسِ",
            somaliTranslation = "Boqorka Dadka.",
            englishTranslation = "The Sovereign of mankind.",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6231.mp3"
        ),
        AyahEntity(
            surahNumber = 114,
            ayahNumber = 3,
            arabicText = "إِلَٰهِ النَّاسِ",
            somaliTranslation = "Ilaaha Dadka ee xaqa ah.",
            englishTranslation = "The God of mankind,",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6232.mp3"
        ),
        AyahEntity(
            surahNumber = 114,
            ayahNumber = 4,
            arabicText = "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
            somaliTranslation = "Sharka waswaasiyaha dhuunta (shaydaanka).",
            englishTranslation = "From the evil of the retreating whisperer -",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6233.mp3"
        ),
        AyahEntity(
            surahNumber = 114,
            ayahNumber = 5,
            arabicText = "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
            somaliTranslation = "Kee wax ku waswaasiya laabaha Dadka.",
            englishTranslation = "Who whispers into the breasts of mankind -",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6234.mp3"
        ),
        AyahEntity(
            surahNumber = 114,
            ayahNumber = 6,
            arabicText = "مِنَ الْجِنَّةِ وَالنَّاسِ",
            somaliTranslation = "Hadday Jinniyiin yihiin iyo hadday Dad yihiinba.",
            englishTranslation = "From among the jinn and mankind.\"",
            juzNumber = 30,
            pageNumber = 604,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6235.mp3"
        ),

        // Surah 36: Yaseen (Ayah 1-5)
        AyahEntity(
            surahNumber = 36,
            ayahNumber = 1,
            arabicText = "يس",
            somaliTranslation = "Yaasiin (Eebaa og micneheeda).",
            englishTranslation = "Ya, Seen.",
            juzNumber = 22,
            pageNumber = 440,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/3706.mp3"
        ),
        AyahEntity(
            surahNumber = 36,
            ayahNumber = 2,
            arabicText = "وَالْقُرْآنِ الْحَكِيمِ",
            somaliTranslation = "Waxaan ku dhaartay Qur'aanka Xigmadda leh.",
            englishTranslation = "By the wise Qur'an.",
            juzNumber = 22,
            pageNumber = 440,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/3707.mp3"
        ),
        AyahEntity(
            surahNumber = 36,
            ayahNumber = 3,
            arabicText = "إِنَّكَ لَمِنَ الْمُرْسَلِينَ",
            somaliTranslation = "Adigu waxaad xaqiiqdii ka mid tahay Rasuullada la diray.",
            englishTranslation = "Indeed you, [O Muhammad], are from among the messengers,",
            juzNumber = 22,
            pageNumber = 440,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/3708.mp3"
        ),

        // Surah 67: Al-Mulk (Ayah 1-2)
        AyahEntity(
            surahNumber = 67,
            ayahNumber = 1,
            arabicText = "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            somaliTranslation = "Waxa barakoobay Eebaha gacantiisa ay ku jirto boqortooyadu, Isaguna wax kasta wuu karaa.",
            englishTranslation = "Blessed is He in whose hand is dominion, and He is over all things competent -",
            juzNumber = 29,
            pageNumber = 562,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/5242.mp3"
        ),
        AyahEntity(
            surahNumber = 67,
            ayahNumber = 2,
            arabicText = "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ",
            somaliTranslation = "Eebaha abuuray Geerida iyo Noloshaba si uu idiin imtixaano kiinna camal wanaagsan, waana Adkaadaha Dambi-Dhaafka badan.",
            englishTranslation = "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -",
            juzNumber = 29,
            pageNumber = 562,
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/5243.mp3"
        )
    )

    val dailyAyahsPool: List<AyahEntity> by lazy {
        getInitialPrepopulatedAyahs().filter { it.ayahNumber > 0 }
    }
}
