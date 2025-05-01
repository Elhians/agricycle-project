package com.agricycle.app.domain;

import com.agricycle.app.domain.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "email")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "date_inscription")
    private Instant dateInscription;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Agriculteur agriculteur;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Commercant commercant;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Transporteur transporteur;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Consommateur consommateur;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Organisation organisation;

    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    private Entreprise entreprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public Utilisateur phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public Utilisateur passwordHash(String passwordHash) {
        this.setPasswordHash(passwordHash);
        return this;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return this.role;
    }

    public Utilisateur role(UserRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Instant getDateInscription() {
        return this.dateInscription;
    }

    public Utilisateur dateInscription(Instant dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Agriculteur getAgriculteur() {
        return this.agriculteur;
    }

    public void setAgriculteur(Agriculteur agriculteur) {
        if (this.agriculteur != null) {
            this.agriculteur.setUtilisateur(null);
        }
        if (agriculteur != null) {
            agriculteur.setUtilisateur(this);
        }
        this.agriculteur = agriculteur;
    }

    public Utilisateur agriculteur(Agriculteur agriculteur) {
        this.setAgriculteur(agriculteur);
        return this;
    }

    public Commercant getCommercant() {
        return this.commercant;
    }

    public void setCommercant(Commercant commercant) {
        if (this.commercant != null) {
            this.commercant.setUtilisateur(null);
        }
        if (commercant != null) {
            commercant.setUtilisateur(this);
        }
        this.commercant = commercant;
    }

    public Utilisateur commercant(Commercant commercant) {
        this.setCommercant(commercant);
        return this;
    }

    public Transporteur getTransporteur() {
        return this.transporteur;
    }

    public void setTransporteur(Transporteur transporteur) {
        if (this.transporteur != null) {
            this.transporteur.setUtilisateur(null);
        }
        if (transporteur != null) {
            transporteur.setUtilisateur(this);
        }
        this.transporteur = transporteur;
    }

    public Utilisateur transporteur(Transporteur transporteur) {
        this.setTransporteur(transporteur);
        return this;
    }

    public Consommateur getConsommateur() {
        return this.consommateur;
    }

    public void setConsommateur(Consommateur consommateur) {
        if (this.consommateur != null) {
            this.consommateur.setUtilisateur(null);
        }
        if (consommateur != null) {
            consommateur.setUtilisateur(this);
        }
        this.consommateur = consommateur;
    }

    public Utilisateur consommateur(Consommateur consommateur) {
        this.setConsommateur(consommateur);
        return this;
    }

    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        if (this.organisation != null) {
            this.organisation.setUtilisateur(null);
        }
        if (organisation != null) {
            organisation.setUtilisateur(this);
        }
        this.organisation = organisation;
    }

    public Utilisateur organisation(Organisation organisation) {
        this.setOrganisation(organisation);
        return this;
    }

    public Entreprise getEntreprise() {
        return this.entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        if (this.entreprise != null) {
            this.entreprise.setUtilisateur(null);
        }
        if (entreprise != null) {
            entreprise.setUtilisateur(this);
        }
        this.entreprise = entreprise;
    }

    public Utilisateur entreprise(Entreprise entreprise) {
        this.setEntreprise(entreprise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", email='" + getEmail() + "'" +
            ", role='" + getRole() + "'" +
            ", dateInscription='" + getDateInscription() + "'" +
            "}";
    }
}
