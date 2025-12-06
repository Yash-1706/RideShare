package com.yashg.rideshare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RideResponse {

    private Long id;
    private String pickupLocation;
    private String dropoffLocation;
    private String driverName;
    private String driverPhone;
    private Integer price;
    private Integer availableSeats;
    private String rideTime;
}
