package com.myapp.service;

import com.myapp.domain.IzlaznaFaktura;
import com.myapp.domain.StavkaFakture;
import com.myapp.repository.IzlaznaFakturaRepository;
import com.myapp.repository.StavkaFaktureRepository;
import org.springframework.stereotype.Service;

@Service
public class IzlaznaFakturaService {
    private final StavkaFaktureRepository stavkaFaktureRepository;
    private final IzlaznaFakturaRepository izlaznaFakturaRepository;

    public IzlaznaFakturaService(StavkaFaktureRepository stavkaFaktureRepository, IzlaznaFakturaRepository izlaznaFakturaRepository) {
        this.stavkaFaktureRepository = stavkaFaktureRepository;
        this.izlaznaFakturaRepository = izlaznaFakturaRepository;
    }

    public void updateExistingFaktura(StavkaFakture stavkaFakture, Long faktura){
        IzlaznaFaktura updated = izlaznaFakturaRepository.getById(faktura);
        if(updated.getUkupnaOsnovica() == null)updated.setUkupnaOsnovica(stavkaFakture.getJedinicnaCena());
        else{
            updated.setUkupnaOsnovica(stavkaFakture.getJedinicnaCena() + updated.getUkupnaOsnovica());
        }
        if(updated.getUkupanIznos() == null){
            updated.setUkupanIznos(stavkaFakture.getUkupnaCena());
        }
        else{
            updated.setUkupanIznos(updated.getUkupanIznos() + stavkaFakture.getUkupnaCena());
        }
        if(updated.getUkupanPdv() == null){
            updated.setUkupanPdv(stavkaFakture.getIznosPDV());
        }
        else{
            updated.setUkupanPdv(updated.getUkupanPdv() + stavkaFakture.getIznosPDV());
        }
        izlaznaFakturaRepository.save(updated);
    }

}
