package com.agricycle.app.service.dto;

import com.agricycle.app.domain.enumeration.StatutPaiement;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private StatutPaiement statut;

    @NotNull
    private Double montant;

    private ProduitDTO produit;

    private UtilisateurDTO acheteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public ProduitDTO getProduit() {
        return produit;
    }

    public void setProduit(ProduitDTO produit) {
        this.produit = produit;
    }

    public UtilisateurDTO getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(UtilisateurDTO acheteur) {
        this.acheteur = acheteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", statut='" + getStatut() + "'" +
            ", montant=" + getMontant() +
            ", produit=" + getProduit() +
            ", acheteur=" + getAcheteur() +
            "}";
    }
}
