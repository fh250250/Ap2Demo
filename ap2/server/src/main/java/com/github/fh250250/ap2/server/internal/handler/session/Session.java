package com.github.fh250250.ap2.server.internal.handler.session;

import com.github.fh250250.ap2.lib.AirPlay;
import com.github.fh250250.ap2.server.internal.AudioControlServer;
import com.github.fh250250.ap2.server.internal.AudioServer;
import com.github.fh250250.ap2.server.internal.VideoServer;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Session {

    private final String id;

    private final AirPlay airPlay;
    private final VideoServer videoServer;
    private final AudioServer audioServer;
    private final AudioControlServer audioControlServer;
    private final Map<String, ChannelHandlerContext> reverseContexts;
    private final Map<String, ChannelHandlerContext> playlistRequestContexts;

    Session(String id) {
        this.id = id;
        airPlay = new AirPlay();
        videoServer = new VideoServer(airPlay);
        audioServer = new AudioServer(airPlay);
        audioControlServer = new AudioControlServer();
        reverseContexts = new ConcurrentHashMap<>();
        playlistRequestContexts = new ConcurrentHashMap<>();
    }
}
