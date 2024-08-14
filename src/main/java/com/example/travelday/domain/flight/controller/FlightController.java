package com.example.travelday.domain.flight.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.example.travelday.domain.flight.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

//    private final AmadeusConnect amadeusConnect;
//
//    @Autowired
//    public FlightController(AmadeusConnect amadeusConnect) {
//        this.amadeusConnect = amadeusConnect;
//    }

    @GetMapping("/")
    public String hello(){
        return "HELLO";
    }


    @GetMapping("/flights")
    public ResponseEntity<FlightOfferSearch[]> flights(@RequestParam(required=true) String origin,
                                                       @RequestParam(required=true) String destination,
                                                       @RequestParam(required=true) String departDate,
                                                       @RequestParam(required=true) String adults,
                                                       @RequestParam(required = false) String returnDate)
        throws ResponseException {
        FlightOfferSearch[] flightOffers = flightService.getFlightOffers(origin, destination, departDate, adults, returnDate);
        return ResponseEntity.ok(flightOffers);
    }
}

//    @PostMapping("/confirm")
//    public FlightPrice confirm(@RequestBody(required=true) FlightOfferSearch search) throws ResponseException {
//        return amadeusConnect.confirm(search);
//    }

