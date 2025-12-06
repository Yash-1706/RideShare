package com.yashg.rideshare.controller;

import com.yashg.rideshare.model.Ride;
import com.yashg.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
public class DriverController {

    @Autowired
    private RideService rideService;

    @GetMapping("/rides/requests")
    public List<Ride> pendingRequests() {
        return rideService.getPendingRides();
    }

    @PostMapping("/rides/{rideId}/accept")
    public Ride accept(@PathVariable String rideId, Authentication auth) {
        return rideService.acceptRide(rideId, auth.getName());
    }
}