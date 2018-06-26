package org.wachowiak.vertx;

public final class TimeUtils {

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //ignore
        }

    }
}
