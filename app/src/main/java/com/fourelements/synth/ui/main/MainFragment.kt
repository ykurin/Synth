package com.fourelements.synth.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.MotionEvent
import android.view.View
import com.fourelements.synth.MainActivity
import com.fourelements.synth.Note
import com.fourelements.synth.R
import com.fourelements.synth.databinding.MainFragmentBinding

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
        private val TAG = MainFragment::class.java.simpleName
    }

    private lateinit var viewModel: MainViewModel

    private var binding: MainFragmentBinding? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view).apply {
            button1.setOnTouchListener(NoteTouchListener(Note.C5))
            button2.setOnTouchListener(NoteTouchListener(Note.E5))
            button3.setOnTouchListener(NoteTouchListener(Note.G5))
        }
    }

    private inner class NoteTouchListener(val freqOfTone: Float) : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (v == null || event == null) {
                return false
            }
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG, "ACTION_DOWN")
                    (activity as MainActivity).playNote(freqOfTone)
                }
                MotionEvent.ACTION_CANCEL -> {
                    Log.d(TAG, "ACTION_CANCEL")
                    (activity as MainActivity).stopPlayingNote()
                }
                MotionEvent.ACTION_UP -> {
                    Log.d(TAG, "ACTION_UP")
                    (activity as MainActivity).stopPlayingNote()
                    v.performClick()
                }
            }
            return false
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}