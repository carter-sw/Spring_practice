package com.github.supercoding.respository.passenger;

import java.util.Objects;

public class Passenger {

    private Integer passengerId;
    private Integer userId;
    private String passportNum;

    public Passenger(Integer passengerId, Integer userId, String passportNum) {
        this.passengerId = passengerId;
        this.userId = userId;
        this.passportNum = passportNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(passengerId, passenger.passengerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerId);
    }

    public Integer getPassengerId() {
        return passengerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPassportNum() {
        return passportNum;
    }
}
