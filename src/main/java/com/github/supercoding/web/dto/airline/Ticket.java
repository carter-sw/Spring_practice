package com.github.supercoding.web.dto.airline;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.supercoding.respository.airlineTicket.AirlineTicket;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class Ticket {
    private String depart;
    private String arrival;
    private String departureTime;
    private String returnTime;
    private Integer ticketId;



    public Ticket() {
    }

    private static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Ticket(AirlineTicket airlineTicket){
        this.ticketId = airlineTicket.getTicketId();
        this.depart = airlineTicket.getDepartureLocation();
        this.arrival = airlineTicket.getArrivalLocation();
        this.departureTime = airlineTicket.getDepartureAt().format(formatter);
        this.returnTime = airlineTicket.getReturnAt().format(formatter);
    }

    public String getDepart() {
        return depart;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public static void setFormatter(DateTimeFormatter formatter) {
        Ticket.formatter = formatter;
    }
}
