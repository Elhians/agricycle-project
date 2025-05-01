package com.agricycle.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.QRCode} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QRCodeDTO implements Serializable {

    private Long id;

    @NotNull
    private String valeur;

    private Instant dateExpiration;

    private TransactionDTO transaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Instant getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Instant dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public TransactionDTO getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionDTO transaction) {
        this.transaction = transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QRCodeDTO)) {
            return false;
        }

        QRCodeDTO qRCodeDTO = (QRCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, qRCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QRCodeDTO{" +
            "id=" + getId() +
            ", valeur='" + getValeur() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", transaction=" + getTransaction() +
            "}";
    }
}
