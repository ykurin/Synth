package com.fourelements.synth.ui.main

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @Composable
    fun ConstraintLayoutContent() {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

            val (text, button1, button2, button3) = createRefs()

            Text("Hello World!", Modifier.constrainAs(text) {
                linkTo(parent.start, parent.top, parent.end, parent.bottom)
            }, color = Color.White)

            Button(onClick = { },
                modifier = Modifier
                    .constrainAs(button1) {
                        bottom.linkTo(parent.bottom, margin = 200.dp)
                        linkTo(parent.start, button2.start)
                    }
                    .pointerInteropFilter {
                        onTouch(Note.C5, it)
                    }) {
                Text("C")
            }

            Button(onClick = { },
                modifier = Modifier
                    .constrainAs(button2) {
                        bottom.linkTo(parent.bottom, margin = 200.dp)
                        linkTo(button1.end, button3.start)
                    }
                    .pointerInteropFilter {
                        onTouch(Note.E5, it)
                    }) {
                Text("E")
            }

            Button(onClick = { },
                modifier = Modifier
                    .constrainAs(button3) {
                        bottom.linkTo(parent.bottom, margin = 200.dp)
                        linkTo(button2.end, parent.end)
                    }
                    .pointerInteropFilter {
                        onTouch(Note.G5, it)
                    }) {
                Text("G")
            }
        }

    }

    private fun onTouch(freqOfTone: Float, event: MotionEvent): Boolean {
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
            }
        }
        return false
    }

    @Preview
    @Composable
    fun PreviewKeyboard() {
        ConstraintLayoutContent()
    }

}