package com.myapp.repository;

import com.myapp.domain.PoslovnaGodina;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PoslovnaGodina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoslovnaGodinaRepository extends JpaRepository<PoslovnaGodina, Long> {}
