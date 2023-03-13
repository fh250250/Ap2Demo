package com.github.fh250250.ap2.server;

import com.github.fh250250.ap2.lib.AudioStreamInfo;
import com.github.fh250250.ap2.lib.VideoStreamInfo;

import java.nio.file.Path;

public interface AirPlayConsumer {

    void onVideoFormat(VideoStreamInfo videoStreamInfo);

    void onVideo(byte[] bytes);

    void onVideoSrcDisconnect();

    void onAudioFormat(AudioStreamInfo audioStreamInfo);

    void onAudio(byte[] bytes);

    void onAudioSrcDisconnect();

    default void onMediaPlaylist(String playlistUri) {
    }
}
