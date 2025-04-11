package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AmadeusService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${amadeus.client-id}")
    private String clientId;
    @Value("${amadeus.client-secret}")
    private String clientSecret;
    @Value("${amadeus.auth-url}")
    private String authUrl;
    @Value("${amadeus.flight-url}")
    private String flightUrl;

    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<AuthTokenResponse> response = restTemplate.postForEntity(
                authUrl, request, AuthTokenResponse.class
        );

        return response.getBody().getAccessToken();
    }

    public String getFlightOffersFiltered(String origin, String dest, String date, int adults,
                                          Boolean nonStop, String travelClass, Integer maxPrice) {
        String token = getAccessToken();

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(flightUrl)
                .queryParam("originLocationCode", origin)
                .queryParam("destinationLocationCode", dest)
                .queryParam("departureDate", date)
                .queryParam("adults", adults);

        if (nonStop != null) builder.queryParam("nonStop", nonStop);
        if (travelClass != null) builder.queryParam("travelClass", travelClass);
        if (maxPrice != null) builder.queryParam("maxPrice", maxPrice);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, request, String.class
        );

        return response.getBody();
    }

}
