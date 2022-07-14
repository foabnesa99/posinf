package com.myapp.repository;

import com.myapp.domain.Cena;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cena entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CenaRepository extends JpaRepository<Cena, Long> {}
