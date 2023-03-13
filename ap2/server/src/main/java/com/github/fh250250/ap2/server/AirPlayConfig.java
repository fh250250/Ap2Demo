package com.github.fh250250.ap2.server;

import lombok.Data;

@Data
public class AirPlayConfig {
    private String serverName;
    private int width;
    private int height;
    private int fps;
}
