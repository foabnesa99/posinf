package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cena.
 */
@Entity
@Table(name = "cena")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cena implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "datum_vazenja")
    private LocalDate datumVazenja;

    @Column(name = "iznos_cene")
    private Double iznosCene;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cenas", "jedinicaMere", "poreskaKategorija", "stavkaFaktures" }, allowSetters = true)
    private RobaIliUsluga robaIliUsluga;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cena id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatumVazenja() {
        return this.datumVazenja;
    }

    public Cena datumVazenja(LocalDate datumVazenja) {
        this.setDatumVazenja(datumVazenja);
        return this;
    }

    public void setDatumVazenja(LocalDate datumVazenja) {
        this.datumVazenja = datumVazenja;
    }

    public Double getIznosCene() {
        return this.iznosCene;
    }

    public Cena iznosCene(Double iznosCene) {
        this.setIznosCene(iznosCene);
        return this;
    }

    public void setIznosCene(Double iznosCene) {
        this.iznosCene = iznosCene;
    }

    public RobaIliUsluga getRobaIliUsluga() {
        return this.robaIliUsluga;
    }

    public void setRobaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.robaIliUsluga = robaIliUsluga;
    }

    public Cena robaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.setRobaIliUsluga(robaIliUsluga);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cena)) {
            return false;
        }
        return id != null && id.equals(((Cena) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cena{" +
            "id=" + getId() +
            ", datumVazenja='" + getDatumVazenja() + "'" +
            ", iznosCene=" + getIznosCene() +
            "}";
    }
}
