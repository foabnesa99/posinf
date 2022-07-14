package com.myapp.repository;

import com.myapp.domain.IzlaznaFaktura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IzlaznaFaktura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IzlaznaFakturaRepository extends JpaRepository<IzlaznaFaktura, Long> {}
