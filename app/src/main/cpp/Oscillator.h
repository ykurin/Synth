//
// Created by Yury on 2/6/2021.
//

#ifndef SYNTH_OSCILLATOR_H
#define SYNTH_OSCILLATOR_H

#include <atomic>
#include <cstdint>
#include "IRenderableAudio.h"

class Oscillator : public IRenderableAudio {
public:
    void setIsOn(bool isOn, double frequency);
    void setSampleRate(int32_t sampleRate);
    void setFrequency(double frequency);
    void renderAudio(float *audioData, std::int32_t numFrames) override;

private:
    std::atomic<bool> m_isOn{false};
    double m_phase = 0.0;
    std::atomic<double> m_phaseIncrement{0.0};
    double m_frequency = 523.25;
    int32_t m_sampleRate = 16000;

    void calculatePhaseIncrement();
};



#endif //SYNTH_OSCILLATOR_H
