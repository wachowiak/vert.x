package org.wachowiak.vertx.server;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public final class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void runVerticle(Class<? extends Verticle> clazz) {

        Consumer<Vertx> runner = vertx ->
            vertx.deployVerticle(clazz.getCanonicalName(), Runner::checkInitStatus);
        runner.accept(Vertx.vertx(vertexOptions()));

    }

    private static VertxOptions vertexOptions() {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setWorkerPoolSize(100);

        return vertxOptions;
    }

    public static void runVerticle(Verticle verticle) {

        Consumer<Vertx> runner = vertx ->
            vertx.deployVerticle(verticle, Runner::checkInitStatus);
        runner.accept(Vertx.vertx());
    }


    private static void checkInitStatus(AsyncResult<String> handler){
        if (handler.failed()) {
            LOGGER.warn("Could not initialize verticle", handler.cause());
        } else {
            LOGGER.info("Verticle successfully initialized");
        }

    }
}
