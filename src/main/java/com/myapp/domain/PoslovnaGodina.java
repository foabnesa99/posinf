package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PoslovnaGodina.
 */
@Entity
@Table(name = "poslovna_godina")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PoslovnaGodina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "godina")
    private Integer godina;

    @Column(name = "zakljucena")
    private Boolean zakljucena;

    @ManyToOne
    @JsonIgnoreProperties(value = { "izlaznaFakturas", "poslovnaGodinas" }, allowSetters = true)
    private Preduzece preduzece;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PoslovnaGodina id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGodina() {
        return this.godina;
    }

    public PoslovnaGodina godina(Integer godina) {
        this.setGodina(godina);
        return this;
    }

    public void setGodina(Integer godina) {
        this.godina = godina;
    }

    public Boolean getZakljucena() {
        return this.zakljucena;
    }

    public PoslovnaGodina zakljucena(Boolean zakljucena) {
        this.setZakljucena(zakljucena);
        return this;
    }

    public void setZakljucena(Boolean zakljucena) {
        this.zakljucena = zakljucena;
    }

    public Preduzece getPreduzece() {
        return this.preduzece;
    }

    public void setPreduzece(Preduzece preduzece) {
        this.preduzece = preduzece;
    }

    public PoslovnaGodina preduzece(Preduzece preduzece) {
        this.setPreduzece(preduzece);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoslovnaGodina)) {
            return false;
        }
        return id != null && id.equals(((PoslovnaGodina) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoslovnaGodina{" +
            "id=" + getId() +
            ", godina=" + getGodina() +
            ", zakljucena='" + getZakljucena() + "'" +
            "}";
    }
}
