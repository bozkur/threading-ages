package org.example;

/**
 * @author cevher
 */
public class Restaurants {

    public static Restaurants fetch(Request request) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Restaurants();
    }
}
