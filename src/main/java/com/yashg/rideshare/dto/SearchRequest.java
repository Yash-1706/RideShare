package com.yashg.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchRequest {

    @NotBlank(message = "Search keyword is required")
    private String keyword;
}
