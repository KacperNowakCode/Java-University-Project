package WeatherApp.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AdminController {

    @Value("${spring.application.name:Application}")
    private String appName;

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping("/admin")
    public Object adminData(Model model) {


        Map<String, Object> appDetails = new HashMap<>();
        appDetails.put("Application Name", appName);
        appDetails.put("Server Port", serverPort);
        appDetails.put("Java Version", System.getProperty("java.version"));
        appDetails.put("User Timezone", System.getProperty("user.timezone"));
        appDetails.put("User Country", "Poland");
        appDetails.put("User Language", System.getProperty("user.language"));
        appDetails.put("OS Name", System.getProperty("os.name"));
        appDetails.put("OS Version", System.getProperty("os.version"));
        appDetails.put("Service","Tomcat");
        appDetails.put("Servlet engine","Apache Tomcat/10.1.33");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return appDetails;
    }
}
