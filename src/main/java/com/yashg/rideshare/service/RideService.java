package com.yashg.rideshare.service;

import com.yashg.rideshare.dto.CreateRideRequest;
import com.yashg.rideshare.exception.BadRequestException;
import com.yashg.rideshare.exception.NotFoundException;
import com.yashg.rideshare.model.Ride;
import com.yashg.rideshare.model.User;
import com.yashg.rideshare.repository.RideRepository;
import com.yashg.rideshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    public Ride createRide(CreateRideRequest request, String username) {
        User user = getUser(username);
        ensureRole(user, "ROLE_USER");

        Ride ride = new Ride();
        ride.setUserId(user.getId());
        ride.setPassengerUsername(username);
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus("REQUESTED");
        
        Date now = new Date();
        ride.setCreatedAt(now);
        ride.setCreatedDate(now);
        
        // Set default fare and distance if provided in request
        ride.setFare(0.0);
        ride.setDistanceKm(0.0);

        return rideRepository.save(ride);
    }

    public List<Ride> getUserRides(String username) {
        User user = getUser(username);
        return rideRepository.findByUserId(user.getId());
    }

    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String rideId, String username) {
        User driver = getUser(username);
        ensureRole(driver, "ROLE_DRIVER");

        Ride ride = getRide(rideId);
        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in REQUESTED state");
        }

        ride.setDriverId(driver.getId());
        ride.setDriverUsername(username);
        ride.setStatus("ACCEPTED");
        return rideRepository.save(ride);
    }

    public Ride completeRide(String rideId, String username) {
        User actor = getUser(username);
        Ride ride = getRide(rideId);

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not in ACCEPTED state");
        }

        boolean isUser = actor.getId().equals(ride.getUserId());
        boolean isDriver = actor.getId().equals(ride.getDriverId());
        if (!isUser && !isDriver) {
            throw new BadRequestException("You are not part of this ride");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Ride getRide(String rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
    }

    private void ensureRole(User user, String role) {
        if (!role.equals(user.getRole())) {
            throw new BadRequestException("Operation not allowed for your role");
        }
    }
}