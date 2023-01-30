//
// Created by Yury on 2/22/2021.
//

#include "Mixer.h"

void Mixer::renderAudio(float *audioData, std::int32_t numFrames) {
    memset(audioData, 0, sizeof(float) * numFrames * m_channelCount);

    for (int i = 0; i < m_nextFreeTrackIndex; ++i) {
        m_tracks[i]->renderAudio(m_mixingBuffer, numFrames);

        for (int j = 0; j < numFrames * m_channelCount; ++j) {
            audioData[j] += m_mixingBuffer[j];
        }
    }
}

void Mixer::addTrack(IRenderableAudio *track) {
    m_tracks[m_nextFreeTrackIndex++] = track;
}

void Mixer::setChannelCount(std::int32_t channelCount) {
    m_channelCount = channelCount;
}
