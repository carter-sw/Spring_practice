package com.github.supercoding.respository.reservation;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private Integer reservationId;
    private Integer passengerId;
    private Integer airlineTickId;
    private String reservationStatus;
    private LocalDateTime reserveAt;

    public Reservation(Integer passengerId,Integer airlineTickId){
        this.passengerId = passengerId;
        this.airlineTickId = airlineTickId;
        this.reservationStatus = "대기";
        this.reserveAt = LocalDateTime.now();
    }

    public Reservation(Integer reservationId, Integer passengerId, Integer airlineTickId, String reservationStatus, LocalDateTime reserveAt) {
        this.reservationId = reservationId;
        this.passengerId = passengerId;
        this.airlineTickId = airlineTickId;
        this.reservationStatus = reservationStatus;
        this.reserveAt = reserveAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", passengerId=" + passengerId +
                ", airlineTickId=" + airlineTickId +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", reserveAt=" + reserveAt +
                '}';
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Integer passengerId) {
        this.passengerId = passengerId;
    }

    public Integer getAirlineTickId() {
        return airlineTickId;
    }

    public void setAirlineTickId(Integer airlineTickId) {
        this.airlineTickId = airlineTickId;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public LocalDateTime getReserveAt() {
        return reserveAt;
    }

    public void setReserveAt(LocalDateTime reserveAt) {
        this.reserveAt = reserveAt;
    }
}
