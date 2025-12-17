package com.yashg.rideshare.controller;

import com.yashg.rideshare.model.Ride;
import com.yashg.rideshare.service.RideSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideSearchController {

    @Autowired
    private RideSearchService rideSearchService;

    @GetMapping("/rides/search")
    public List<Ride> searchRides(@RequestParam String keyword) {
        return rideSearchService.searchByLocation(keyword);
    }

    @GetMapping("/rides/filter-distance")
    public List<Ride> filterByDistance(@RequestParam Double min, @RequestParam Double max) {
        return rideSearchService.filterByDistance(min, max);
    }

    @GetMapping("/rides/filter-date-range")
    public List<Ride> filterByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        return rideSearchService.filterByDateRange(start, end);
    }

    @GetMapping("/rides/sort")
    public List<Ride> sortRides(@RequestParam(defaultValue = "asc") String direction) {
        return rideSearchService.sortByFare(direction);
    }

    @GetMapping("/rides/user/{userId}")
    public List<Ride> getUserRides(@PathVariable String userId) {
        return rideSearchService.getRidesByUserId(userId);
    }

    @GetMapping("/rides/user/{userId}/status/{status}")
    public List<Ride> getUserRidesByStatus(@PathVariable String userId, @PathVariable String status) {
        return rideSearchService.getRidesByUserIdAndStatus(userId, status);
    }

    @GetMapping("/driver/{driverId}/active-rides")
    public List<Ride> getActiveDriverRides(@PathVariable String driverId) {
        return rideSearchService.getActiveRidesForDriver(driverId);
    }

    @GetMapping("/rides/filter-status")
    public List<Ride> filterByStatusAndLocation(@RequestParam String status, @RequestParam String keyword) {
        return rideSearchService.filterByStatusAndLocation(status, keyword);
    }

    @GetMapping("/rides/advanced-search")
    public List<Ride> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return rideSearchService.advancedSearch(keyword, status, sortBy, sortDir, page, size);
    }

    @GetMapping("/rides/date/{date}")
    public List<Ride> getRidesByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return rideSearchService.getRidesByDate(date);
    }
}
