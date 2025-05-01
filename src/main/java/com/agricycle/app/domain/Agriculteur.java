package com.agricycle.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Agriculteur.
 */
@Entity
@Table(name = "agriculteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agriculteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type_production")
    private String typeProduction;

    @Column(name = "annee_experience")
    private Integer anneeExperience;

    @Column(name = "localisation")
    private String localisation;

    @JsonIgnoreProperties(
        value = { "agriculteur", "commercant", "transporteur", "consommateur", "organisation", "entreprise" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agriculteur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeProduction() {
        return this.typeProduction;
    }

    public Agriculteur typeProduction(String typeProduction) {
        this.setTypeProduction(typeProduction);
        return this;
    }

    public void setTypeProduction(String typeProduction) {
        this.typeProduction = typeProduction;
    }

    public Integer getAnneeExperience() {
        return this.anneeExperience;
    }

    public Agriculteur anneeExperience(Integer anneeExperience) {
        this.setAnneeExperience(anneeExperience);
        return this;
    }

    public void setAnneeExperience(Integer anneeExperience) {
        this.anneeExperience = anneeExperience;
    }

    public String getLocalisation() {
        return this.localisation;
    }

    public Agriculteur localisation(String localisation) {
        this.setLocalisation(localisation);
        return this;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Agriculteur utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agriculteur)) {
            return false;
        }
        return getId() != null && getId().equals(((Agriculteur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agriculteur{" +
            "id=" + getId() +
            ", typeProduction='" + getTypeProduction() + "'" +
            ", anneeExperience=" + getAnneeExperience() +
            ", localisation='" + getLocalisation() + "'" +
            "}";
    }
}
