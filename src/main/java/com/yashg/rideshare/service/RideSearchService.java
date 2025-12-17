package com.yashg.rideshare.service;

import com.yashg.rideshare.model.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class RideSearchService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Ride> searchByLocation(String keyword) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(keyword, "i"),
                Criteria.where("dropLocation").regex(keyword, "i")
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> filterByDistance(Double minKm, Double maxKm) {
        Criteria criteria = Criteria.where("distanceKm").gte(minKm).lte(maxKm);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> filterByDateRange(Date startDate, Date endDate) {
        Criteria criteria = Criteria.where("createdDate").gte(startDate).lte(endDate);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> sortByFare(String direction) {
        Query query = new Query();
        if ("desc".equalsIgnoreCase(direction)) {
            query.with(Sort.by(Sort.Direction.DESC, "fare"));
        } else {
            query.with(Sort.by(Sort.Direction.ASC, "fare"));
        }
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> getRidesByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> getRidesByUserIdAndStatus(String userId, String status) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("status").is(status)
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> getActiveRidesForDriver(String driverId) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("driverId").is(driverId),
                Criteria.where("status").is("ACCEPTED")
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> filterByStatusAndLocation(String status, String keyword) {
        Criteria locationCriteria = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(keyword, "i"),
                Criteria.where("dropLocation").regex(keyword, "i")
        );
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("status").is(status),
                locationCriteria
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> advancedSearch(String keyword, String status, String sortBy, String sortDir, int page, int size) {
        Criteria criteria = new Criteria();

        if (keyword != null && !keyword.isEmpty()) {
            criteria.orOperator(
                    Criteria.where("pickupLocation").regex(keyword, "i"),
                    Criteria.where("dropLocation").regex(keyword, "i")
            );
        }

        if (status != null && !status.isEmpty()) {
            criteria.and("status").is(status);
        }

        Query query = new Query(criteria);

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            query.with(Sort.by(direction, sortBy));
        }

        query.skip((long) page * size).limit(size);

        return mongoTemplate.find(query, Ride.class);
    }

    public List<Ride> getRidesByDate(LocalDate date) {
        Date startOfDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Criteria criteria = Criteria.where("createdDate").gte(startOfDay).lt(endOfDay);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }
}
