//
// Created by Yury on 2/6/2021.
//

#ifndef SYNTH_OSCILLATOR_H
#define SYNTH_OSCILLATOR_H

#include <atomic>
#include <stdint.h>

class Oscillator {
public:
    void setIsOn(bool inOn);
    void setSampleRate(int32_t sampleRate);
    void setFrequency(double frequency);
    void render(float *audioData, int32_t numFrames);

private:
    std::atomic<bool> m_isOn{false};
    double m_phase = 0.0;
    double m_phaseIncrement = 0.0;
    double m_frequency = 523.25;
    int32_t m_sampleRate = 16000;

    void calculatePhaseIncrement();
};



#endif //SYNTH_OSCILLATOR_H
