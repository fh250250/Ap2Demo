package com.github.fh250250.ap2.server;

import com.github.fh250250.ap2.lib.AirPlayBonjour;
import com.github.fh250250.ap2.server.internal.ControlServer;

public class AirPlayServer {

    private final AirPlayBonjour airPlayBonjour;
    private final ControlServer controlServer;

    public AirPlayServer(AirPlayConfig airPlayConfig, AirPlayConsumer airPlayConsumer) {
        airPlayBonjour = new AirPlayBonjour(airPlayConfig.getServerName());
        controlServer = new ControlServer(airPlayConfig, airPlayConsumer);
    }

    public void start() throws Exception {
        controlServer.start();
        airPlayBonjour.start(controlServer.getPort());
    }

    public void stop() {
        airPlayBonjour.stop();
        controlServer.stop();
    }
}
