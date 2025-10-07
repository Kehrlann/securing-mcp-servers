package wf.garnier.mcp.security.demo.mcp.server.weather;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
class WeatherService {

	private final RestClient restClient;

	public WeatherService(RestClient.Builder builder) {
		this.restClient = builder.build();
	}

	@McpTool(name = "current-temperature",
			description = "Get the current temperature (in celsius) for a specific location")
	public WeatherResponse getWeather(@McpToolParam(description = "The location latitude") double latitude,
			@McpToolParam(description = "The location longitude") double longitude) {
		var response = restClient.get()
			.uri("https://api.open-meteo.com/v1/forecast?latitude={latitude}&longitude={longitude}&current=temperature_2m",
					latitude, longitude)
			.retrieve()
			.body(WeatherApiResponse.class);

		return new WeatherResponse(response.current().time(), response.current().temperature_2m());
	}

	@McpTool(name = "temperature-history",
			description = "Get 5-year historical temperature data (in Celsius), including daily min and daily max temperatures, for a specific location")
	public HistoricalWeatherResponse getHistoricalWeatherData(
			@McpToolParam(description = "The location latitude") double latitude,
			@McpToolParam(description = "The location longitude") double longitude) {

		var data = IntStream.range(0, 5)
			.parallel()
			.mapToObj(yearDelta -> getWeatherData(latitude, longitude, yearDelta))
			.flatMap(List::stream)
			.toList();

		return new HistoricalWeatherResponse(data);
	}

	/**
	 * Obtain weather data at the given location, N years ago, for +/- 2 days. For
	 * example, if today is 2025-09-28, and N years = 2 years, will return weather data
	 * for 2023-09-26 through 2023-09-30.
	 */
	private List<HistoricalWeatherResponse.DailyTemperatures> getWeatherData(double latitude, double longitude,
			int yearsAgo) {
		var response = restClient.get()
			.uri("https://archive-api.open-meteo.com/v1/archive?latitude={latitude}&longitude={longitude}&start_date={start}&end_date={end}&daily=temperature_2m_min,temperature_2m_max",
			//@formatter:off
                        Map.of(
                                "latitude", latitude,
                                "longitude", longitude,
                                "start", LocalDate.now().minus(Period.ofYears(yearsAgo)).minus(Period.ofDays(2)),
                                "end", yearsAgo == 0 ? LocalDate.now() : LocalDate.now().minus(Period.ofYears(yearsAgo)).plus(Period.ofDays(2))
                        )
                        //@formatter:on
			)
			.retrieve()
			.body(HistoricalWeatherApiResponse.class);
		var entries = response.daily().time().length;
		//@formatter:off
        return IntStream.range(0, entries)
                .mapToObj(i -> new HistoricalWeatherResponse.DailyTemperatures(
                        response.daily().time()[i].toString(),
                        response.daily().temperature_2m_min()[i],
                        response.daily().temperature_2m_max()[i]
                ))
                .toList();
        //@formatter:on
	}

	public record HistoricalWeatherApiResponse(Daily daily) {
		public record Daily(LocalDate[] time, double[] temperature_2m_max, double[] temperature_2m_min) {
		}
	}

	public record HistoricalWeatherResponse(List<DailyTemperatures> dailyTemperatures) {
		public record DailyTemperatures(String date, double minTemperature, double maxTemperature) {
		}
	}

	public record WeatherApiResponse(Current current) {
		public record Current(LocalDateTime time, int interval, double temperature_2m) {
		}
	}

	public record WeatherResponse(LocalDateTime time, double temperature) {
	}

}
