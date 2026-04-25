import com.weather.weatherviewer.dto.LocationSearchResultDto;
import com.weather.weatherviewer.dto.WeatherResultDto;
import com.weather.weatherviewer.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherServiceTest {

    private HttpClient client;
    private HttpResponse<String> response;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        client = mock(HttpClient.class);
        response = (HttpResponse<String>) mock(HttpResponse.class);
        weatherService = new WeatherService(client);
    }

    @Test
    void getWeather_success() throws Exception {
        String json = """
        {
          "current": {
            "time": "2026-04-17T05:00",
            "interval": 900,
            "temperature_2m": 10.4,
            "wind_speed_10m": 2.5,
            "weather_code": 0,
            "relative_humidity_2m": 80,
            "apparent_temperature": 9.3
          }
        }
        """;

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        when(client.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(response);

        WeatherResultDto result = weatherService.getWeather(
                new BigDecimal("48.85"),
                new BigDecimal("2.32")
        );

        assertNotNull(result);
        assertEquals(0, result.getTemperature().compareTo(new BigDecimal("10.4")));
        assertEquals(0, result.getFeelsLike().compareTo(new BigDecimal("9.3")));
        assertEquals(80, result.getHumidity());
        assertEquals("Clear sky", result.getDescription());
        assertEquals("/image/clear.png", result.getIconPatch());
    }

    @Test
    void getWeather_whenStatusIsNot200_throwsException() throws Exception {
        when(response.statusCode()).thenReturn(500);
        when(client.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(response);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> weatherService.getWeather(
                        new BigDecimal("48.85"),
                        new BigDecimal("2.32")
                ));

        assertEquals("Open-meteo request failed", ex.getMessage());
    }

    @Test
    void getWeather_whenCurrentIsNull_throwsException() throws Exception {
        String json = """
        {
          "somethingElse": {}
        }
        """;

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        when(client.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(response);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> weatherService.getWeather(
                        new BigDecimal("48.85"),
                        new BigDecimal("2.32")
                ));

        assertEquals("current not found", ex.getMessage());
    }
    @Test
    void getWeather_whenStatusIs400_throwException()throws Exception{
        when(response.statusCode()).thenReturn(400);
        when(client.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(response);
        RuntimeException ex = assertThrows(RuntimeException.class,()->weatherService.getWeather(
                new BigDecimal("48.85"),
                new BigDecimal("2.32")));
        assertEquals("Open-meteo request failed",ex.getMessage());
    }
    @Test
    void searchLocations_success()throws Exception{
    String json = """
            {
              "results": [
                {
                  "name": "London",
                  "latitude":51.5072,
                  "longitude":-0.1276,
                  "country_code":"GB"
                }
              ]
            }
            """ ;
    when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        when(client.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(response);
        List<LocationSearchResultDto> resultList = (List<LocationSearchResultDto>) weatherService.searchLocations("London");
        LocationSearchResultDto result = resultList.get(0);
        assertNotNull(result);
        assertEquals(0,result.getLat().compareTo(new BigDecimal("51.5072")));
        assertEquals(0,result.getLon().compareTo(new BigDecimal("-0.1276")));
        assertEquals("London",result.getName());
        assertEquals("GB",result.getCountry());

    }

    @Test
    void searchLocations_whenResultsMissing_throwsException()throws Exception{
        String json = """
            {
              "BagJson": [
                {
                  "name": "London",
                  "latitude":"erfd",
                  "longitude":"ssdd",
                  "country_code":"GBs"
                }
              ]
            }
            """ ;
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(json);
        when(client.send(
                any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(response);
        RuntimeException ex = assertThrows(RuntimeException.class,()->weatherService.searchLocations("London"));
        assertEquals("Locations not found",ex.getMessage());


    }

    private static <T> T any(Class<T> clazz) {
        return ArgumentMatchers.any(clazz);
    }
}