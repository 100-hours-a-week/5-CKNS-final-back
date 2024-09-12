package com.example.travelday;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class TravelDayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelDayApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("Spring boot application running in UTC timezone 시작 시간: {}", LocalDateTime.now());
    }
}

