package com.github.fh250250.ap2.lib;

public class VideoStreamInfo implements MediaStreamInfo {

    private final String streamConnectionId;

    public VideoStreamInfo(String streamConnectionId) {
        this.streamConnectionId = streamConnectionId;
    }

    @Override
    public StreamType getStreamType() {
        return MediaStreamInfo.StreamType.VIDEO;
    }

    public String getStreamConnectionId() {
        return streamConnectionId;
    }

    @Override
    public String toString() {
        return "VideoStreamInfo{" +
                "streamConnectionId='" + streamConnectionId + '\'' +
                '}';
    }
}
