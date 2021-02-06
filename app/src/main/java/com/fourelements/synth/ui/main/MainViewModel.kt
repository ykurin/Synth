package com.fourelements.synth.ui.main

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil
import kotlin.math.sin


class MainViewModel : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
        private const val TWO_PI = 2 * Math.PI
        private const val AMPLITUDE = 0.3
    }

    private val sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC)
    private val audioTrackBufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_FLOAT
    )


    private val synthThreadsMap: HashMap<Float, PlaySynth> = HashMap()

    override fun onCleared() {
        super.onCleared()
        synthThreadsMap.values.forEach { it.isOn.set(false) }
        synthThreadsMap.clear()
    }

    fun playSynth(freqOfTone: Float) {
        Log.d(TAG, "playSynth()")
        Log.d(TAG, "audioTrackBufferSize $audioTrackBufferSize")
        Log.d(TAG, "samole rate $sampleRate")

        if (!synthThreadsMap.containsKey(freqOfTone)) {
            synthThreadsMap[freqOfTone] = PlaySynth(freqOfTone).apply {
                start()
            }
        }
    }

    fun stopPlaying(freqOfTone: Float) {
        Log.d(TAG, "stopPlaying()")
        synthThreadsMap[freqOfTone]?.also {
            it.isOn.set(false)
            synthThreadsMap.remove(freqOfTone)
        }
    }

    private inner class PlaySynth(freqOfTone: Float) : Thread() {

        var isOn = AtomicBoolean(true)

        private val audioTrack = AudioTrack.Builder()
            .setTransferMode(AudioTrack.MODE_STREAM)
            .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(audioTrackBufferSize)
            .build()

        private var phase = 0.0
        private val phaseIncrement = freqOfTone * TWO_PI / sampleRate

        override fun run() {
            Log.d(TAG, "PlaySynth run()")
            audioTrack.play()
            while (isOn.get()) {
                val sound = generateSynth()
                writeSound(sound)
            }
            Log.d(TAG, "PlaySynth interrupted")
            audioTrack.pause()
            audioTrack.flush()
            audioTrack.release()
        }

        private fun generateSynth(): FloatArray {
            val sound = FloatArray(audioTrackBufferSize * 2)

            for (i in sound.indices) {
                if (isOn.get()) {
                    sound[i] = (sin(phase) * AMPLITUDE).toFloat()
                    phase += phaseIncrement
                    if (phase >= TWO_PI) phase -= TWO_PI
                } else {
                    Log.d(TAG, "generateSynth interrupted")
                    sound[i] = 0f
                }
            }

            return sound
        }

        private fun writeSound(sound: FloatArray) {
            var offset = 0
            var bufferSize = sound.size
            while (bufferSize > 0) {
                val sizeToWrite =
                    if (bufferSize > audioTrackBufferSize) audioTrackBufferSize else bufferSize
                when (audioTrack.write(sound, offset, sizeToWrite, AudioTrack.WRITE_BLOCKING)) {
                    AudioTrack.ERROR_INVALID_OPERATION -> {
                        Log.e(TAG, "AudioTrack.ERROR_INVALID_OPERATION")
                    }
                    AudioTrack.ERROR_BAD_VALUE -> {
                        Log.e(TAG, "AudioTrack.ERROR_BAD_VALUE")
                    }
                    AudioTrack.ERROR_DEAD_OBJECT -> {
                        Log.e(TAG, "AudioTrack.ERROR_DEAD_OBJECT")
                    }
                    AudioTrack.ERROR -> {
                        Log.e(TAG, "AudioTrack.ERROR")
                    }
                    sizeToWrite -> {
                        Log.d(TAG, "Written $sizeToWrite")
                        bufferSize -= sizeToWrite
                        offset += sizeToWrite
                    }
                }
            }
        }
    }

}