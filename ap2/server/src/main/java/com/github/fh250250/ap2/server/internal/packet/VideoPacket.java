package com.github.fh250250.ap2.server.internal.packet;

import lombok.Data;

@Data
public class VideoPacket {

    private final int payloadType;
    private final int payloadSize;

    private final byte[] payload;
}
