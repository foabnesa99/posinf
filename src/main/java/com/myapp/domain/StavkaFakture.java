package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StavkaFakture.
 */
@Entity
@Table(name = "stavka_fakture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StavkaFakture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kolicina")
    private Double kolicina = 0.0;

    @Column(name = "jedinicna_cena")
    private Double jedinicnaCena = 0.0;

    @Column(name = "rabat")
    private Integer rabat = 0;
    @Max(value = 50, message= "Stopa PDV-a ne moze biti veca od 50%!")
    @Column(name = "procenat_pdv")
    private Integer procenatPDV = 0;

    @Column(name = "iznos_pdv")
    private Double iznosPDV = 0.0;

    @Column(name = "ukupna_cena")
    private Double ukupnaCena = 0.0;

    @ManyToOne
    @JsonIgnoreProperties(value = { "kupac", "preduzece", "stavkaFaktures" }, allowSetters = true)
    private IzlaznaFaktura izlaznaFaktura;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cenas", "jedinicaMere", "poreskaKategorija", "stavkaFaktures" }, allowSetters = true)
    private RobaIliUsluga robaIliUsluga;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StavkaFakture id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getKolicina() {
        return this.kolicina;
    }

    public StavkaFakture kolicina(Double kolicina) {
        this.setKolicina(kolicina);
        return this;
    }

    public void setKolicina(Double kolicina) {
        this.kolicina = kolicina;
    }

    public Double getJedinicnaCena() {
        return this.jedinicnaCena;
    }

    public StavkaFakture jedinicnaCena(Double jedinicnaCena) {
        this.setJedinicnaCena(jedinicnaCena);
        return this;
    }

    public void setJedinicnaCena(Double jedinicnaCena) {
        this.jedinicnaCena = jedinicnaCena;
    }

    public Integer getRabat() {
        return this.rabat;
    }

    public StavkaFakture rabat(Integer rabat) {
        this.setRabat(rabat);
        return this;
    }

    public void setRabat(Integer rabat) {
        this.rabat = rabat;
    }

    public Integer getProcenatPDV() {
        return this.procenatPDV;
    }

    public StavkaFakture procenatPDV(Integer procenatPDV) {
        this.setProcenatPDV(procenatPDV);
        return this;
    }

    public void setProcenatPDV(Integer procenatPDV) {
        this.procenatPDV = procenatPDV;
    }

    public Double getIznosPDV() {
        return this.iznosPDV;
    }

    public StavkaFakture iznosPDV(Double iznosPDV) {
        this.setIznosPDV(iznosPDV);
        return this;
    }

    public void setIznosPDV(Double iznosPDV) {
        this.iznosPDV = iznosPDV;
    }

    public Double getUkupnaCena() {
        return this.ukupnaCena;
    }

    public StavkaFakture ukupnaCena(Double ukupnaCena) {
        this.setUkupnaCena(ukupnaCena);
        return this;
    }

    public void setUkupnaCena(Double ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public IzlaznaFaktura getIzlaznaFaktura() {
        return this.izlaznaFaktura;
    }

    public void setIzlaznaFaktura(IzlaznaFaktura izlaznaFaktura) {
        this.izlaznaFaktura = izlaznaFaktura;
    }

    public StavkaFakture izlaznaFaktura(IzlaznaFaktura izlaznaFaktura) {
        this.setIzlaznaFaktura(izlaznaFaktura);
        return this;
    }

    public RobaIliUsluga getRobaIliUsluga() {
        return this.robaIliUsluga;
    }

    public void setRobaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.robaIliUsluga = robaIliUsluga;
    }

    public StavkaFakture robaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.setRobaIliUsluga(robaIliUsluga);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StavkaFakture)) {
            return false;
        }
        return id != null && id.equals(((StavkaFakture) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StavkaFakture{" +
            "id=" + getId() +
            ", kolicina=" + getKolicina() +
            ", jedinicnaCena=" + getJedinicnaCena() +
            ", rabat=" + getRabat() +
            ", procenatPDV=" + getProcenatPDV() +
            ", iznosPDV=" + getIznosPDV() +
            ", ukupnaCena=" + getUkupnaCena() +
            "}";
    }
}
