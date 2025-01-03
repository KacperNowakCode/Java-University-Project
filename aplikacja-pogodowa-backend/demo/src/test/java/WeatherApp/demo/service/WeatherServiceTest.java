package WeatherApp.demo.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import WeatherApp.demo.util.WeatherUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherServiceTest {

    private WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        weatherService = new WeatherService();
    }

    @Test
    public void testGetWeatherData() {
        JSONArray mockLocationData = new JSONArray();
        JSONObject location = new JSONObject();
        location.put("latitude", 51.5074);
        location.put("longitude", -0.1278);
        mockLocationData.add(location);

        JSONObject mockWeatherData = new JSONObject();
        mockWeatherData.put("data", "Mock weather data");

        try (MockedStatic<WeatherUtil> mockedWeatherUtil = Mockito.mockStatic(WeatherUtil.class)) {
            mockedWeatherUtil.when(() -> WeatherUtil.getLocationData("London")).thenReturn(mockLocationData);
            mockedWeatherUtil.when(() -> WeatherUtil.fetchWeeklyWeatherData(Mockito.anyString())).thenReturn(mockWeatherData);

            JSONObject result = weatherService.getWeatherData("London");
            assertEquals(mockWeatherData, result);
        }
    }

    @Test
    public void testGetWeatherDataLocationNotFound() {
        try (MockedStatic<WeatherUtil> mockedWeatherUtil = Mockito.mockStatic(WeatherUtil.class)) {
            mockedWeatherUtil.when(() -> WeatherUtil.getLocationData("UnknownCity")).thenReturn(null);

            assertThrows(RuntimeException.class, () -> weatherService.getWeatherData("UnknownCity"));
        }
    }
}