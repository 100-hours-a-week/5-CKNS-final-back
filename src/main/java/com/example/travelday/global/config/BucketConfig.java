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

        final Refill refill = Refill.intervally(40, Duration.ofDays(1));

        final Bandwidth limit = Bandwidth.classic(40, refill);

        return Bucket.builder()
                    .addLimit(limit)
                    .build();
    }
}
