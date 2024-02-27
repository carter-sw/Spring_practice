package com.github.supercoding.service;

import com.github.supercoding.respository.airlineTicket.AirlinTicketRepository;
import com.github.supercoding.respository.airlineTicket.AirlineTicket;
import com.github.supercoding.respository.airlineTicket.AirlineTicketAndFlightInfo;
import com.github.supercoding.respository.passenger.Passenger;
import com.github.supercoding.respository.passenger.PassengerRepository;
import com.github.supercoding.respository.reservation.Reservation;
import com.github.supercoding.respository.users.UserEntity;
import com.github.supercoding.respository.users.UserRepository;
import com.github.supercoding.respository.reservation.ReservationRepository;
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

    private UserRepository userRepository;
    private AirlinTicketRepository airlinTicketRepository;
    private ReservationRepository reservationRepository;
    private PassengerRepository passengerRepository;

    public AirReservationService(UserRepository userRepository, AirlinTicketRepository airlinTicketRepository, ReservationRepository reservationRepository, PassengerRepository passengerRepository) {
        this.userRepository = userRepository;
        this.airlinTicketRepository = airlinTicketRepository;
        this.reservationRepository = reservationRepository;
        this.passengerRepository = passengerRepository;
    }

    public List<Ticket> findUserFavoritePlaceTickets(Integer userId, String ticketType) {
        // 필요한 Repository : UserRepository, airlinmeTicketRepository
        // 1. 유저를 userId로 가져와서 , 선호하는 여행지 도출
        // 2. 선호하는 여행지와 ticketType으로 AirlineTiket table 질의 해서 필요한 AirlineTicket
        // 3.  이 둘의 정보를 조합해서 Ticket DTO를 만든다.

        Set<String> ticketTypeSet = new HashSet<>(Arrays.asList("편도","왕복"));

        if(!ticketTypeSet.contains(ticketType) )
            throw new InvalidValueException("해당 TicketType "+ticketType+ " 은 지원하지 않습니다.");

        UserEntity userEntity = userRepository.findUserById(userId).orElseThrow(
                ()->new NotFoundException("해당 ID:"+userId+"유저를 찾을 수 없습니다."));
        String likePlace = userEntity.getLikeTravelPlace();

        List<AirlineTicket> airlineTickets =
                airlinTicketRepository.findAllAirlineTicketsWithPlaceAndTicketType(likePlace,ticketType);

        if(airlineTickets.isEmpty())
            throw new NotFoundException("해당 likePlace: "+likePlace+"와 TicketType: "+ticketType+"에 해당하는 항공권 찾을 수 없습니다.");

        List<Ticket> tickets = airlineTickets.stream().map(TicketMapper.INSTANCE::airlineTicketToTicket).collect(Collectors.toList());
        return  tickets;

    }


    @Transactional (transactionManager = "tm2")
    public ReservationResult makeReservation(ReservationRequest reservationRequest) {

        // 1. Reservation Repository,Passenger Repository, Join table (flight / airline_ticket) ,

        // 0. userId,airline_ticket_id
        Integer userId = reservationRequest.getUserId();
        Integer airlineTicketId = reservationRequest.getAirlineTicketId();

        // 1. Passenger I
        Passenger passenger = passengerRepository.findPassengerByUserId(userId)
                .orElseThrow(() -> new NotFoundException("요청하신 userId"+userId+"에 해당하는 Passenger를 찾을 수 없습니다."));
        Integer passengerId = passenger.getPassengerId();;

        // 2. price 정보 불러오기
        List<AirlineTicketAndFlightInfo> airlineTicketAndFlightInfos
                = airlinTicketRepository.findAllAirlineTicketAndFlightInfo(airlineTicketId);

        if(airlineTicketAndFlightInfos.isEmpty())
            throw new NotFoundException("AirlineTicket Id "+airlineTicketId+"에 해당하는 항공편과 항공권 찾을 수 없습니다.");

        Boolean isSuccess = false;
        // 3. reservation 생성
        Reservation reservation = new Reservation(passengerId,airlineTicketId);
        try {
            isSuccess = reservationRepository.saveReservation(reservation);
        }catch (RuntimeException e){
            throw new NotFoundException("Reservation이 등록되는 과정이 거부되었습니다.");
        }

        //ReservationResult DTO 만들기
        List<Integer> prices = airlineTicketAndFlightInfos.stream().map(AirlineTicketAndFlightInfo::getPrice).collect(Collectors.toList());
        List<Integer> charges = airlineTicketAndFlightInfos.stream().map(AirlineTicketAndFlightInfo::getCharge).collect(Collectors.toList());
        Integer tax = airlineTicketAndFlightInfos.stream().map(AirlineTicketAndFlightInfo::getTax).findFirst().get();
        Integer totalPrice = airlineTicketAndFlightInfos.stream().map(AirlineTicketAndFlightInfo::getTotalPrice).findFirst().get();

        return new ReservationResult(prices,charges,tax,totalPrice,isSuccess);
    }
}
