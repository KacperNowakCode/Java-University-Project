package WeatherApp.demo.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import WeatherApp.demo.util.WeatherUtil;

@Service
public class WeatherService {

    public JSONObject getWeatherData(String locationName) {
        JSONArray locationData = WeatherUtil.getLocationData(locationName);
        if (locationData == null || locationData.isEmpty()) {
            throw new RuntimeException("Location not found");
        }

        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                + "&longitude=" + longitude
                + "&hourly=temperature_2m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=Europe%2FBerlin";

        return WeatherUtil.fetchWeeklyWeatherData(urlString);
    }
}