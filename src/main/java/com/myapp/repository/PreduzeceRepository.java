package com.myapp.repository;

import com.myapp.domain.Preduzece;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Preduzece entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreduzeceRepository extends JpaRepository<Preduzece, Long> {}
