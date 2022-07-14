package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Preduzece.
 */
@Entity
@Table(name = "preduzece")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Preduzece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pib")
    private String pib;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "adresa")
    private String adresa;

    @OneToMany(mappedBy = "preduzece")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kupac", "preduzece", "stavkaFaktures" }, allowSetters = true)
    private Set<IzlaznaFaktura> izlaznaFakturas = new HashSet<>();

    @OneToMany(mappedBy = "preduzece")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "preduzece" }, allowSetters = true)
    private Set<PoslovnaGodina> poslovnaGodinas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Preduzece id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPib() {
        return this.pib;
    }

    public Preduzece pib(String pib) {
        this.setPib(pib);
        return this;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public String getNaziv() {
        return this.naziv;
    }

    public Preduzece naziv(String naziv) {
        this.setNaziv(naziv);
        return this;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return this.adresa;
    }

    public Preduzece adresa(String adresa) {
        this.setAdresa(adresa);
        return this;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Set<IzlaznaFaktura> getIzlaznaFakturas() {
        return this.izlaznaFakturas;
    }

    public void setIzlaznaFakturas(Set<IzlaznaFaktura> izlaznaFakturas) {
        if (this.izlaznaFakturas != null) {
            this.izlaznaFakturas.forEach(i -> i.setPreduzece(null));
        }
        if (izlaznaFakturas != null) {
            izlaznaFakturas.forEach(i -> i.setPreduzece(this));
        }
        this.izlaznaFakturas = izlaznaFakturas;
    }

    public Preduzece izlaznaFakturas(Set<IzlaznaFaktura> izlaznaFakturas) {
        this.setIzlaznaFakturas(izlaznaFakturas);
        return this;
    }

    public Preduzece addIzlaznaFaktura(IzlaznaFaktura izlaznaFaktura) {
        this.izlaznaFakturas.add(izlaznaFaktura);
        izlaznaFaktura.setPreduzece(this);
        return this;
    }

    public Preduzece removeIzlaznaFaktura(IzlaznaFaktura izlaznaFaktura) {
        this.izlaznaFakturas.remove(izlaznaFaktura);
        izlaznaFaktura.setPreduzece(null);
        return this;
    }

    public Set<PoslovnaGodina> getPoslovnaGodinas() {
        return this.poslovnaGodinas;
    }

    public void setPoslovnaGodinas(Set<PoslovnaGodina> poslovnaGodinas) {
        if (this.poslovnaGodinas != null) {
            this.poslovnaGodinas.forEach(i -> i.setPreduzece(null));
        }
        if (poslovnaGodinas != null) {
            poslovnaGodinas.forEach(i -> i.setPreduzece(this));
        }
        this.poslovnaGodinas = poslovnaGodinas;
    }

    public Preduzece poslovnaGodinas(Set<PoslovnaGodina> poslovnaGodinas) {
        this.setPoslovnaGodinas(poslovnaGodinas);
        return this;
    }

    public Preduzece addPoslovnaGodina(PoslovnaGodina poslovnaGodina) {
        this.poslovnaGodinas.add(poslovnaGodina);
        poslovnaGodina.setPreduzece(this);
        return this;
    }

    public Preduzece removePoslovnaGodina(PoslovnaGodina poslovnaGodina) {
        this.poslovnaGodinas.remove(poslovnaGodina);
        poslovnaGodina.setPreduzece(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Preduzece)) {
            return false;
        }
        return id != null && id.equals(((Preduzece) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Preduzece{" +
            "id=" + getId() +
            ", pib='" + getPib() + "'" +
            ", naziv='" + getNaziv() + "'" +
            ", adresa='" + getAdresa() + "'" +
            "}";
    }
}
