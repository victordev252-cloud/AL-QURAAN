package com.example.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.localization.AppLanguage
import com.example.core.localization.LocalizationManager
import com.example.core.localization.Strings
import com.example.data.repository.QuranRepository
import com.example.services.DailyAyahNotificationReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("quran_settings_prefs", Context.MODE_PRIVATE)
    private val repository = QuranRepository(application)

    // Language
    private val _currentLanguage = MutableStateFlow(
        AppLanguage.fromCode(prefs.getString("app_language", "en") ?: "en")
    )
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    // Strings
    private val _strings = MutableStateFlow(LocalizationManager.getStrings(_currentLanguage.value))
    val strings: StateFlow<Strings> = _strings.asStateFlow()

    // Theme: "SYSTEM", "LIGHT", "DARK"
    private val _themeMode = MutableStateFlow(prefs.getString("theme_mode", "SYSTEM") ?: "SYSTEM")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    // Reading settings
    private val _arabicFontSize = MutableStateFlow(prefs.getFloat("arabic_font_size", 26f))
    val arabicFontSize: StateFlow<Float> = _arabicFontSize.asStateFlow()

    private val _translationFontSize = MutableStateFlow(prefs.getFloat("translation_font_size", 14f))
    val translationFontSize: StateFlow<Float> = _translationFontSize.asStateFlow()

    private val _lineSpacing = MutableStateFlow(prefs.getFloat("line_spacing", 36f))
    val lineSpacing: StateFlow<Float> = _lineSpacing.asStateFlow()

    private val _showSomali = MutableStateFlow(prefs.getBoolean("show_somali", true))
    val showSomali: StateFlow<Boolean> = _showSomali.asStateFlow()

    private val _showEnglish = MutableStateFlow(prefs.getBoolean("show_english", true))
    val showEnglish: StateFlow<Boolean> = _showEnglish.asStateFlow()

    // Audio
    private val _selectedReciterId = MutableStateFlow(prefs.getString("selected_reciter_id", "ar.alafasy") ?: "ar.alafasy")
    val selectedReciterId: StateFlow<String> = _selectedReciterId.asStateFlow()

    private val _selectedReciterName = MutableStateFlow(
        prefs.getString("selected_reciter_name", "Mishary Rashid Alafasy") ?: "Mishary Rashid Alafasy"
    )
    val selectedReciterName: StateFlow<String> = _selectedReciterName.asStateFlow()

    // Notifications
    private val _notificationsEnabled = MutableStateFlow(prefs.getBoolean("notifications_enabled", true))
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _notificationHour = MutableStateFlow(prefs.getInt("notification_hour", 7))
    val notificationHour: StateFlow<Int> = _notificationHour.asStateFlow()

    private val _notificationMinute = MutableStateFlow(prefs.getInt("notification_minute", 0))
    val notificationMinute: StateFlow<Int> = _notificationMinute.asStateFlow()

    // Storage
    private val _cacheSizeBytes = MutableStateFlow(0L)
    val cacheSizeBytes: StateFlow<Long> = _cacheSizeBytes.asStateFlow()

    init {
        refreshCacheSize()
        if (_notificationsEnabled.value) {
            DailyAyahNotificationReceiver.scheduleDailyNotification(
                application,
                _notificationHour.value,
                _notificationMinute.value
            )
        }
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        _strings.value = LocalizationManager.getStrings(language)
        prefs.edit().putString("app_language", language.code).apply()
    }

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
        prefs.edit().putString("theme_mode", mode).apply()
    }

    fun setArabicFontSize(size: Float) {
        _arabicFontSize.value = size
        prefs.edit().putFloat("arabic_font_size", size).apply()
    }

    fun setTranslationFontSize(size: Float) {
        _translationFontSize.value = size
        prefs.edit().putFloat("translation_font_size", size).apply()
    }

    fun setLineSpacing(spacing: Float) {
        _lineSpacing.value = spacing
        prefs.edit().putFloat("line_spacing", spacing).apply()
    }

    fun toggleShowSomali() {
        val newVal = !_showSomali.value
        _showSomali.value = newVal
        prefs.edit().putBoolean("show_somali", newVal).apply()
    }

    fun toggleShowEnglish() {
        val newVal = !_showEnglish.value
        _showEnglish.value = newVal
        prefs.edit().putBoolean("show_english", newVal).apply()
    }

    fun setReciter(id: String, name: String) {
        _selectedReciterId.value = id
        _selectedReciterName.value = name
        prefs.edit()
            .putString("selected_reciter_id", id)
            .putString("selected_reciter_name", name)
            .apply()
    }

    fun toggleNotifications() {
        val enabled = !_notificationsEnabled.value
        _notificationsEnabled.value = enabled
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
        if (enabled) {
            DailyAyahNotificationReceiver.scheduleDailyNotification(
                getApplication(),
                _notificationHour.value,
                _notificationMinute.value
            )
        }
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        _notificationHour.value = hour
        _notificationMinute.value = minute
        prefs.edit()
            .putInt("notification_hour", hour)
            .putInt("notification_minute", minute)
            .apply()
        if (_notificationsEnabled.value) {
            DailyAyahNotificationReceiver.scheduleDailyNotification(getApplication(), hour, minute)
        }
    }

    fun refreshCacheSize() {
        viewModelScope.launch {
            _cacheSizeBytes.value = repository.getAudioCacheSize()
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            repository.clearAudioCache()
            _cacheSizeBytes.value = 0L
        }
    }
}
