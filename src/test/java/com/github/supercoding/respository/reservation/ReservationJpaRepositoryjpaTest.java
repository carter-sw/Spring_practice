package com.github.supercoding.respository.reservation;

import com.github.supercoding.respository.airlineTicket.AirlineTicket;
import com.github.supercoding.respository.airlineTicket.AirlineTicketJpaRepository;
import com.github.supercoding.respository.passenger.Passenger;
import com.github.supercoding.respository.passenger.PasserngerJpaRepository;
import com.github.supercoding.service.AirReservationService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Slice Test => DAO lay / Jpa 사용하고 있는 Slice test
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class ReservationJpaRepositoryjpaTest {

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private PasserngerJpaRepository passerngerJpaRepository;

    @Autowired
    private AirlineTicketJpaRepository airlineTicketJpaRepository;

    @DisplayName("ReservationRepository로 항공편 가격과 수수료 검색")
    @Test
    void findFlightPriceAndCharge() {
        // given
        Integer userId = 10;

        //when
        List<FlightPriceAndCharge> flightPriceAndCharges = reservationJpaRepository.findFlightPriceAndCharge(userId);

        //then
        log.info("결과: "+flightPriceAndCharges);
    }

    @DisplayName("Reservation 예약 진행")
    @Test
    void saveReservation(){

        //given
        Integer userId = 10;
        Integer ticketId = 5;

        Passenger passenger = passerngerJpaRepository.findPassengerByUserId_UserId(userId).get();
        AirlineTicket airlineTicket = airlineTicketJpaRepository.findById(5).get();

        //when
        Reservation reservation = new Reservation(passenger,airlineTicket);
        Reservation res = reservationJpaRepository.save(reservation );

        // then
        log.info("결과: "+ res);

        assertEquals(res.getPassenger(),passenger);
        assertEquals(res.getAirlineTick(),airlineTicket);

    }

}