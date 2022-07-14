package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.domain.enumeration.StatusFakture;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IzlaznaFaktura.
 */
@Entity
@Table(name = "izlazna_faktura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IzlaznaFaktura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "broj_fakture")
    private String brojFakture;

    @Column(name = "datum_izdavanja")
    private LocalDate datumIzdavanja;

    @Column(name = "ukupno_pdv")
    private Double ukupnoPdv;

    @Column(name = "datum_valute")
    private LocalDate datumValute;

    @Column(name = "ukupna_osnovica")
    private Double ukupnaOsnovica;

    private Double ukupanPdv;

    @Column(name = "ukupan_iznos")
    private Double ukupanIznos;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusFakture status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "izlaznaFakturas" }, allowSetters = true)
    private Kupac kupac;

    @ManyToOne
    @JsonIgnoreProperties(value = { "izlaznaFakturas", "poslovnaGodinas" }, allowSetters = true)
    private Preduzece preduzece;

    @OneToMany(mappedBy = "izlaznaFaktura")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "izlaznaFaktura", "robaIliUsluga" }, allowSetters = true)
    private Set<StavkaFakture> stavkaFaktures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IzlaznaFaktura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrojFakture() {
        return this.brojFakture;
    }

    public IzlaznaFaktura brojFakture(String brojFakture) {
        this.setBrojFakture(brojFakture);
        return this;
    }

    public void setBrojFakture(String brojFakture) {
        this.brojFakture = brojFakture;
    }

    public LocalDate getDatumIzdavanja() {
        return this.datumIzdavanja;
    }

    public IzlaznaFaktura datumIzdavanja(LocalDate datumIzdavanja) {
        this.setDatumIzdavanja(datumIzdavanja);
        return this;
    }

    public void setDatumIzdavanja(LocalDate datumIzdavanja) {
        this.datumIzdavanja = datumIzdavanja;
    }

    public LocalDate getDatumValute() {
        return this.datumValute;
    }

    public IzlaznaFaktura datumValute(LocalDate datumValute) {
        this.setDatumValute(datumValute);
        return this;
    }

    public void setDatumValute(LocalDate datumValute) {
        this.datumValute = datumValute;
    }

    public Double getUkupnaOsnovica() {
        return this.ukupnaOsnovica;
    }

    public IzlaznaFaktura ukupnaOsnovica(Double ukupnaOsnovica) {
        this.setUkupnaOsnovica(ukupnaOsnovica);
        return this;
    }

    public void setUkupnaOsnovica(Double ukupnaOsnovica) {
        this.ukupnaOsnovica = ukupnaOsnovica;
    }

    public Double getUkupanPdv() {
        return this.ukupanPdv;
    }

    public IzlaznaFaktura ukupanRabat(Double ukupanRabat) {
        this.setUkupanPdv(ukupanRabat);
        return this;
    }

    public void setUkupanPdv(Double ukupanRabat) {
        this.ukupanPdv = ukupanRabat;
    }

    public Double getUkupanIznos() {
        return this.ukupanIznos;
    }

    public IzlaznaFaktura ukupanIznos(Double ukupanIznos) {
        this.setUkupanIznos(ukupanIznos);
        return this;
    }

    public void setUkupanIznos(Double ukupanIznos) {
        this.ukupanIznos = ukupanIznos;
    }

    public StatusFakture getStatus() {
        return this.status;
    }

    public IzlaznaFaktura status(StatusFakture status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusFakture status) {
        this.status = status;
    }

    public Kupac getKupac() {
        return this.kupac;
    }

    public void setKupac(Kupac kupac) {
        this.kupac = kupac;
    }

    public IzlaznaFaktura kupac(Kupac kupac) {
        this.setKupac(kupac);
        return this;
    }

    public Preduzece getPreduzece() {
        return this.preduzece;
    }

    public void setPreduzece(Preduzece preduzece) {
        this.preduzece = preduzece;
    }

    public IzlaznaFaktura preduzece(Preduzece preduzece) {
        this.setPreduzece(preduzece);
        return this;
    }

    public Set<StavkaFakture> getStavkaFaktures() {
        return this.stavkaFaktures;
    }

    public void setStavkaFaktures(Set<StavkaFakture> stavkaFaktures) {
        if (this.stavkaFaktures != null) {
            this.stavkaFaktures.forEach(i -> i.setIzlaznaFaktura(null));
        }
        if (stavkaFaktures != null) {
            stavkaFaktures.forEach(i -> i.setIzlaznaFaktura(this));
        }
        this.stavkaFaktures = stavkaFaktures;
    }

    public IzlaznaFaktura stavkaFaktures(Set<StavkaFakture> stavkaFaktures) {
        this.setStavkaFaktures(stavkaFaktures);
        return this;
    }

    public IzlaznaFaktura addStavkaFakture(StavkaFakture stavkaFakture) {
        this.stavkaFaktures.add(stavkaFakture);
        stavkaFakture.setIzlaznaFaktura(this);
        return this;
    }

    public IzlaznaFaktura removeStavkaFakture(StavkaFakture stavkaFakture) {
        this.stavkaFaktures.remove(stavkaFakture);
        stavkaFakture.setIzlaznaFaktura(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IzlaznaFaktura)) {
            return false;
        }
        return id != null && id.equals(((IzlaznaFaktura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IzlaznaFaktura{" +
            "id=" + getId() +
            ", brojFakture='" + getBrojFakture() + "'" +
            ", datumIzdavanja='" + getDatumIzdavanja() + "'" +
            ", datumValute='" + getDatumValute() + "'" +
            ", ukupnaOsnovica=" + getUkupnaOsnovica() +
            ", ukupanRabat=" + getUkupanPdv() +
            ", ukupanIznos=" + getUkupanIznos() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
