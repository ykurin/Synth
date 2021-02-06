package com.fourelements.synth.ui.main

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.fourelements.synth.Note
import com.fourelements.synth.R
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private val TAG = MainFragment::class.java.simpleName
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_fragment, container, false).apply {
        button1.setOnTouchListener(NoteTouchListener(Note.C5))
        button2.setOnTouchListener(NoteTouchListener(Note.E5))
        button3.setOnTouchListener(NoteTouchListener(Note.G5))

        val hasLowLatencyFeature: Boolean =
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY)

        val hasProFeature: Boolean =
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_PRO)

        Log.d(TAG, "hasLowLatencyFeature $hasLowLatencyFeature")
        Log.d(TAG, "hasProFeature $hasProFeature")
    }

    private inner class NoteTouchListener(val freqOfTone : Float) : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (v == null || event == null) {
                return false
            }
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "ACTION_DOWN")
                    viewModel.playSynth(freqOfTone)
                }
                MotionEvent.ACTION_CANCEL -> {
                    Log.d(TAG, "ACTION_CANCEL")
                    viewModel.stopPlaying(freqOfTone)
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "ACTION_UP")
                    viewModel.stopPlaying(freqOfTone)
                    v.performClick()
                }
            }
            return false
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}