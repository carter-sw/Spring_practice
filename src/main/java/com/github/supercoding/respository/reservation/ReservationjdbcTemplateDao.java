package com.github.supercoding.respository.reservation;

import com.github.supercoding.respository.airlineTicket.AirlineTicket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public class ReservationjdbcTemplateDao implements ReservationRepository {
    private JdbcTemplate jdbcTemplate;

    public ReservationjdbcTemplateDao(@Qualifier("jdbcTemplate2") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static RowMapper<Reservation> reservationRowMapper = ((rs, rowNums) -> new Reservation(
            rs.getInt("reservation_id"),
            rs.getInt("passenger_id"),
            rs.getInt("airline_ticket_id"),
            rs.getNString("reservation_status"),
            rs.getDate("reserve_at").toLocalDate().atStartOfDay()
    ));


    @Override
    public Boolean saveReservation(Reservation reservation) {
        Integer rowNums = jdbcTemplate.update("INSERT INTO reservation(passenger_id, airline_ticket_id, reservation_status, reserve_at) VALUES (? ,? , ?, ? )",
                reservation.getPassengerId(), reservation.getAirlineTickId(), reservation.getReservationStatus(),
                new Date(Timestamp.valueOf(reservation.getReserveAt()).getTime()));

        return rowNums > 0;
    }

    @Override
    public Reservation findReservationWithPassengerIdAndAirLineTicketId(Integer passengerId, Integer airlineTicketId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM reservation WHERE passenger_id = ? AND airline_ticket_id = ?", reservationRowMapper, passengerId, airlineTicketId);
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public void updateReservationStatus(Integer reservationId, String status) {
        jdbcTemplate.update("UPDATE reservation " +
                "       SET reservation_status = ? " +
                "       WHERE reservation_id = ? ", status, reservationId);
    }
    }

