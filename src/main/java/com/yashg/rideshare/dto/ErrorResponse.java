package com.yashg.rideshare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private String message;
    private Map<String, String> errors; // optional field-level errors
    private Instant timestamp;
}