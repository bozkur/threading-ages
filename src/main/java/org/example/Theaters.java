package org.example;

/**
 * @author cevher
 */
public class Theaters {
    public static Theaters fetch(Request request) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Theaters();
    }
}
