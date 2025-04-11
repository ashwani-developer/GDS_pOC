package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gds")
@RequiredArgsConstructor
public class GdsController {

    private final AmadeusService amadeusService;

    @GetMapping("/flights")
    public ResponseEntity<?> getFlightOffers(
            @RequestParam String origin,
            @RequestParam String dest,
            @RequestParam String date,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(required = false) Boolean nonStop,
            @RequestParam(required = false) String travelClass,  // ECONOMY, BUSINESS, FIRST
            @RequestParam(required = false) Integer maxPrice
    ) {
        String response = amadeusService.getFlightOffersFiltered(
                origin, dest, date, adults, nonStop, travelClass, maxPrice
        );
        return ResponseEntity.ok(response);
    }

}
