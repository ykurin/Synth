//
// Created by Yury on 2/22/2021.
//

#ifndef SYNTH_MIXER_H
#define SYNTH_MIXER_H

#include <array>
#include "IRenderableAudio.h"

constexpr std::int32_t kBufferSize = 192*10;
constexpr std::uint8_t kMaxTracks = 100;

class Mixer : public IRenderableAudio {
public:
    void renderAudio(float *audioData, std::int32_t numFrames) override;
    void addTrack(IRenderableAudio *track);
    void setChannelCount(std::int32_t channelCount);

private:
    float m_mixingBuffer[kBufferSize];
    std::array<IRenderableAudio*, kMaxTracks> m_tracks;
    std::uint8_t m_nextFreeTrackIndex = 0;
    std::int32_t m_channelCount = 1; // Default to mono
};


#endif //SYNTH_MIXER_H
