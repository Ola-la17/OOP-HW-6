package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class YandexweatherModel{
//    String url = "https://yandex.ru/pogoda/";
    public static final String HOST = "yandex.ru";
    public static final String POGODA = "pogoda";
    public static final String PROTOCOL = "https";
    public static final String BASE_HOST = "api.weather.yandex.ru";
    public static final String VERSION = "v2";
    public static final String FORECAST = "forecast";
    public static final String LATTITUDE = "lat=55.75396";
    public static final String LONGITUDE = "lon=37.620393";
    public static final String API_KEY = "c58cd2ed-ef0b-4edf-867b-522f08859e28";
    public static final String API_KEY_HEADER_PARAM = "X-Yandex-API-Key";

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    public static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static String getWeather() throws IOException {
        // https://api.weather.yandex.ru/v2/forecast
        HttpUrl httpUrl = new HttpUrl.Builder()   // .scheme(PROTOCOL).host(HOST).addPathSegment(POGODA).addPathSegment(city).build(); builder ссылки на yandex/pogoda
                .scheme(PROTOCOL)
                .host(BASE_HOST)
                .addPathSegment(VERSION)
                .addPathSegment(FORECAST)
                .addQueryParameter(LONGITUDE, LATTITUDE)
                .build();

        Request request = new Request.Builder().header(API_KEY_HEADER_PARAM, API_KEY).url(httpUrl).build();
//        Request request = new Request.Builder().url(httpUrl).build(); запрос через сайт yandex/pogoda
        Response ForecastResponse = okHttpClient.newCall(request).execute();
        String weatherResponse = ForecastResponse.body().string();
//        System.out.println(weatherResponse);

        // парсинг ответа сервера
        String date = objectMapper.readTree(weatherResponse).get("now_dt").asText();
        System.out.println("Сегодня: " + date);
        String temp = objectMapper.readTree(weatherResponse).get("fact").get("temp").asText();
        System.out.println("Температура воздуха: " + temp);
        String condition = objectMapper.readTree(weatherResponse).get("fact").get("condition").asText();
        System.out.println(condition);
        String humidity = objectMapper.readTree(weatherResponse).get("fact").get("humidity").asText();
        System.out.println("Влажность воздуха: " + humidity);
        String pressure = objectMapper.readTree(weatherResponse).get("fact").get("pressure_mm").asText();
        System.out.println("Давление, мм: " + pressure);

        return weatherResponse;
    }
}