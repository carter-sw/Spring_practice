package com.github.supercoding.respository.passenger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface PasserngerJpaRepository extends JpaRepository<Passenger,Integer> {

    Optional<Passenger> findPassengerByUserId_UserId(Integer userId);
}
