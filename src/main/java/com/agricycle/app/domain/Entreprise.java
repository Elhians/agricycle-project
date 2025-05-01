package com.agricycle.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Entreprise.
 */
@Entity
@Table(name = "entreprise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Entreprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_entreprise")
    private String nomEntreprise;

    @Column(name = "type_activite")
    private String typeActivite;

    @Column(name = "licence")
    private String licence;

    @Column(name = "adresse_physique")
    private String adressePhysique;

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

    public Entreprise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEntreprise() {
        return this.nomEntreprise;
    }

    public Entreprise nomEntreprise(String nomEntreprise) {
        this.setNomEntreprise(nomEntreprise);
        return this;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getTypeActivite() {
        return this.typeActivite;
    }

    public Entreprise typeActivite(String typeActivite) {
        this.setTypeActivite(typeActivite);
        return this;
    }

    public void setTypeActivite(String typeActivite) {
        this.typeActivite = typeActivite;
    }

    public String getLicence() {
        return this.licence;
    }

    public Entreprise licence(String licence) {
        this.setLicence(licence);
        return this;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getAdressePhysique() {
        return this.adressePhysique;
    }

    public Entreprise adressePhysique(String adressePhysique) {
        this.setAdressePhysique(adressePhysique);
        return this;
    }

    public void setAdressePhysique(String adressePhysique) {
        this.adressePhysique = adressePhysique;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Entreprise utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entreprise)) {
            return false;
        }
        return getId() != null && getId().equals(((Entreprise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Entreprise{" +
            "id=" + getId() +
            ", nomEntreprise='" + getNomEntreprise() + "'" +
            ", typeActivite='" + getTypeActivite() + "'" +
            ", licence='" + getLicence() + "'" +
            ", adressePhysique='" + getAdressePhysique() + "'" +
            "}";
    }
}
