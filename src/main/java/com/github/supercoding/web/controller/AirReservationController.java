package com.github.supercoding.web.controller;

import com.github.supercoding.respository.userDetails.CustomUserDetails;
import com.github.supercoding.service.AirReservationService;
import com.github.supercoding.service.exceptions.InvalidValueException;
import com.github.supercoding.service.exceptions.NotAcceptException;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.web.dto.airline.ReservationRequest;
import com.github.supercoding.web.dto.airline.ReservationResult;
import com.github.supercoding.web.dto.airline.Ticket;
import com.github.supercoding.web.dto.airline.TicketResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/air-reservation")
@Slf4j
public class AirReservationController {

    private AirReservationService airReservationService;

    public AirReservationController(AirReservationService airReservationService) {
        this.airReservationService = airReservationService;
    }

    @GetMapping("/tickets")
    public TicketResponse findAirlineTickets(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
//            @RequestParam("user-Id")Integer userId,
            @RequestParam("airline-ticket-type")String ticketType){
            Integer userId = customUserDetails.getUserId();
            List<Ticket> tickets = airReservationService.findUserFavoritePlaceTickets(userId,ticketType);
            return new TicketResponse(tickets);
    }

    @PostMapping("/reservations")
    public ReservationResult makeReservation(@RequestBody ReservationRequest reservationRequest){
          return  airReservationService.makeReservation(reservationRequest);
    }

    @ApiOperation("userId의 예약한 항공편과 수수료 총합")
    @GetMapping("/users-sum-price")
    public Double findUserFlightSumPrice(
            @ApiParam(name="user-Id",value = "유저 ID",example = "1")@RequestParam("user-id") Integer userId
    ) {
        Double sum = airReservationService.findUserFlightSumPrice(userId);
        return sum;
    }


}
