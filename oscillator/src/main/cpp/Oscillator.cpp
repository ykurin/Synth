//
// Created by Yury on 2/6/2021.
//

#include "oscillator/Oscillator.h"
#include <cmath>

#define TWO_PI (3.14159 * 2)
#define AMPLITUDE 0.3

void Oscillator::setIsOn(bool inOn) {
    m_isOn.store(inOn);
}

void Oscillator::setSampleRate(int32_t sampleRate) {
    m_sampleRate = sampleRate;
    calculatePhaseIncrement();
}

void Oscillator::setFrequency(double frequency) {
    m_frequency = frequency;
    calculatePhaseIncrement();
}

void Oscillator::calculatePhaseIncrement() {
    m_phaseIncrement = (TWO_PI * m_frequency) / (double) m_sampleRate;
}

void Oscillator::render(float *audioData, int32_t numFrames) {
    if (!m_isOn.load()) m_phase = 0;

    for (int i = 0; i < numFrames; ++i) {
        if (m_isOn.load()) {
            audioData[i] = (float) (sin(m_phase) * AMPLITUDE);
            m_phase += m_phaseIncrement;
            if (m_phase > TWO_PI) m_phase -= TWO_PI;
        } else {
            audioData[i] = 0;
        }
    }
}

