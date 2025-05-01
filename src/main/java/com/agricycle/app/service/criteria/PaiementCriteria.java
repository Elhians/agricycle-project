package com.agricycle.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Paiement} entity. This class is used
 * in {@link com.agricycle.app.web.rest.PaiementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter moyenPaiement;

    private StringFilter preuve;

    private InstantFilter date;

    private LongFilter transactionId;

    private LongFilter acheteurId;

    private Boolean distinct;

    public PaiementCriteria() {}

    public PaiementCriteria(PaiementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.moyenPaiement = other.optionalMoyenPaiement().map(StringFilter::copy).orElse(null);
        this.preuve = other.optionalPreuve().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.transactionId = other.optionalTransactionId().map(LongFilter::copy).orElse(null);
        this.acheteurId = other.optionalAcheteurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaiementCriteria copy() {
        return new PaiementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMoyenPaiement() {
        return moyenPaiement;
    }

    public Optional<StringFilter> optionalMoyenPaiement() {
        return Optional.ofNullable(moyenPaiement);
    }

    public StringFilter moyenPaiement() {
        if (moyenPaiement == null) {
            setMoyenPaiement(new StringFilter());
        }
        return moyenPaiement;
    }

    public void setMoyenPaiement(StringFilter moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public StringFilter getPreuve() {
        return preuve;
    }

    public Optional<StringFilter> optionalPreuve() {
        return Optional.ofNullable(preuve);
    }

    public StringFilter preuve() {
        if (preuve == null) {
            setPreuve(new StringFilter());
        }
        return preuve;
    }

    public void setPreuve(StringFilter preuve) {
        this.preuve = preuve;
    }

    public InstantFilter getDate() {
        return date;
    }

    public Optional<InstantFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public InstantFilter date() {
        if (date == null) {
            setDate(new InstantFilter());
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public Optional<LongFilter> optionalTransactionId() {
        return Optional.ofNullable(transactionId);
    }

    public LongFilter transactionId() {
        if (transactionId == null) {
            setTransactionId(new LongFilter());
        }
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LongFilter getAcheteurId() {
        return acheteurId;
    }

    public Optional<LongFilter> optionalAcheteurId() {
        return Optional.ofNullable(acheteurId);
    }

    public LongFilter acheteurId() {
        if (acheteurId == null) {
            setAcheteurId(new LongFilter());
        }
        return acheteurId;
    }

    public void setAcheteurId(LongFilter acheteurId) {
        this.acheteurId = acheteurId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaiementCriteria that = (PaiementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(moyenPaiement, that.moyenPaiement) &&
            Objects.equals(preuve, that.preuve) &&
            Objects.equals(date, that.date) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(acheteurId, that.acheteurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moyenPaiement, preuve, date, transactionId, acheteurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMoyenPaiement().map(f -> "moyenPaiement=" + f + ", ").orElse("") +
            optionalPreuve().map(f -> "preuve=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalTransactionId().map(f -> "transactionId=" + f + ", ").orElse("") +
            optionalAcheteurId().map(f -> "acheteurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
