package com.fourelements.synth.ui.main

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import androidx.lifecycle.ViewModel
import com.fourelements.synth.Note
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

    private val synth = PlaySynth().apply {
        //start()
    }

    override fun onCleared() {
        super.onCleared()
        //synth.interrupt()
    }

    fun playSynth(freqOfTone: Float) {
        Log.d(TAG, "playSynth()")
        Log.d(TAG, "audioTrackBufferSize $audioTrackBufferSize")
        Log.d(TAG, "samole rate $sampleRate")

        //synth.play(freqOfTone)
    }

    fun stopPlaying() {
        Log.d(TAG, "stopPlaying()")
        //synth.stopPlaying()
    }

    private inner class PlaySynth : Thread() {

        private val isOn = AtomicBoolean(false)
        private var freqOfTone: Float = Note.C5

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
        private var phaseIncrement = 0.0

        private fun calculatePhaseIncrement() {
            phaseIncrement = freqOfTone * TWO_PI / sampleRate
        }

        fun play(freqOfTone: Float) {
            this.freqOfTone = freqOfTone
            calculatePhaseIncrement()
            isOn.set(true)
        }

        fun stopPlaying() {
            isOn.set(false)
        }

        override fun run() {
            Log.d(TAG, "PlaySynth run()")
            audioTrack.play()
            while (!isInterrupted) {
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
                        //Log.d(TAG, "Written $sizeToWrite")
                        bufferSize -= sizeToWrite
                        offset += sizeToWrite
                    }
                }
            }
        }
    }

}