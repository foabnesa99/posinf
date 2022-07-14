package com.myapp.repository;

import com.myapp.domain.Kupac;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Kupac entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KupacRepository extends JpaRepository<Kupac, Long> {}
