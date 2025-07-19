package com.agricycle.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Commercant.
 */
@Entity
@Table(name = "commercant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Commercant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "adresse_commerce")
    private String adresseCommerce;

    @Column(name = "moyen_paiement")
    private String moyenPaiement;

    @JsonIgnoreProperties(
        value = { "user", "agriculteur", "commercant", "transporteur", "consommateur", "organisation", "entreprise" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commercant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresseCommerce() {
        return this.adresseCommerce;
    }

    public Commercant adresseCommerce(String adresseCommerce) {
        this.setAdresseCommerce(adresseCommerce);
        return this;
    }

    public void setAdresseCommerce(String adresseCommerce) {
        this.adresseCommerce = adresseCommerce;
    }

    public String getMoyenPaiement() {
        return this.moyenPaiement;
    }

    public Commercant moyenPaiement(String moyenPaiement) {
        this.setMoyenPaiement(moyenPaiement);
        return this;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Commercant utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commercant)) {
            return false;
        }
        return getId() != null && getId().equals(((Commercant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commercant{" +
            "id=" + getId() +
            ", adresseCommerce='" + getAdresseCommerce() + "'" +
            ", moyenPaiement='" + getMoyenPaiement() + "'" +
            "}";
    }
}
