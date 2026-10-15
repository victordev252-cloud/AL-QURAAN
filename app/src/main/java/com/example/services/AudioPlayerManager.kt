package com.example.services

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.data.local.AyahEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

data class AudioPlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val surahNumber: Int = 0,
    val surahName: String = "",
    val currentAyahNumber: Int = 0,
    val currentAyahIndex: Int = 0,
    val totalAyahsInSurah: Int = 0,
    val reciterName: String = "Mishary Rashid Alafasy",
    val reciterId: String = "ar.alafasy",
    val playbackSpeed: Float = 1.0f,
    val repeatAyah: Boolean = false,
    val repeatSurah: Boolean = false,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val errorMessage: String? = null
)

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    private val _playbackState = MutableStateFlow(AudioPlaybackState())
    val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    private var currentPlaylist: List<AyahEntity> = emptyList()
    private var activeSurahName: String = ""

    init {
        setupProgressTracker()
    }

    private fun setupProgressTracker() {
        progressRunnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let { player ->
                    try {
                        if (player.isPlaying) {
                            val cur = player.currentPosition
                            val dur = player.duration
                            _playbackState.value = _playbackState.value.copy(
                                currentPositionMs = cur,
                                durationMs = if (dur > 0) dur else _playbackState.value.durationMs
                            )
                        }
                    } catch (e: Exception) {
                        // Ignored if player released
                    }
                }
                handler.postDelayed(this, 300)
            }
        }
        handler.post(progressRunnable!!)
    }

    fun playAyahList(
        ayahs: List<AyahEntity>,
        startIndex: Int = 0,
        surahName: String = "",
        reciterId: String = _playbackState.value.reciterId,
        reciterName: String = _playbackState.value.reciterName
    ) {
        if (ayahs.isEmpty()) return
        currentPlaylist = ayahs
        activeSurahName = surahName
        val safeIndex = startIndex.coerceIn(0, ayahs.size - 1)
        playAyahAtIndex(safeIndex, reciterId, reciterName)
    }

    fun playAyahAtIndex(index: Int, reciterId: String = _playbackState.value.reciterId, reciterName: String = _playbackState.value.reciterName) {
        if (index !in currentPlaylist.indices) return
        val ayah = currentPlaylist[index]

        releasePlayer()
        _playbackState.value = _playbackState.value.copy(
            isPlaying = false,
            isBuffering = true,
            surahNumber = ayah.surahNumber,
            surahName = activeSurahName,
            currentAyahNumber = ayah.ayahNumber,
            currentAyahIndex = index,
            totalAyahsInSurah = currentPlaylist.size,
            reciterId = reciterId,
            reciterName = reciterName,
            currentPositionMs = 0,
            errorMessage = null
        )

        try {
            // Check if downloaded offline
            val offlineFile = File(context.filesDir, "audio/$reciterId/surah_${ayah.surahNumber}.mp3")
            val audioSource = if (offlineFile.exists()) {
                offlineFile.absolutePath
            } else {
                ayah.audioUrl ?: "https://cdn.islamic.network/quran/audio/128/$reciterId/${ayah.ayahNumber}.mp3"
            }

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioSource)
                setOnPreparedListener { mp ->
                    _playbackState.value = _playbackState.value.copy(
                        isBuffering = false,
                        isPlaying = true,
                        durationMs = mp.duration
                    )
                    applyPlaybackSpeed(_playbackState.value.playbackSpeed)
                    mp.start()
                }
                setOnCompletionListener {
                    handlePlaybackComplete()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayerManager", "MediaPlayer error: what=$what, extra=$extra")
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = false,
                        isBuffering = false,
                        errorMessage = "Audio playback error ($what)"
                    )
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerManager", "Error starting playback", e)
            _playbackState.value = _playbackState.value.copy(
                isPlaying = false,
                isBuffering = false,
                errorMessage = e.message ?: "Failed to play audio"
            )
        }
    }

    private fun handlePlaybackComplete() {
        if (_playbackState.value.repeatAyah) {
            playAyahAtIndex(_playbackState.value.currentAyahIndex)
            return
        }

        val nextIndex = _playbackState.value.currentAyahIndex + 1
        if (nextIndex < currentPlaylist.size) {
            playAyahAtIndex(nextIndex)
        } else if (_playbackState.value.repeatSurah) {
            playAyahAtIndex(0)
        } else {
            _playbackState.value = _playbackState.value.copy(isPlaying = false, currentPositionMs = 0)
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) {
                    player.pause()
                    _playbackState.value = _playbackState.value.copy(isPlaying = false)
                } else {
                    player.start()
                    _playbackState.value = _playbackState.value.copy(isPlaying = true)
                }
            } catch (e: Exception) {
                Log.e("AudioPlayerManager", "Toggle play pause error", e)
            }
        } ?: run {
            if (currentPlaylist.isNotEmpty()) {
                playAyahAtIndex(_playbackState.value.currentAyahIndex)
            }
        }
    }

    fun nextAyah() {
        val nextIdx = _playbackState.value.currentAyahIndex + 1
        if (nextIdx < currentPlaylist.size) {
            playAyahAtIndex(nextIdx)
        }
    }

    fun previousAyah() {
        val prevIdx = _playbackState.value.currentAyahIndex - 1
        if (prevIdx >= 0) {
            playAyahAtIndex(prevIdx)
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.let { player ->
            try {
                player.seekTo(positionMs)
                _playbackState.value = _playbackState.value.copy(currentPositionMs = positionMs)
            } catch (e: Exception) {
                Log.e("AudioPlayerManager", "Seek error", e)
            }
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackState.value = _playbackState.value.copy(playbackSpeed = speed)
        applyPlaybackSpeed(speed)
    }

    private fun applyPlaybackSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        player.playbackParams = PlaybackParams().apply { this.speed = speed }
                    }
                }
            } catch (e: Exception) {
                Log.e("AudioPlayerManager", "Error setting playback speed", e)
            }
        }
    }

    fun toggleRepeatAyah() {
        val newRepeat = !_playbackState.value.repeatAyah
        _playbackState.value = _playbackState.value.copy(
            repeatAyah = newRepeat,
            repeatSurah = if (newRepeat) false else _playbackState.value.repeatSurah
        )
    }

    fun toggleRepeatSurah() {
        val newRepeat = !_playbackState.value.repeatSurah
        _playbackState.value = _playbackState.value.copy(
            repeatSurah = newRepeat,
            repeatAyah = if (newRepeat) false else _playbackState.value.repeatAyah
        )
    }

    fun setReciter(reciterId: String, reciterName: String) {
        _playbackState.value = _playbackState.value.copy(reciterId = reciterId, reciterName = reciterName)
        if (_playbackState.value.isPlaying) {
            playAyahAtIndex(_playbackState.value.currentAyahIndex, reciterId, reciterName)
        }
    }

    fun stop() {
        releasePlayer()
        _playbackState.value = _playbackState.value.copy(isPlaying = false, currentPositionMs = 0)
    }

    private fun releasePlayer() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            // Ignored
        } finally {
            mediaPlayer = null
        }
    }

    fun destroy() {
        progressRunnable?.let { handler.removeCallbacks(it) }
        releasePlayer()
    }
}
