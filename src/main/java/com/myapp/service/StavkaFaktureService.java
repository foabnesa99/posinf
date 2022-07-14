package com.myapp.service;

import com.myapp.domain.*;
import com.myapp.repository.CenaRepository;
import com.myapp.repository.PoreskaKategorijaRepository;
import com.myapp.repository.PoreskaStopaRepository;
import com.myapp.repository.RobaIliUslugaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service

public class StavkaFaktureService {

    private final Logger log = LoggerFactory.getLogger(StavkaFaktureService.class);

    public StavkaFaktureService(RobaIliUslugaRepository robaIliUslugaRepository, CenaRepository cenaRepository, PoreskaStopaRepository poreskaStopaRepository, PoreskaKategorijaRepository poreskaKategorijaRepository) {
        this.robaIliUslugaRepository = robaIliUslugaRepository;
        this.cenaRepository = cenaRepository;
        this.poreskaStopaRepository = poreskaStopaRepository;
        this.poreskaKategorijaRepository = poreskaKategorijaRepository;
    }

    private final RobaIliUslugaRepository robaIliUslugaRepository;

    private final CenaRepository cenaRepository;

    private final PoreskaStopaRepository poreskaStopaRepository;

    private final PoreskaKategorijaRepository poreskaKategorijaRepository;



    public StavkaFakture generateStavka(StavkaFakture stavkaFakture) {
        RobaIliUsluga robaIliUsluga = robaIliUslugaRepository.getById(stavkaFakture.getRobaIliUsluga().getId());
        PoreskaKategorija kategorija = poreskaKategorijaRepository.getById(robaIliUsluga.getPoreskaKategorija().getId());
        Comparator<Cena> comparator = Comparator.comparing(Cena::getDatumVazenja);
        Cena najnovijaCena = robaIliUsluga.getCenas().stream().max(comparator).get();
        stavkaFakture.setJedinicnaCena(BigDecimal.valueOf(najnovijaCena.getIznosCene() - (najnovijaCena.getIznosCene() / 100 * stavkaFakture.getRabat())).setScale(2, RoundingMode.HALF_UP).doubleValue());
        Comparator<PoreskaStopa> poreskaStopaComparator = Comparator.comparing(PoreskaStopa::getDatumVazenja);
        List<PoreskaStopa> stopaList = poreskaStopaRepository.getPoreskaStopasByPoreskaKategorijaAndDatumVazenjaBefore(kategorija, LocalDate.now().plusDays(1));
        log.info(" \n LISTA PORESKIH STOPA: " + stopaList);
        PoreskaStopa najnovijaStopa = stopaList.stream().max(poreskaStopaComparator).get();
        stavkaFakture.setProcenatPDV(najnovijaStopa.getProcenatPdv().intValue());
        Double jedinicnaCena = stavkaFakture.getJedinicnaCena();
        Integer procenatPdv = stavkaFakture.getProcenatPDV();
        stavkaFakture.setIznosPDV(BigDecimal.valueOf(jedinicnaCena / 100 * procenatPdv).setScale(2, RoundingMode.HALF_UP).doubleValue());
        stavkaFakture.setUkupnaCena(BigDecimal.valueOf(stavkaFakture.getKolicina() * (stavkaFakture.getJedinicnaCena() + stavkaFakture.getIznosPDV())).setScale(2, RoundingMode.HALF_UP).doubleValue());

        return stavkaFakture;
    }

}
