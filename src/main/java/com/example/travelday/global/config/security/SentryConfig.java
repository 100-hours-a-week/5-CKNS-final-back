package com.example.travelday.global.config.security;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(dsn = "https://03037d8f733436db9cc28b8e01b367f3@o4507814026543104.ingest.us.sentry.io/4507825093607424")
@Configuration
public class SentryConfig {
}