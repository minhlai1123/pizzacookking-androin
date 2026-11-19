package com.antigravity.pizzacooking.presentation.theme

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Procedural low-latency sound effects generator for Pizza Cooking World.
 * Generates playful cartoon audio feedback in real-time with zero external files or permissions.
 */
object SoundEffects {

    private val scope = CoroutineScope(Dispatchers.Default)
    private const val SAMPLE_RATE = 22050

    var isMuted: Boolean = false
        set(value) {
            field = value
            if (value) {
                stopBgm()
            } else {
                startBgm()
            }
        }

    private var bgmTrack: AudioTrack? = null
    private var isBgmPlaying: Boolean = false

    /**
     * Starts cheerful, procedural Italian cartoon pizzeria background music.
     * Uses looped PCM synthesis with zero external files or network requests.
     */
    fun startBgm() {
        if (isMuted || isBgmPlaying) return
        scope.launch {
            try {
                if (bgmTrack == null) {
                    val bgmBuffer = generateBgmBuffer()
                    val track = AudioTrack.Builder()
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_GAME)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        .setAudioFormat(
                            AudioFormat.Builder()
                                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                .setSampleRate(SAMPLE_RATE)
                                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                .build()
                        )
                        .setBufferSizeInBytes(bgmBuffer.size * 2)
                        .setTransferMode(AudioTrack.MODE_STATIC)
                        .build()

                    track.write(bgmBuffer, 0, bgmBuffer.size)
                    track.setLoopPoints(0, bgmBuffer.size, -1) // Infinite smooth loop
                    bgmTrack = track
                }

                bgmTrack?.let {
                    it.play()
                    isBgmPlaying = true
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Pauses procedural background music.
     */
    fun stopBgm() {
        try {
            bgmTrack?.pause()
            isBgmPlaying = false
        } catch (_: Exception) {}
    }

    /**
     * Synthesizes an upbeat, cheerful, kid-friendly Italian cartoon pizzeria theme (6.4s loop).
     */
    private fun generateBgmBuffer(): ShortArray {
        // Cheerful 32-note upbeat melody in C Major (150 BPM, 200ms per 8th note)
        val melodyNotes = doubleArrayOf(
            261.63, 329.63, 392.00, 392.00, // C4 - E4 - G4 - G4
            440.00, 392.00, 329.63, 261.63, // A4 - G4 - E4 - C4
            293.66, 349.23, 440.00, 392.00, // D4 - F4 - A4 - G4
            329.63, 293.66, 261.63, 196.00, // E4 - D4 - C4 - G3
            261.63, 329.63, 392.00, 523.25, // C4 - E4 - G4 - C5
            493.88, 440.00, 392.00, 329.63, // B4 - A4 - G4 - E4
            349.23, 293.66, 392.00, 246.94, // F4 - D4 - G4 - B3
            261.63, 329.63, 261.63, 0.0     // C4 - E4 - C4 - Rest
        )

        val bassNotes = doubleArrayOf(
            130.81, 130.81, 196.00, 196.00,
            174.61, 174.61, 196.00, 196.00,
            146.83, 146.83, 196.00, 196.00,
            130.81, 130.81, 196.00, 196.00,
            130.81, 130.81, 196.00, 196.00,
            174.61, 174.61, 196.00, 196.00,
            146.83, 146.83, 196.00, 196.00,
            130.81, 130.81, 130.81, 0.0
        )

        val noteDurationMs = 200
        val noteSamples = noteDurationMs * SAMPLE_RATE / 1000
        val totalSamples = noteSamples * melodyNotes.size
        val buffer = ShortArray(totalSamples)

        for (n in melodyNotes.indices) {
            val melFreq = melodyNotes[n]
            val bassFreq = bassNotes[n]
            val startIdx = n * noteSamples

            for (i in 0 until noteSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val prog = i.toDouble() / noteSamples
                // Plucky, percussive marimba/accordion envelope
                val melEnv = exp(-prog * 4.2)
                val bassEnv = exp(-prog * 3.0)

                var melSample = 0.0
                if (melFreq > 0.0) {
                    // Fundamental + subtle playful overtone
                    melSample = (sin(2.0 * PI * melFreq * t) * 0.75 +
                            sin(4.0 * PI * melFreq * t) * 0.25) * melEnv * 9000.0
                }

                var bassSample = 0.0
                if (bassFreq > 0.0) {
                    bassSample = sin(2.0 * PI * bassFreq * t) * bassEnv * 6500.0
                }

                val mixed = melSample + bassSample
                buffer[startIdx + i] = mixed.toInt().coerceIn(-32767, 32767).toShort()
            }
        }
        return buffer
    }

    fun playPop() {
        if (isMuted) return
        scope.launch {
            // Rising chirp (400Hz to 850Hz over 70ms)
            val durationMs = 70
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val freq = 400.0 + 450.0 * progress
                val envelope = 1.0 - progress
                val sample = sin(2.0 * PI * freq * t) * envelope * 24000.0
                buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playBoing() {
        if (isMuted) return
        scope.launch {
            // Bouncy spring sound (frequency wobbles 220Hz -> 330Hz -> 180Hz)
            val durationMs = 180
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val wobble = sin(2.0 * PI * 18.0 * t) * 60.0
                val freq = 220.0 + wobble
                val envelope = exp(- progress * 3.5)
                val sample = sin(2.0 * PI * freq * t) * envelope * 28000.0
                buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playWhoosh() {
        if (isMuted) return
        scope.launch {
            // Soft breezy whoosh
            val durationMs = 120
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val progress = i.toDouble() / numSamples
                val noise = (Math.random() - 0.5) * 18000.0
                val envelope = sin(progress * PI)
                buffer[i] = (noise * envelope).toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playRoll() {
        if (isMuted) return
        scope.launch {
            // Rhythmic wooden rumble
            val durationMs = 120
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val freq = 120.0 + sin(2.0 * PI * 25.0 * t) * 30.0
                val envelope = sin(progress * PI)
                val sample = sin(2.0 * PI * freq * t) * envelope * 22000.0
                buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playSplat() {
        if (isMuted) return
        scope.launch {
            // Squishy wet splat
            val durationMs = 100
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val freq = 320.0 - 180.0 * progress
                val noise = (Math.random() - 0.5) * 6000.0
                val envelope = (1.0 - progress) * (1.0 - progress)
                val sample = (sin(2.0 * PI * freq * t) * 20000.0 + noise) * envelope
                buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playGrate() {
        if (isMuted) return
        scope.launch {
            // Crisp metallic scrape
            val durationMs = 90
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val progress = i.toDouble() / numSamples
                val noise = (Math.random() - 0.5) * 28000.0
                val envelope = sin(progress * PI)
                buffer[i] = (noise * envelope).toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playDing() {
        if (isMuted) return
        scope.launch {
            // Sweet oven bell chime (high pure 1320Hz tone ringing out)
            val durationMs = 450
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val t = i.toDouble() / SAMPLE_RATE
                val progress = i.toDouble() / numSamples
                val freq = 1320.0
                val harmonic = sin(2.0 * PI * freq * 2.0 * t) * 0.25
                val envelope = exp(- progress * 4.0)
                val sample = (sin(2.0 * PI * freq * t) + harmonic) * envelope * 28000.0
                buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playCrunch() {
        if (isMuted) return
        scope.launch {
            // Satisfying crispy bite crunch
            val durationMs = 110
            val numSamples = durationMs * SAMPLE_RATE / 1000
            val buffer = ShortArray(numSamples)
            for (i in 0 until numSamples) {
                val progress = i.toDouble() / numSamples
                val noise = (Math.random() - 0.5) * 30000.0
                val envelope = exp(- progress * 6.0)
                buffer[i] = (noise * envelope).toInt().coerceIn(-32767, 32767).toShort()
            }
            playPcmBuffer(buffer)
        }
    }

    fun playFanfare() {
        if (isMuted) return
        scope.launch {
            // Celebratory 3-note arpeggio (C5 -> E5 -> G5 -> C6)
            val notes = listOf(523.25, 659.25, 783.99, 1046.50)
            val noteDurationMs = 120
            val noteSamples = noteDurationMs * SAMPLE_RATE / 1000
            val totalSamples = noteSamples * notes.size
            val buffer = ShortArray(totalSamples)

            for ((index, freq) in notes.withIndex()) {
                val startIdx = index * noteSamples
                for (i in 0 until noteSamples) {
                    val t = i.toDouble() / SAMPLE_RATE
                    val progress = i.toDouble() / noteSamples
                    val envelope = 1.0 - progress * 0.5
                    val sample = sin(2.0 * PI * freq * t) * envelope * 24000.0
                    buffer[startIdx + i] = sample.toInt().coerceIn(-32767, 32767).toShort()
                }
            }
            playPcmBuffer(buffer)
        }
    }

    private fun playPcmBuffer(buffer: ShortArray) {
        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            track.setNotificationMarkerPosition(buffer.size)
            track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(t: AudioTrack?) {
                    try {
                        t?.stop()
                        t?.release()
                    } catch (_: Exception) {}
                }
                override fun onPeriodicNotification(t: AudioTrack?) {}
            })
        } catch (_: Exception) {}
    }
}
