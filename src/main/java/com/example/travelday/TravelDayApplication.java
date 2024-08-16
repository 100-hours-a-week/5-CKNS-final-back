package com.example.travelday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TravelDayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelDayApplication.class, args);
    }
}

