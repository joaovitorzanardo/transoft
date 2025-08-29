package br.com.transoft.backend.service;

import br.com.transoft.backend.entity.Coordinate;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

@Service
public class CoordinateService {

    private final GeoApiContext geoApiContext;

    public CoordinateService(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    public Coordinate findCoordinateByAddress(String address) {
        try {
            GeocodingApiRequest request = GeocodingApi
                    .newRequest(geoApiContext)
                    .address(address);

            GeocodingResult result = request.await()[0];

            return new Coordinate(
                    result.geometry.location.lat,
                    result.geometry.location.lng
            );
        } catch (Exception e) {
            return null;
        }
    }

}
