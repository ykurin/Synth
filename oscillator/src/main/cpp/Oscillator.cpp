//
// Created by Yury on 2/6/2021.
//

#include "Oscillator.h"
#include <cmath>

#define TWO_PI (3.14159 * 2)
#define AMPLITUDE 0.3

void Oscillator::setIsOn(bool inOn) {
    _isOn.store(inOn);
}

void Oscillator::setSampleRate(int32_t sampleRate) {
    _sampleRate = sampleRate;
    calculatePhaseIncrement();
}

void Oscillator::setFrequency(double frequency) {
    _frequency = frequency;
    calculatePhaseIncrement();
}

void Oscillator::calculatePhaseIncrement() {
    _phaseIncrement = (TWO_PI * _frequency) / (double) _sampleRate;
}

void Oscillator::render(float *audioData, int32_t numFrames) {
    if (!_isOn.load()) _phase = 0;

    for (int i = 0; i < numFrames; ++i) {
        if (_isOn.load()) {
            audioData[i] = (float) (sin(_phase) * AMPLITUDE);
            _phase += _phaseIncrement;
            if (_phase > TWO_PI) _phase -= TWO_PI;
        } else {
            audioData[i] = 0;
        }
    }
}

