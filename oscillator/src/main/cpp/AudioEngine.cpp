//
// Created by Yury on 2/10/2021.
//

#include "AudioEngine.h"

bool AudioEngine::start() {
    return false;
}

void AudioEngine::stop() {
    if (_stream != nullptr) {
        AAudioStream_requestStop(_stream);
        AAudioStream_close(_stream);
    }
}

void AudioEngine::restart() {

}

void AudioEngine::setIsOscillatorOn(bool isOscillatorOn) {
    _oscillator.setIsOn(isOscillatorOn);
}
