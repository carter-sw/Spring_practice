package com.github.supercoding.respository.users;

import com.github.supercoding.respository.passenger.Passenger;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="userId")
@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    @Column(name = "user_name",length = 20)
    private String userName;
    @Column(name = "like_travel_place",length = 30)
    private String likeTravelPlace;
    @Column(name = "phone_num", length = 30)
    private String phoneNum;

    @OneToOne(mappedBy = "userId")
    private Passenger passenger;


}
