package com.myapp.repository;

import com.myapp.domain.PoreskaKategorija;
import com.myapp.domain.PoreskaStopa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data SQL repository for the PoreskaStopa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoreskaStopaRepository extends JpaRepository<PoreskaStopa, Long> {

    List<PoreskaStopa> getPoreskaStopasByPoreskaKategorijaAndDatumVazenjaBefore(PoreskaKategorija kategorija, LocalDate datumVazenja);

}
