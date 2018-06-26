package org.wachowiak.vertx.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wachowiak.vertx.TimeUtils;

public class ServerVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

    @Override
    public void start() {

        vertx.createHttpServer().requestHandler(request -> {

            request.setExpectMultipart(true);
            LOGGER.debug("Request received");

            request.bodyHandler(bh -> {

                JsonObject body = bh.toJsonObject();
                long a = body.getLong("a");
                long b = body.getLong("b");

                vertx.<String>executeBlocking(future -> {

                            LOGGER.debug("Processing request");

                            LOGGER.debug("Calling time-consuming operation");
                            TimeUtils.sleep(500);
                            future.complete(new JsonObject().put("result", a + b).encode());
                        },
                        false,
                        result -> {
                            if (result.succeeded()) {
                                LOGGER.debug("Sending response");
                                request.response().putHeader("content-type", "text/json").end(result.result());
                            } else {
                                LOGGER.warn("Processing failed", result.cause());
                            }
                        });
            });
        }).listen(9999);


    }

    public static void main(String[] args) {
        Runner.runVerticle(ServerVerticle.class);
    }
}
