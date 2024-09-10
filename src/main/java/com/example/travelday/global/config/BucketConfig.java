package com.example.travelday.global.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BucketConfig {

    @Value("${bucket.max-tokens}")
    private long MAX_BUCKET_TOKENS;

    @Bean
    public Bucket bucket() {

        final Refill refill = Refill.intervally(MAX_BUCKET_TOKENS, Duration.ofDays(1));

        final Bandwidth limit = Bandwidth.classic(MAX_BUCKET_TOKENS, refill);

        return Bucket.builder()
                    .addLimit(limit)
                    .build();
    }
}
