package com.fourelements.synth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fourelements.synth.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    external fun playNote(noteFreq: Float)
    external fun stopPlayingNote()
    external fun startEngine()
    external fun stopEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
        startEngine()
    }

    override fun onDestroy() {
        stopEngine()
        super.onDestroy()
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}