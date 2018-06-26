package org.wachowiak.vertx.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class ClientMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) {
        ClientVerticle clientVerticle = new ClientVerticle();
        clientVerticle.init();
        clientVerticle.waitUntilInit();

        IntStream.range(1, 100000).forEach( i->
        clientVerticle.sum(i,i+1, c-> LOGGER.info("Result for a={} and b={} received: {}", i, i+1, c)));
    }
}
