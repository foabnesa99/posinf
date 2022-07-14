package com.myapp.repository;

import com.myapp.domain.PoreskaKategorija;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PoreskaKategorija entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoreskaKategorijaRepository extends JpaRepository<PoreskaKategorija, Long> {}
