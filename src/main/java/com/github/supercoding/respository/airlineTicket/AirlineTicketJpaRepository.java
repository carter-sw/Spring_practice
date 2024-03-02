package com.github.supercoding.respository.airlineTicket;

import com.github.supercoding.respository.storeSales.StoreSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AirlineTicketJpaRepository extends JpaRepository<AirlineTicket, Integer> {
    List<AirlineTicket> findAllAirlineTicketsWithPlaceAndTicketType(String likePlace, String ticketType);

    List<AirlineTicketAndFlightInfo> findAllAirlineTicketAndFlightInfo(Integer airlineTicketId);
}
