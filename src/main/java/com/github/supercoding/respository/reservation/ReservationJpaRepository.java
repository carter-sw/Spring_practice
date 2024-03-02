package com.github.supercoding.respository.reservation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Integer> {


    @Query("SELECT new com.github.supercoding.respository.reservation.FlightPriceAndCharge(f.flightPrice, f.charge) " +
            "FROM Reservation r " +
            "JOIN r.passenger p " +
            "JOIN r.airlineTick a " +
            "JOIN a.flightList f " +
            "WHERE p.userId = :userId ")
    List<FlightPriceAndCharge> findFlightPriceAndCharge(Integer userId);
}
