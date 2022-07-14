package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RobaIliUsluga.
 */
@Entity
@Table(name = "roba_ili_usluga")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RobaIliUsluga implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "naziv")
    private String naziv;

    @Lob
    @Column(name = "opis")
    private String opis;

    @OneToMany(mappedBy = "robaIliUsluga")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "robaIliUsluga" }, allowSetters = true)
    private Set<Cena> cenas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "robaIliUslugas" }, allowSetters = true)
    private JedinicaMere jedinicaMere;

    @ManyToOne
    @JsonIgnoreProperties(value = { "poreskaStopas", "robaIliUslugas" }, allowSetters = true)
    private PoreskaKategorija poreskaKategorija;

    @OneToMany(mappedBy = "robaIliUsluga")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "izlaznaFaktura", "robaIliUsluga" }, allowSetters = true)
    private Set<StavkaFakture> stavkaFaktures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RobaIliUsluga id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return this.naziv;
    }

    public RobaIliUsluga naziv(String naziv) {
        this.setNaziv(naziv);
        return this;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return this.opis;
    }

    public RobaIliUsluga opis(String opis) {
        this.setOpis(opis);
        return this;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Set<Cena> getCenas() {
        return this.cenas;
    }

    public void setCenas(Set<Cena> cenas) {
        if (this.cenas != null) {
            this.cenas.forEach(i -> i.setRobaIliUsluga(null));
        }
        if (cenas != null) {
            cenas.forEach(i -> i.setRobaIliUsluga(this));
        }
        this.cenas = cenas;
    }

    public RobaIliUsluga cenas(Set<Cena> cenas) {
        this.setCenas(cenas);
        return this;
    }

    public RobaIliUsluga addCena(Cena cena) {
        this.cenas.add(cena);
        cena.setRobaIliUsluga(this);
        return this;
    }

    public RobaIliUsluga removeCena(Cena cena) {
        this.cenas.remove(cena);
        cena.setRobaIliUsluga(null);
        return this;
    }

    public JedinicaMere getJedinicaMere() {
        return this.jedinicaMere;
    }

    public void setJedinicaMere(JedinicaMere jedinicaMere) {
        this.jedinicaMere = jedinicaMere;
    }

    public RobaIliUsluga jedinicaMere(JedinicaMere jedinicaMere) {
        this.setJedinicaMere(jedinicaMere);
        return this;
    }

    public PoreskaKategorija getPoreskaKategorija() {
        return this.poreskaKategorija;
    }

    public void setPoreskaKategorija(PoreskaKategorija poreskaKategorija) {
        this.poreskaKategorija = poreskaKategorija;
    }

    public RobaIliUsluga poreskaKategorija(PoreskaKategorija poreskaKategorija) {
        this.setPoreskaKategorija(poreskaKategorija);
        return this;
    }

    public Set<StavkaFakture> getStavkaFaktures() {
        return this.stavkaFaktures;
    }

    public void setStavkaFaktures(Set<StavkaFakture> stavkaFaktures) {
        if (this.stavkaFaktures != null) {
            this.stavkaFaktures.forEach(i -> i.setRobaIliUsluga(null));
        }
        if (stavkaFaktures != null) {
            stavkaFaktures.forEach(i -> i.setRobaIliUsluga(this));
        }
        this.stavkaFaktures = stavkaFaktures;
    }

    public RobaIliUsluga stavkaFaktures(Set<StavkaFakture> stavkaFaktures) {
        this.setStavkaFaktures(stavkaFaktures);
        return this;
    }

    public RobaIliUsluga addStavkaFakture(StavkaFakture stavkaFakture) {
        this.stavkaFaktures.add(stavkaFakture);
        stavkaFakture.setRobaIliUsluga(this);
        return this;
    }

    public RobaIliUsluga removeStavkaFakture(StavkaFakture stavkaFakture) {
        this.stavkaFaktures.remove(stavkaFakture);
        stavkaFakture.setRobaIliUsluga(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RobaIliUsluga)) {
            return false;
        }
        return id != null && id.equals(((RobaIliUsluga) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RobaIliUsluga{" +
            "id=" + getId() +
            ", naziv='" + getNaziv() + "'" +
            ", opis='" + getOpis() + "'" +
            "}";
    }
}
