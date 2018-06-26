package org.wachowiak.vertx.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wachowiak.vertx.TimeUtils;
import org.wachowiak.vertx.server.Runner;

import java.util.function.Consumer;

public class ClientVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientVerticle.class);
    private volatile WebClient webClient;

    @Override
    public void start() {
        webClient = WebClient.create(getVertx());
    }

    public void sum(long a, long b, Consumer<Long> response) {

        sendOperation(sumJsonBody(a, b),
                r -> {
                    if (r.failed()) {
                        LOGGER.warn("Request failed", r.cause());
                    } else {
                        response.accept(r.result().bodyAsJsonObject().getLong("result"));
                    }
                }
        );
    }

    private void sendOperation(JsonObject data, Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
        webClient.post(9999, "localhost", "/").sendJson(data, handler);
    }

    void init() {
        Runner.runVerticle(this);
    }

    void waitUntilInit() {
        while (!started()) {
            TimeUtils.sleep(100);
        }
    }

    private boolean started() {
        return webClient != null;
    }

    private JsonObject sumJsonBody(long a, long b) {
        return new JsonObject().put("a", a).put("b", b);
    }
}
