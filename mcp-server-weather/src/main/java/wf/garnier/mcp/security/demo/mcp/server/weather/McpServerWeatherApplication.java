package wf.garnier.mcp.security.demo.mcp.server.weather;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@formatter:off
@SpringBootApplication
@RegisterReflectionForBinding({
        WeatherService.HistoricalWeatherApiResponse.class,
        WeatherService.HistoricalWeatherApiResponse.Daily.class,
        WeatherService.HistoricalWeatherResponse.class,
        WeatherService.HistoricalWeatherResponse.DailyTemperatures.class,
        WeatherService.WeatherApiResponse.class,
        WeatherService.WeatherApiResponse.Current.class,
        WeatherService.WeatherResponse.class
})
public class McpServerWeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerWeatherApplication.class, args);
    }

}
//@formatter:on
