package WeatherApp.demo.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherUtilTest {

    @BeforeEach
    public void setUp() {
        // Any setup can be done here if needed
    }

    @Test
    public void testGetLocationData() throws Exception {
        String mockResponse = "{\"results\":[{\"latitude\":51.5074,\"longitude\":-0.1278}]}";
        InputStream mockInputStream = new ByteArrayInputStream(mockResponse.getBytes());

        try (MockedStatic<WeatherUtil> mockedWeatherUtil = Mockito.mockStatic(WeatherUtil.class, Mockito.CALLS_REAL_METHODS)) {
            mockedWeatherUtil.when(() -> WeatherUtil.fetchApiResponse(Mockito.anyString())).thenAnswer(invocation -> {
                HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
                Mockito.when(mockConnection.getResponseCode()).thenReturn(200);
                Mockito.when(mockConnection.getInputStream()).thenReturn(mockInputStream);
                return mockConnection;
            });

            JSONArray result = WeatherUtil.getLocationData("London");
            JSONObject location = (JSONObject) result.get(0);
            assertEquals(51.5074, location.get("latitude"));
            assertEquals(-0.1278, location.get("longitude"));
        }
    }

    @Test
    public void testGetLocationDataFailure() {
        try (MockedStatic<WeatherUtil> mockedWeatherUtil = Mockito.mockStatic(WeatherUtil.class, Mockito.CALLS_REAL_METHODS)) {
            mockedWeatherUtil.when(() -> WeatherUtil.fetchApiResponse(Mockito.anyString())).thenAnswer(invocation -> {
                HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
                Mockito.when(mockConnection.getResponseCode()).thenReturn(500);
                return mockConnection;
            });

            assertThrows(RuntimeException.class, () -> WeatherUtil.getLocationData("UnknownCity"));
        }
    }

    @Test
    public void testFetchWeeklyWeatherDataFailure() {
        try (MockedStatic<WeatherUtil> mockedWeatherUtil = Mockito.mockStatic(WeatherUtil.class, Mockito.CALLS_REAL_METHODS)) {
            mockedWeatherUtil.when(() -> WeatherUtil.fetchApiResponse(Mockito.anyString())).thenAnswer(invocation -> {
                HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
                Mockito.when(mockConnection.getResponseCode()).thenReturn(500);
                return mockConnection;
            });

            assertThrows(RuntimeException.class, () -> WeatherUtil.fetchWeeklyWeatherData("mockUrl"));
        }
    }
}