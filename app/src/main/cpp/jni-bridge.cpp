//
// Created by Yury on 2/17/2021.
//

#include <jni.h>
#include <android/input.h>
#include "AudioEngine.h"

static AudioEngine *audioEngine = new AudioEngine();

extern "C" {

JNIEXPORT void JNICALL
Java_com_fourelements_synth_MainActivity_startEngine(JNIEnv *env, jobject /* this */) {
    audioEngine->start();
}

JNIEXPORT void JNICALL
Java_com_fourelements_synth_MainActivity_stopEngine(JNIEnv *env, jobject /* this */) {
    audioEngine->stop();
}

JNIEXPORT void JNICALL
Java_com_fourelements_synth_MainActivity_playNote(JNIEnv *env, jobject thiz, jfloat note_freq) {
    audioEngine->setIsOscillatorOn(true, note_freq);
}

JNIEXPORT void JNICALL
Java_com_fourelements_synth_MainActivity_stopPlayingNote(JNIEnv *env, jobject thiz) {
    audioEngine->setIsOscillatorOn(false, 0.0);
}

}