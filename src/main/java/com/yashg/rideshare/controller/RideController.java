package com.yashg.rideshare.controller;

import com.yashg.rideshare.dto.CreateRideRequest;
import com.yashg.rideshare.model.Ride;
import com.yashg.rideshare.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping("/rides")
    public Ride requestRide(@Valid @RequestBody CreateRideRequest request, Authentication auth) {
        return rideService.createRide(request, auth.getName());
    }

    @GetMapping("/user/rides")
    public List<Ride> myRides(Authentication auth) {
        return rideService.getUserRides(auth.getName());
    }

    @PostMapping("/rides/{rideId}/complete")
    public Ride completeRide(@PathVariable String rideId, Authentication auth) {
        return rideService.completeRide(rideId, auth.getName());
    }
}