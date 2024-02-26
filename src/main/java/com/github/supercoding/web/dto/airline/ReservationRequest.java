package com.github.supercoding.web.dto.airline;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReservationRequest {

    private Integer userId;
    private Integer airlineTicketId;

    public ReservationRequest(Integer userId, Integer airlineTicketId) {
        this.userId = userId;
        this.airlineTicketId = airlineTicketId;
    }

    public ReservationRequest() {
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getAirlineTicketId() {
        return airlineTicketId;
    }
}
