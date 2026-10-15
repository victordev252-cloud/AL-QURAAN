package com.example.ui.viewmodels

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TasbihSessionEntity
import com.example.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DhikrPreset(
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val defaultTarget: Int = 33
)

class TasbihViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuranRepository(application)
    private val prefs = application.getSharedPreferences("quran_tasbih_prefs", Context.MODE_PRIVATE)

    val presets = listOf(
        DhikrPreset("سُبْحَانَ اللَّهِ", "SubhanAllah", "Glory be to Allah", 33),
        DhikrPreset("الْحَمْدُ لِلَّهِ", "Alhamdulillah", "All praise is for Allah", 33),
        DhikrPreset("اللَّهُ أَكْبَرُ", "Allahu Akbar", "Allah is the Greatest", 34),
        DhikrPreset("أَسْتَغْفِرُ اللَّهَ", "Astaghfirullah", "I seek forgiveness from Allah", 100),
        DhikrPreset("لَا إِلَٰهَ إِلَّا اللَّهُ", "La ilaha illallah", "There is no deity except Allah", 100),
        DhikrPreset("اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ", "Allahumma Salli Ala Muhammad", "Blessings upon Prophet Muhammad", 100)
    )

    private val _currentDhikrIndex = MutableStateFlow(prefs.getInt("current_dhikr_index", 0))
    val currentDhikrIndex: StateFlow<Int> = _currentDhikrIndex.asStateFlow()

    private val _count = MutableStateFlow(prefs.getInt("tasbih_count", 0))
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _target = MutableStateFlow(prefs.getInt("tasbih_target", 33))
    val target: StateFlow<Int> = _target.asStateFlow()

    private val _vibrationEnabled = MutableStateFlow(prefs.getBoolean("vibrate_enabled", true))
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled.asStateFlow()

    // History & Stats
    val recentSessions: StateFlow<List<TasbihSessionEntity>> = repository.getRecentTasbihSessionsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalDhikrCount: StateFlow<Int> = repository.getTotalDhikrCountFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun increment() {
        val newCount = _count.value + 1
        _count.value = newCount
        prefs.edit().putInt("tasbih_count", newCount).apply()

        triggerVibrate(isTargetReached = newCount % _target.value == 0)

        // If target reached, save session
        if (newCount % _target.value == 0) {
            val currentPreset = presets.getOrNull(_currentDhikrIndex.value)
            val dhikrName = currentPreset?.transliteration ?: "Dhikr"
            viewModelScope.launch {
                repository.saveTasbihSession(dhikrName, _target.value, _target.value)
            }
        }
    }

    fun undo() {
        if (_count.value > 0) {
            val newCount = _count.value - 1
            _count.value = newCount
            prefs.edit().putInt("tasbih_count", newCount).apply()
        }
    }

    fun reset() {
        val currentPreset = presets.getOrNull(_currentDhikrIndex.value)
        val dhikrName = currentPreset?.transliteration ?: "Dhikr"
        if (_count.value > 0) {
            viewModelScope.launch {
                repository.saveTasbihSession(dhikrName, _count.value, _target.value)
            }
        }
        _count.value = 0
        prefs.edit().putInt("tasbih_count", 0).apply()
    }

    fun selectDhikr(index: Int) {
        if (index in presets.indices) {
            _currentDhikrIndex.value = index
            val preset = presets[index]
            _target.value = preset.defaultTarget
            _count.value = 0
            prefs.edit()
                .putInt("current_dhikr_index", index)
                .putInt("tasbih_target", preset.defaultTarget)
                .putInt("tasbih_count", 0)
                .apply()
        }
    }

    fun setTarget(newTarget: Int) {
        if (newTarget > 0) {
            _target.value = newTarget
            prefs.edit().putInt("tasbih_target", newTarget).apply()
        }
    }

    fun toggleVibration() {
        val enabled = !_vibrationEnabled.value
        _vibrationEnabled.value = enabled
        prefs.edit().putBoolean("vibrate_enabled", enabled).apply()
    }

    private fun triggerVibrate(isTargetReached: Boolean) {
        if (!_vibrationEnabled.value || vibrator == null || !vibrator.hasVibrator()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = if (isTargetReached) {
                    VibrationEffect.createWaveform(longArrayOf(0, 100, 80, 150), -1)
                } else {
                    VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(if (isTargetReached) 250 else 35)
            }
        } catch (e: Exception) {
            // Ignored if device does not support
        }
    }
}
