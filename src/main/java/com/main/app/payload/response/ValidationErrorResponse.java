package com.main.app.payload.response;

import java.util.Map;

public record ValidationErrorResponse(
        String message,
        String errorCode,
        int status,
        String timestamp,
        Map<String, String> fieldErrors
) {}