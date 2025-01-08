package WeatherApp.demo.controller;

import WeatherApp.demo.service.WeatherService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testGetWeather() throws Exception {
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("data", "Weather data for London");

        when(weatherService.getWeatherData("London")).thenReturn(mockResponse);

        MvcResult result = mockMvc.perform(get("/api/weather/London"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedContent = mockResponse.toJSONString();
        String actualContent = result.getResponse().getContentAsString();
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testGetWeatherWithDifferentCity() throws Exception {
        JSONObject mockResponse = new JSONObject();
        mockResponse.put("data", "Weather data for New York");

        when(weatherService.getWeatherData("New York")).thenReturn(mockResponse);

        MvcResult result = mockMvc.perform(get("/api/weather/New York"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedContent = mockResponse.toJSONString();
        String actualContent = result.getResponse().getContentAsString();
        assertEquals(expectedContent, actualContent);
    }
}