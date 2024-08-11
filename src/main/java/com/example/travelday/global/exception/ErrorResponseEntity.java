package com.example.travelday.global.exception;

import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorResponseEntity(int status, String name, String code, String message) {

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(errorCode.getHttpStatus().value())
                        .name(errorCode.name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
