package com.myapp.repository;

import com.myapp.domain.StavkaFakture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StavkaFakture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StavkaFaktureRepository extends JpaRepository<StavkaFakture, Long> {}
