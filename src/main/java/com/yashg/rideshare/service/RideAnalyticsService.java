package com.yashg.rideshare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class RideAnalyticsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Map<String, Object> getRidesPerDay() {
        Aggregation aggregation = newAggregation(
                group("createdDate").count().as("rideCount"),
                project("rideCount").and("_id").as("date"),
                sort(org.springframework.data.domain.Sort.Direction.DESC, "date")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "rides", Map.class);
        return Map.of("ridesPerDay", results.getMappedResults());
    }

    public Map<String, Object> getDriverSummary(String driverId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("driverId").is(driverId)),
                group()
                        .count().as("totalRides")
                        .sum(new org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond(
                                org.springframework.data.mongodb.core.aggregation.ComparisonOperators.valueOf("status").equalToValue("COMPLETED"),
                                1, 0
                        )).as("completedRides")
                        .avg("distanceKm").as("avgDistance")
                        .sum("fare").as("totalFare")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "rides", Map.class);
        return results.getUniqueMappedResult() != null ? results.getUniqueMappedResult() : Map.of();
    }

    public Map<String, Object> getUserSpending(String userId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("userId").is(userId).and("status").is("COMPLETED")),
                group()
                        .sum("fare").as("totalSpent")
                        .count().as("completedRides")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "rides", Map.class);
        return results.getUniqueMappedResult() != null ? results.getUniqueMappedResult() : Map.of();
    }

    public Map<String, Object> getStatusSummary() {
        Aggregation aggregation = newAggregation(
                group("status").count().as("count"),
                project("count").and("_id").as("status")
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "rides", Map.class);
        return Map.of("statusSummary", results.getMappedResults());
    }
}
