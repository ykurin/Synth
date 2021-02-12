//
// Created by Yury on 2/10/2021.
//

#ifndef SYNTH_AUDIOENGINE_H
#define SYNTH_AUDIOENGINE_H

#include <aaudio/AAudio.h>
#include "Oscillator.h"

class AudioEngine {
public:
    bool start();
    void stop();
    void restart();
    void setIsOscillatorOn(bool isOscillatorOn);

private:
    Oscillator _oscillator;
    AAudioStream *_stream;
};


#endif //SYNTH_AUDIOENGINE_H
