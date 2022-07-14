package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PoreskaKategorija.
 */
@Entity
@Table(name = "poreska_kategorija")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PoreskaKategorija implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "naziv")
    private String naziv;

    @OneToMany(mappedBy = "poreskaKategorija")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "poreskaKategorija" }, allowSetters = true)
    private Set<PoreskaStopa> poreskaStopas = new HashSet<>();

    @OneToMany(mappedBy = "poreskaKategorija")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cenas", "jedinicaMere", "poreskaKategorija", "stavkaFaktures" }, allowSetters = true)
    private Set<RobaIliUsluga> robaIliUslugas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PoreskaKategorija id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return this.naziv;
    }

    public PoreskaKategorija naziv(String naziv) {
        this.setNaziv(naziv);
        return this;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Set<PoreskaStopa> getPoreskaStopas() {
        return this.poreskaStopas;
    }

    public void setPoreskaStopas(Set<PoreskaStopa> poreskaStopas) {
        if (this.poreskaStopas != null) {
            this.poreskaStopas.forEach(i -> i.setPoreskaKategorija(null));
        }
        if (poreskaStopas != null) {
            poreskaStopas.forEach(i -> i.setPoreskaKategorija(this));
        }
        this.poreskaStopas = poreskaStopas;
    }

    public PoreskaKategorija poreskaStopas(Set<PoreskaStopa> poreskaStopas) {
        this.setPoreskaStopas(poreskaStopas);
        return this;
    }

    public PoreskaKategorija addPoreskaStopa(PoreskaStopa poreskaStopa) {
        this.poreskaStopas.add(poreskaStopa);
        poreskaStopa.setPoreskaKategorija(this);
        return this;
    }

    public PoreskaKategorija removePoreskaStopa(PoreskaStopa poreskaStopa) {
        this.poreskaStopas.remove(poreskaStopa);
        poreskaStopa.setPoreskaKategorija(null);
        return this;
    }

    public Set<RobaIliUsluga> getRobaIliUslugas() {
        return this.robaIliUslugas;
    }

    public void setRobaIliUslugas(Set<RobaIliUsluga> robaIliUslugas) {
        if (this.robaIliUslugas != null) {
            this.robaIliUslugas.forEach(i -> i.setPoreskaKategorija(null));
        }
        if (robaIliUslugas != null) {
            robaIliUslugas.forEach(i -> i.setPoreskaKategorija(this));
        }
        this.robaIliUslugas = robaIliUslugas;
    }

    public PoreskaKategorija robaIliUslugas(Set<RobaIliUsluga> robaIliUslugas) {
        this.setRobaIliUslugas(robaIliUslugas);
        return this;
    }

    public PoreskaKategorija addRobaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.robaIliUslugas.add(robaIliUsluga);
        robaIliUsluga.setPoreskaKategorija(this);
        return this;
    }

    public PoreskaKategorija removeRobaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.robaIliUslugas.remove(robaIliUsluga);
        robaIliUsluga.setPoreskaKategorija(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoreskaKategorija)) {
            return false;
        }
        return id != null && id.equals(((PoreskaKategorija) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoreskaKategorija{" +
            "id=" + getId() +
            ", naziv='" + getNaziv() + "'" +
            "}";
    }
}
