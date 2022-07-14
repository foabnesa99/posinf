package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Kupac.
 */
@Entity
@Table(name = "kupac")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Kupac implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pib")
    private String pib;

    @Column(name = "mib")
    private String mib;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "adresa")
    private String adresa;

    @Column(name = "telefon")
    private String telefon;

    @OneToMany(mappedBy = "kupac")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "kupac", "preduzece", "stavkaFaktures" }, allowSetters = true)
    private Set<IzlaznaFaktura> izlaznaFakturas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Kupac id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPib() {
        return this.pib;
    }

    public Kupac pib(String pib) {
        this.setPib(pib);
        return this;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public String getMib() {
        return this.mib;
    }

    public Kupac mib(String mib) {
        this.setMib(mib);
        return this;
    }

    public void setMib(String mib) {
        this.mib = mib;
    }

    public String getNaziv() {
        return this.naziv;
    }

    public Kupac naziv(String naziv) {
        this.setNaziv(naziv);
        return this;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return this.adresa;
    }

    public Kupac adresa(String adresa) {
        this.setAdresa(adresa);
        return this;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public Kupac telefon(String telefon) {
        this.setTelefon(telefon);
        return this;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Set<IzlaznaFaktura> getIzlaznaFakturas() {
        return this.izlaznaFakturas;
    }

    public void setIzlaznaFakturas(Set<IzlaznaFaktura> izlaznaFakturas) {
        if (this.izlaznaFakturas != null) {
            this.izlaznaFakturas.forEach(i -> i.setKupac(null));
        }
        if (izlaznaFakturas != null) {
            izlaznaFakturas.forEach(i -> i.setKupac(this));
        }
        this.izlaznaFakturas = izlaznaFakturas;
    }

    public Kupac izlaznaFakturas(Set<IzlaznaFaktura> izlaznaFakturas) {
        this.setIzlaznaFakturas(izlaznaFakturas);
        return this;
    }

    public Kupac addIzlaznaFaktura(IzlaznaFaktura izlaznaFaktura) {
        this.izlaznaFakturas.add(izlaznaFaktura);
        izlaznaFaktura.setKupac(this);
        return this;
    }

    public Kupac removeIzlaznaFaktura(IzlaznaFaktura izlaznaFaktura) {
        this.izlaznaFakturas.remove(izlaznaFaktura);
        izlaznaFaktura.setKupac(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kupac)) {
            return false;
        }
        return id != null && id.equals(((Kupac) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Kupac{" +
            "id=" + getId() +
            ", pib='" + getPib() + "'" +
            ", mib='" + getMib() + "'" +
            ", naziv='" + getNaziv() + "'" +
            ", adresa='" + getAdresa() + "'" +
            ", telefon='" + getTelefon() + "'" +
            "}";
    }
}
