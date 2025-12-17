package com.yashg.rideshare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DistanceFilterRequest {
    
    @NotNull(message = "Minimum distance is required")
    @Min(value = 0, message = "Minimum distance must be >= 0")
    private Double minKm;
    
    @NotNull(message = "Maximum distance is required")
    @Min(value = 0, message = "Maximum distance must be >= 0")
    private Double maxKm;
}
