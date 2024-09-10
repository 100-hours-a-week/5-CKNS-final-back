package com.example.travelday.domain.bucket.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscordWebhookNotifier {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${discord.webhook.url}")
    private String DISCORD_WEBHOOK_URL;

    public void sendDiscordNotification(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String payload = "{\"content\": \"" + message + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        restTemplate.exchange(DISCORD_WEBHOOK_URL, HttpMethod.POST, entity, String.class);
    }
}
