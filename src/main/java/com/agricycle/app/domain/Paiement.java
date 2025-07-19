package com.agricycle.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Paiement.
 */
@Entity
@Table(name = "paiement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "moyen_paiement", nullable = false)
    private String moyenPaiement;

    @Column(name = "preuve")
    private String preuve;

    @Column(name = "date")
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "produit", "acheteur" }, allowSetters = true)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "agriculteur", "commercant", "transporteur", "consommateur", "organisation", "entreprise" },
        allowSetters = true
    )
    private Utilisateur acheteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paiement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoyenPaiement() {
        return this.moyenPaiement;
    }

    public Paiement moyenPaiement(String moyenPaiement) {
        this.setMoyenPaiement(moyenPaiement);
        return this;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public String getPreuve() {
        return this.preuve;
    }

    public Paiement preuve(String preuve) {
        this.setPreuve(preuve);
        return this;
    }

    public void setPreuve(String preuve) {
        this.preuve = preuve;
    }

    public Instant getDate() {
        return this.date;
    }

    public Paiement date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Paiement transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    public Utilisateur getAcheteur() {
        return this.acheteur;
    }

    public void setAcheteur(Utilisateur utilisateur) {
        this.acheteur = utilisateur;
    }

    public Paiement acheteur(Utilisateur utilisateur) {
        this.setAcheteur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paiement)) {
            return false;
        }
        return getId() != null && getId().equals(((Paiement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paiement{" +
            "id=" + getId() +
            ", moyenPaiement='" + getMoyenPaiement() + "'" +
            ", preuve='" + getPreuve() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
