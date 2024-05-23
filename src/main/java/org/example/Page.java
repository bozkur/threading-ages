package org.example;

/**
 * @author cevher
 */
public class Page {
    private final Request request;
    private Weather weather;
    private Restaurants restaurants;
    private Theaters theaters;

    public Page(Request request) {
        this.request = request;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Page setWeather(Weather weather) {
        this.weather = weather;
        return this;
    }

    public Page setRestaurants(Restaurants restaurants) {
        this.restaurants = restaurants;
        return this;
    }

    public Page setTheaters(Theaters theaters) {
        this.theaters = theaters;
        return this;
    }

    public void send() {
    }
}
