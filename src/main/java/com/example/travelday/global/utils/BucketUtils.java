package com.example.travelday.global.utils;

import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BucketUtils {

    private static final int CONSUME_BUCKET_COUNT = 1;

    private final Bucket bucket;

    public void checkRequestBucketCount() {
        if (bucket.tryConsume(CONSUME_BUCKET_COUNT)) {
            log.info("Bucket count : {}", bucket.getAvailableTokens());
            return;
        }

        log.warn("Bucket count is empty");
        throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
    }
}
