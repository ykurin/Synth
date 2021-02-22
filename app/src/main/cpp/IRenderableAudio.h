//
// Created by Yury on 2/22/2021.
//

#ifndef SYNTH_IRENDERABLEAUDIO_H
#define SYNTH_IRENDERABLEAUDIO_H

#include <cstdint>

class IRenderableAudio {
public:
    virtual ~IRenderableAudio() = default;
    virtual void renderAudio(float *audioData, std::int32_t numFrames) = 0;
};

#endif //SYNTH_IRENDERABLEAUDIO_H
