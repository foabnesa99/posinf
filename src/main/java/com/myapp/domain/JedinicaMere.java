package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A JedinicaMere.
 */
@Entity
@Table(name = "jedinica_mere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JedinicaMere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "skraceni_naziv")
    private String skraceniNaziv;

    @OneToMany(mappedBy = "jedinicaMere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cenas", "jedinicaMere", "poreskaKategorija", "stavkaFaktures" }, allowSetters = true)
    private Set<RobaIliUsluga> robaIliUslugas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public JedinicaMere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return this.naziv;
    }

    public JedinicaMere naziv(String naziv) {
        this.setNaziv(naziv);
        return this;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSkraceniNaziv() {
        return this.skraceniNaziv;
    }

    public JedinicaMere skraceniNaziv(String skraceniNaziv) {
        this.setSkraceniNaziv(skraceniNaziv);
        return this;
    }

    public void setSkraceniNaziv(String skraceniNaziv) {
        this.skraceniNaziv = skraceniNaziv;
    }

    public Set<RobaIliUsluga> getRobaIliUslugas() {
        return this.robaIliUslugas;
    }

    public void setRobaIliUslugas(Set<RobaIliUsluga> robaIliUslugas) {
        if (this.robaIliUslugas != null) {
            this.robaIliUslugas.forEach(i -> i.setJedinicaMere(null));
        }
        if (robaIliUslugas != null) {
            robaIliUslugas.forEach(i -> i.setJedinicaMere(this));
        }
        this.robaIliUslugas = robaIliUslugas;
    }

    public JedinicaMere robaIliUslugas(Set<RobaIliUsluga> robaIliUslugas) {
        this.setRobaIliUslugas(robaIliUslugas);
        return this;
    }

    public JedinicaMere addRobaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.robaIliUslugas.add(robaIliUsluga);
        robaIliUsluga.setJedinicaMere(this);
        return this;
    }

    public JedinicaMere removeRobaIliUsluga(RobaIliUsluga robaIliUsluga) {
        this.robaIliUslugas.remove(robaIliUsluga);
        robaIliUsluga.setJedinicaMere(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JedinicaMere)) {
            return false;
        }
        return id != null && id.equals(((JedinicaMere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JedinicaMere{" +
            "id=" + getId() +
            ", naziv='" + getNaziv() + "'" +
            ", skraceniNaziv='" + getSkraceniNaziv() + "'" +
            "}";
    }
}
