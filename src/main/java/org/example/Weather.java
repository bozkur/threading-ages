package org.example;

/**
 * @author cevher
 */
public class Weather {
    public static Weather fetch(Request request) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Weather();
    }
}
