package com.example.travelday.global.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BucketConfig {

    @Bean
    public Bucket bucket() {

        final Refill refill = Refill.intervally(1, Duration.ofMinutes(5));

        final Bandwidth limit = Bandwidth.classic(1, refill);

        return Bucket.builder()
                    .addLimit(limit)
                    .build();
    }
}
