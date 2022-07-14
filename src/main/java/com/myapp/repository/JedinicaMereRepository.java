package com.myapp.repository;

import com.myapp.domain.JedinicaMere;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the JedinicaMere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JedinicaMereRepository extends JpaRepository<JedinicaMere, Long> {}
