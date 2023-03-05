package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

public class AccuweatherModel implements WeatherModel{
//    http://dataservice.accuweather.com/forecasts/v1/daily/1day/{locationKey}
    public static final String PROTOCOL = "https";
    public static final String BASE_HOST = "dataservice.accuweather.com";
    public static final String FORECASTS = "forecasts";
    public static final String VERSION = "v1";
    public static final String DAILY = "daily";
    public static final String ONE_DAY = "1day";
    public static final String FIVE_DAYS = "5day";
    public static final String API_KEY = "DpP62hATSWHkc9HRYwelnQDeRMVb7mD7";
    public static final String API_KEY_QUERY_PARAM = "apikey";
    public static final String LOCATIONS = "locations";
    public static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void getWeather(String selectedCity, org.example.Period period) throws IOException {
        switch (period) {
            case ONE_DAY:
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                Request request = new Request.Builder().url(httpUrl).build();

                Response ForecastResponse = okHttpClient.newCall(request).execute();
                String weatherResponse = ForecastResponse.body().string();

                Map<String, Object> map = objectMapper.readValue(weatherResponse, new TypeReference<Map<String,Object>>(){});
                System.out.println(map.get("DailyForecasts").toString());
                break;
            case FIVE_DAYS:
                httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(FIVE_DAYS)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                request = new Request.Builder().url(httpUrl).build();

                Response fiveDaysForecastResponse = okHttpClient.newCall(request).execute();
                String weatherResponse5days = fiveDaysForecastResponse.body().string();
//                System.out.println(weatherResponse5days);

                map = objectMapper.readValue(weatherResponse5days, new TypeReference<Map<String,Object>>(){});
                System.out.println(map.get("DailyForecasts").toString());
                break;
        }
    }

    private String detectCityKey(String selectCity) throws IOException {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PROTOCOL)
                .host(BASE_HOST)
                .addPathSegment(LOCATIONS)
                .addPathSegment(VERSION)
                .addPathSegment(CITIES)
                .addPathSegment(AUTOCOMPLETE)
                .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .addQueryParameter("q", selectCity)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseString = response.body().string();

        String cityKey = objectMapper.readTree(responseString).get(0).at("/Key").asText();
        return cityKey;
    }
}