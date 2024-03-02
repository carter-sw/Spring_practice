package com.github.supercoding.respository.reservation;

import com.github.supercoding.respository.airlineTicket.AirlineTicket;
import com.github.supercoding.respository.passenger.Passenger;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;
@Getter
@Setter
@ToString
@EqualsAndHashCode(of="reservationId")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id @Column(name = "reservation_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_ticket_id")
    private AirlineTicket airlineTick;

    @Column(name = "reservation_status",length = 10)
    private String reservationStatus;

    @Column(name = "reserve_at")
    private LocalDateTime reserveAt;

    public Reservation(Passenger passenger,AirlineTicket airlineTick){
        this.passenger = passenger;
        this.airlineTick = airlineTick;
        this.reservationStatus = "대기";
        this.reserveAt = LocalDateTime.now();
    }
}
