package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Value("${aviationstack.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<FlightManagement> getFlights(String departureIATA, String arrivalIATA) {
        String url = String.format(
                "%s/flights?access_key=%s&dep_iata=%s&arr_iata=%s",
                baseUrl, apiKey, departureIATA, arrivalIATA
        );

        ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);
        if (response == null || response == null) return List.of();

        return response.getData().stream().map(data -> {
            FlightManagement flight = new FlightManagement();
            flight.setFlightNumber(data.getFlight().getNumber());
            flight.setAirlineName(data.getAirline().getName());
            flight.setDepartureAirport(data.getDeparture().getAirport());
            flight.setArrivalAirport(data.getArrival().getAirport());
            flight.setDepartureTime(data.getDeparture().getScheduled());
            flight.setArrivalTime(data.getArrival().getScheduled());
            flight.setStatus(data.getStatus());
            return flight;
        }).collect(Collectors.toList());
    }
}
