package com.example.demo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ApiResponse {
    private List<FlightData> data;

    @Data
    public static class FlightData {
        private Airline airline;
        private FlightInfo flight;
        private Airport departure;
        private Airport arrival;
        private String status;

        @Data public static class Airline { private String name; }
        @Data public static class FlightInfo { private String number; }
        @Data public static class Airport {
            private String airport;
            private String scheduled;
        }
    }
}
