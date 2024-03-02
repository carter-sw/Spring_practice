package com.github.supercoding.service;


import com.github.supercoding.respository.airlineTicket.AirlineTicket;
import com.github.supercoding.respository.airlineTicket.AirlineTicketJpaRepository;
import com.github.supercoding.respository.flight.Flight;
import com.github.supercoding.respository.flight.FlightJpaRepository;
import com.github.supercoding.respository.passenger.Passenger;
import com.github.supercoding.respository.passenger.PasserngerJpaRepository;
import com.github.supercoding.respository.reservation.FlightPriceAndCharge;
import com.github.supercoding.respository.reservation.Reservation;
import com.github.supercoding.respository.reservation.ReservationJpaRepository;
import com.github.supercoding.respository.users.UserEntity;
import com.github.supercoding.respository.users.UserJpaRepository;
import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.service.mapper.TicketMapper;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AirReservationService {

   private ReservationJpaRepository reservationJpaRepository;
   private UserJpaRepository userJpaRepository;
   private PasserngerJpaRepository passerngerJpaRepository;
   private AirlineTicketJpaRepository airlineTicketJpaRepository;
   private FlightJpaRepository flightJpaRepository;

    public AirReservationService(ReservationJpaRepository reservationJpaRepository, UserJpaRepository userJpaRepository, PasserngerJpaRepository passerngerJpaRepository, AirlineTicketJpaRepository airlineTicketJpaRepository, FlightJpaRepository flightJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.passerngerJpaRepository = passerngerJpaRepository;
        this.airlineTicketJpaRepository = airlineTicketJpaRepository;
        this.flightJpaRepository = flightJpaRepository;
    }

    public List<Ticket> findUserFavoritePlaceTickets(Integer userId, String ticketType) {
        // 필요한 Repository : UserRepository, airlinmeTicketRepository
        // 1. 유저를 userId로 가져와서 , 선호하는 여행지 도출
        // 2. 선호하는 여행지와 ticketType으로 AirlineTiket table 질의 해서 필요한 AirlineTicket
        // 3.  이 둘의 정보를 조합해서 Ticket DTO를 만든다.

        Set<String> ticketTypeSet = new HashSet<>(Arrays.asList("편도","왕복"));

        if(!ticketTypeSet.contains(ticketType) )
            throw new InvalidValueException("해당 TicketType "+ticketType+ " 은 지원하지 않습니다.");

        UserEntity userEntity = userJpaRepository.findById(userId).orElseThrow(
                ()->new NotFoundException("해당 ID:"+userId+"유저를 찾을 수 없습니다."));
        String likePlace = userEntity.getLikeTravelPlace();

        List<AirlineTicket> airlineTickets =
                airlineTicketJpaRepository.findAllAirlineTicketsWithPlaceAndTicketType(likePlace,ticketType);

        if(airlineTickets.isEmpty())
            throw new NotFoundException("해당 likePlace: "+likePlace+"와 TicketType: "+ticketType+"에 해당하는 항공권 찾을 수 없습니다.");

        List<Ticket> tickets = airlineTickets.stream().map(TicketMapper.INSTANCE::airlineTicketToTicket).collect(Collectors.toList());
        return  tickets;

    }


    @Transactional(transactionManager = "tm2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {

        // 1. Reservation Repository,Passenger Repository, Join table (flight / airline_ticket) ,

        // 0. userId,airline_ticket_id
        Integer userId = reservationRequest.getUserId();
        Integer airlineTicketId = reservationRequest.getAirlineTicketId();

        AirlineTicket airlineTicket = airlineTicketJpaRepository.findById(airlineTicketId).orElseThrow(() -> new NotFoundException("에어라인 티켓을 찾을수없습니다."));

        // 1. Passenger I
        Passenger passenger = passerngerJpaRepository.findPassengerByUserId_UserId(userId)
                .orElseThrow(() -> new NotFoundException("요청하신 userId"+userId+"에 해당하는 Passenger를 찾을 수 없습니다."));
        Integer passengerId = passenger.getPassengerId();;

        // 2. price 정보 불러오기
        List<Flight> flightList = airlineTicket.getFlightList();

        if(flightList.isEmpty())
            throw new NotFoundException("AirlineTicket Id "+airlineTicketId+"에 해당하는 항공편과 항공권 찾을 수 없습니다.");

        Boolean isSuccess = false;
        // 3. reservation 생성
        Reservation reservation = new Reservation(passenger,airlineTicket);
        try {
            reservationJpaRepository.save(reservation);
            isSuccess =true;
        }catch (RuntimeException e){
            throw new NotFoundException("Reservation이 등록되는 과정이 거부되었습니다.");
        }

        //ReservationResult DTO 만들기
        List<Integer> prices = flightList.stream().map(Flight::getFlightPrice).map(Double::intValue).collect(Collectors.toList());
        List<Integer> charges = flightList.stream().map(Flight::getCharge).map(Double::intValue).collect(Collectors.toList());
        Integer tax = airlineTicket.getTax().intValue();
        Integer totalPrice = airlineTicket.getTotalPrice().intValue();

        return new ReservationResult(prices,charges,tax,totalPrice,isSuccess);
    }

    public Double findUserFlightSumPrice(Integer userId) {
        // 1. flight_price , Charge 구하기
        List<FlightPriceAndCharge> flightPriceAndCharges = reservationJpaRepository.findFlightPriceAndCharge(userId);

        // 2. 모든 Flight_price와 charge의 각각 합을 구하고
        Double flightSum = flightPriceAndCharges.stream().mapToDouble(FlightPriceAndCharge::getFlightPrice).sum();
        Double chargeSum = flightPriceAndCharges.stream().mapToDouble(FlightPriceAndCharge::getCharge).sum();

        // 3.두개의 합을 다시 더하고 return
        return flightSum+chargeSum;
    }
}
