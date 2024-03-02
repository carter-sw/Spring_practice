package com.github.supercoding.respository.airlineTicket;

import com.github.supercoding.respository.flight.Flight;
import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.MediaSize;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "airline_ticket")
public class AirlineTicket {
    @Id @Column(name = "ticket_id")
    private Integer ticketId;
    @Column(name = "ticket_type")
    private String ticketType;
    @Column(name = "daparture_loc")
    private String departureLocation;
    @Column(name = "arrival_loc")
    private String arrivalLocation;
    @Column(name = "departure_at")
    private LocalDateTime departureAt;
    @Column(name = "return_at")
    private LocalDateTime returnAt;
    @Column(name = "tax")
    private Double tax;
    @Column(name = "total_price")
    private Double totalPrice;

    @OneToMany(mappedBy = "airlineTicket")
    private List<Flight> flightList;





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirlineTicket that = (AirlineTicket) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }


}
