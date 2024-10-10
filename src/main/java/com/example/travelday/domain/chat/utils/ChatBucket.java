package com.example.travelday.domain.chat.utils;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatBucket {

    @Value("${bucket.chat-token-refill-seconds}")
    private long CHAT_REFILL_MILLISECONDS;

    @Value("${bucket.chat-max-tokens}")
    private long CHAT_MAX_BUCKET_TOKENS;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // 버킷 가져오기
    public Bucket resolveBucket(String sessionId) {
        return cache.computeIfAbsent(sessionId, this::ChatBucket);
    }

    private Bucket ChatBucket(String sessionId) {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(CHAT_MAX_BUCKET_TOKENS, Refill.intervally(CHAT_MAX_BUCKET_TOKENS, Duration.ofSeconds(CHAT_REFILL_MILLISECONDS))))
            .build();
    }
}
