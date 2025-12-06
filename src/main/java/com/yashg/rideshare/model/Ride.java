package com.yashg.rideshare.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "rides")
public class Ride {

    @Id
    private String id;

    private String userId;       // passenger id
    private String driverId;     // driver id

    private String pickupLocation;
    private String dropLocation;

    private String status;    // REQUESTED / ACCEPTED / COMPLETED
    private Date createdAt;
}
