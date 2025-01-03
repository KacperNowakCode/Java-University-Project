package WeatherApp.demo.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.time.LocalTime;


@Component
public class WeatherUtil {

    public static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName
                + "&count=10&language=en&format=json";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch location data");
            }

            StringBuilder resultJson = new StringBuilder();
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
            }

            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(resultJson.toString());

            return (JSONArray) resultsJsonObj.get("results");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching location data", e);
        }
    }

    public static JSONObject fetchWeeklyWeatherData(String urlString) {
        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch weather data");
            }

            StringBuilder resultJson = new StringBuilder();
            try (Scanner scanner = new Scanner(conn.getInputStream())) {
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
            }

            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(resultJson.toString());

            JSONObject daily = (JSONObject) resultJsonObj.get("daily");
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
            JSONArray maxTemps = (JSONArray) daily.get("temperature_2m_max");
            JSONArray minTemps = (JSONArray) daily.get("temperature_2m_min");
            JSONArray weatherCodes = (JSONArray) daily.get("weather_code");
            JSONArray hourlyTemps = (JSONArray) hourly.get("temperature_2m");
            JSONArray hourlyWeatherCode = (JSONArray) hourly.get("weather_code");


            double weeklyMax = calculateMax(maxTemps);
            double weeklyMin = calculateMin(minTemps);
            int mostCommonWeatherCode = getMostFrequentWeatherCode(weatherCodes);
            JSONObject weeklySummary = new JSONObject();


            weeklySummary.put("daily_max_temperature", ((JSONArray) daily.get("temperature_2m_max")).get(0));
            weeklySummary.put("daily_min_temperature", ((JSONArray) daily.get("temperature_2m_min")).get(0));
            weeklySummary.put("weekly_max_temperature", weeklyMax);
            weeklySummary.put("weekly_min_temperature", weeklyMin);
            weeklySummary.put("most_common_weather_code", mostCommonWeatherCode);
            weeklySummary.put("temperature_hourly", cutArray(hourlyTemps));
            weeklySummary.put("weatherCode_hourly", cutArray(hourlyWeatherCode));

            return weeklySummary;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching weather data", e);
        }
    }

    public static JSONArray cutArray(JSONArray hourly) {
        List<Object> tempList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            tempList.add(hourly.get(i));
        }
        JSONArray hourlyTemps = new JSONArray();
        hourlyTemps.addAll(tempList);
        return hourlyTemps;
    }


    private static double calculateMax(JSONArray array) {
        double max = Double.MIN_VALUE;
        for (Object obj : array) {
            max = Math.max(max, (double) obj);
        }
        return max;
    }

    public static int getHour(){
        return LocalTime.now().getHour();
    }


    private static double calculateMin(JSONArray array) {
        double min = Double.MAX_VALUE;
        for (Object obj : array) {
            min = Math.min(min, (double) obj);
        }
        return min;
    }

    private static double calculateAverage(JSONArray array) {
        double sum = 0.0;
        for (Object obj : array) {
            sum += (double) obj;
        }
        return sum / array.size();
    }


    private static Integer getMostFrequentWeatherCode(JSONArray weatherCodes) {
        Map<Long, Integer> frequencyMap = new HashMap<>();
        for (Object obj : weatherCodes) {
            long code = (long) obj;
            if(frequencyMap.containsKey(code)){
                frequencyMap.put(code,frequencyMap.get(code)+1);
            }
            else{
                frequencyMap.put(code, 1);
            }

        }
        return Collections.max(frequencyMap.values());
    }

    private static HttpURLConnection fetchApiResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        return conn;
    }
}
