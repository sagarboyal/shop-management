package com.main.app.payload.response;

public record ErrorResponse(
        String message,
        String errorCode,
        int status,
        String timestamp
) {}