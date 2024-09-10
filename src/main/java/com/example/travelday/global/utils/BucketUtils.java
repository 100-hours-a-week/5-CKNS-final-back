package com.example.travelday.global.utils;

import com.example.travelday.domain.bucket.utils.DiscordWebhookNotifier;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BucketUtils {

    @Value("${bucket.max-tokens}")
    private long MAX_BUCKET_TOKENS;

    @Value("${bucket.consume-count}")
    private int CONSUME_BUCKET_COUNT;

    private final Bucket bucket;

    private final DiscordWebhookNotifier discordWebhookNotifier;

    public void checkRequestBucketCount() {
        long avilableTokens = bucket.getAvailableTokens();

        if (avilableTokens == 1) {
            discordWebhookNotifier.sendDiscordNotification("⚠\uFE0F Warning: Bucket is at the last token.");
        }

        if (bucket.tryConsume(CONSUME_BUCKET_COUNT)) {
            log.info("Bucket count : {}", bucket.getAvailableTokens());
            return;
        }

        log.warn("Bucket count is empty");
        discordWebhookNotifier.sendDiscordNotification("\uD83D\uDEAB Error: Bucket tokens are empty, request limit exceeded.");
        throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
    }

    public long getAvailableTokens() {
        long availableTokens = bucket.getAvailableTokens();

        // 최대 토큰 수를 초과한 경우 알림 발송
        if (availableTokens > MAX_BUCKET_TOKENS) {
            discordWebhookNotifier.sendDiscordNotification("🚫 Error: Bucket tokens exceeded the limit.");
        }

        return availableTokens;
    }
}
