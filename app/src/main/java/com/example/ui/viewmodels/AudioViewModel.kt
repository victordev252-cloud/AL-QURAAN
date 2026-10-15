package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.local.AyahEntity
import com.example.services.AudioPlaybackState
import com.example.services.AudioPlayerManager
import kotlinx.coroutines.flow.StateFlow

class AudioViewModel(application: Application) : AndroidViewModel(application) {

    private val playerManager = AudioPlayerManager(application)
    val playbackState: StateFlow<AudioPlaybackState> = playerManager.playbackState

    fun playAyahs(
        ayahs: List<AyahEntity>,
        startIndex: Int = 0,
        surahName: String = "",
        reciterId: String = playbackState.value.reciterId,
        reciterName: String = playbackState.value.reciterName
    ) {
        playerManager.playAyahList(ayahs, startIndex, surahName, reciterId, reciterName)
    }

    fun playAyahAtIndex(index: Int) {
        playerManager.playAyahAtIndex(index)
    }

    fun togglePlayPause() {
        playerManager.togglePlayPause()
    }

    fun nextAyah() {
        playerManager.nextAyah()
    }

    fun previousAyah() {
        playerManager.previousAyah()
    }

    fun seekTo(positionMs: Int) {
        playerManager.seekTo(positionMs)
    }

    fun setSpeed(speed: Float) {
        playerManager.setPlaybackSpeed(speed)
    }

    fun toggleRepeatAyah() {
        playerManager.toggleRepeatAyah()
    }

    fun toggleRepeatSurah() {
        playerManager.toggleRepeatSurah()
    }

    fun setReciter(reciterId: String, reciterName: String) {
        playerManager.setReciter(reciterId, reciterName)
    }

    fun stop() {
        playerManager.stop()
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.destroy()
    }
}
