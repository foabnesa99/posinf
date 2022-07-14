package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.Max;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PoreskaStopa.
 */
@Entity
@Table(name = "poreska_stopa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PoreskaStopa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Max(value = 50, message= "Stopa PDV-a ne moze biti veca od 50%!")
    @Column(name = "procenat_pdv")
    private Double procenatPdv;

    @Column(name = "datum_vazenja")
    private LocalDate datumVazenja;

    @ManyToOne
    @JsonIgnoreProperties(value = { "poreskaStopas", "robaIliUslugas" }, allowSetters = true)
    private PoreskaKategorija poreskaKategorija;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PoreskaStopa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getProcenatPdv() {
        return this.procenatPdv;
    }

    public PoreskaStopa procenatPdv(Double procenatPdv) {
        this.setProcenatPdv(procenatPdv);
        return this;
    }

    public void setProcenatPdv(Double procenatPdv) {
        this.procenatPdv = procenatPdv;
    }

    public LocalDate getDatumVazenja() {
        return this.datumVazenja;
    }

    public PoreskaStopa datumVazenja(LocalDate datumVazenja) {
        this.setDatumVazenja(datumVazenja);
        return this;
    }

    public void setDatumVazenja(LocalDate datumVazenja) {
        this.datumVazenja = datumVazenja;
    }

    public PoreskaKategorija getPoreskaKategorija() {
        return this.poreskaKategorija;
    }

    public void setPoreskaKategorija(PoreskaKategorija poreskaKategorija) {
        this.poreskaKategorija = poreskaKategorija;
    }

    public PoreskaStopa poreskaKategorija(PoreskaKategorija poreskaKategorija) {
        this.setPoreskaKategorija(poreskaKategorija);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoreskaStopa)) {
            return false;
        }
        return id != null && id.equals(((PoreskaStopa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoreskaStopa{" +
            "id=" + getId() +
            ", procenatPdv=" + getProcenatPdv() +
            ", datumVazenja='" + getDatumVazenja() + "'" +
            "}";
    }
}
