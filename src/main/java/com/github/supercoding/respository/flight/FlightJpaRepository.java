package com.github.supercoding.respository.flight;

import com.github.supercoding.respository.storeSales.StoreSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightJpaRepository extends JpaRepository<Flight, Integer> {
}
