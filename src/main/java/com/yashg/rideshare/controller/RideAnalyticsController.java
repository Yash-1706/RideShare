package com.yashg.rideshare.controller;

import com.yashg.rideshare.service.RideAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
public class RideAnalyticsController {

    @Autowired
    private RideAnalyticsService analyticsService;

    @GetMapping("/rides-per-day")
    public Map<String, Object> getRidesPerDay() {
        return analyticsService.getRidesPerDay();
    }

    @GetMapping("/driver/{driverId}/summary")
    public Map<String, Object> getDriverSummary(@PathVariable String driverId) {
        return analyticsService.getDriverSummary(driverId);
    }

    @GetMapping("/user/{userId}/spending")
    public Map<String, Object> getUserSpending(@PathVariable String userId) {
        return analyticsService.getUserSpending(userId);
    }

    @GetMapping("/status-summary")
    public Map<String, Object> getStatusSummary() {
        return analyticsService.getStatusSummary();
    }
}
