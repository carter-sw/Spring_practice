package com.github.supercoding.respository.passenger;

import com.github.supercoding.respository.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@EqualsAndHashCode(of="passengerId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "passenger")
public class Passenger {

    @Id @Column(name = "passenger_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passengerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,unique = true)
    private UserEntity userId;

    @Column(name = "passport_num",length = 50)
    private String passportNum;

}
