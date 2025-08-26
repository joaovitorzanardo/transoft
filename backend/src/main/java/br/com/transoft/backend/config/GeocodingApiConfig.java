package br.com.transoft.backend.config;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeocodingApiConfig {

    @Value("${GEOCODING_API_KEY}")
    public String GEOCODING_API_KEY;

    @Bean
    public GeoApiContext getGeoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey(GEOCODING_API_KEY)
                .build();
    }

}
