package org.example;

import java.io.IOException;

public interface WeatherModel {
    void getWeather(String selectedCity, org.example.PERIOD period) throws IOException;
}
