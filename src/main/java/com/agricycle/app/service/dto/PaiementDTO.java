package com.agricycle.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Paiement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementDTO implements Serializable {

    private Long id;

    @NotNull
    private String moyenPaiement;

    private String preuve;

    private Instant date;

    private TransactionDTO transaction;

    private UtilisateurDTO acheteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public String getPreuve() {
        return preuve;
    }

    public void setPreuve(String preuve) {
        this.preuve = preuve;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public TransactionDTO getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionDTO transaction) {
        this.transaction = transaction;
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
        if (!(o instanceof PaiementDTO)) {
            return false;
        }

        PaiementDTO paiementDTO = (PaiementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementDTO{" +
            "id=" + getId() +
            ", moyenPaiement='" + getMoyenPaiement() + "'" +
            ", preuve='" + getPreuve() + "'" +
            ", date='" + getDate() + "'" +
            ", transaction=" + getTransaction() +
            ", acheteur=" + getAcheteur() +
            "}";
    }
}
