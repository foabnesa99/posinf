package com.myapp.repository;

import com.myapp.domain.RobaIliUsluga;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RobaIliUsluga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RobaIliUslugaRepository extends JpaRepository<RobaIliUsluga, Long> {}
