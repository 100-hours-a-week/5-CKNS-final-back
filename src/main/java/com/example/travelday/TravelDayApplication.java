package com.example.travelday;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class TravelDayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelDayApplication.class, args);

        log.info("TravelDayApplication started successfully. spring boot 시작 시간: {}", LocalDateTime.now());
    }
}

