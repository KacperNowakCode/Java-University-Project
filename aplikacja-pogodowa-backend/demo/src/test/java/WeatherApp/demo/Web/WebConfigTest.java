package WeatherApp.demo.Web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class WebConfigTest {

    @Autowired
    private WebConfig webConfig;

    @Test
    public void testCorsConfigurer() {
        WebMvcConfigurer configurer = webConfig.corsConfigurer();
        assertDoesNotThrow(() -> {
            CorsRegistry registry = new CorsRegistry();
            configurer.addCorsMappings(registry);
        });
    }
}