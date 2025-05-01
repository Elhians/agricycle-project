package com.agricycle.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.QRCode} entity. This class is used
 * in {@link com.agricycle.app.web.rest.QRCodeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /qr-codes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QRCodeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter valeur;

    private InstantFilter dateExpiration;

    private LongFilter transactionId;

    private Boolean distinct;

    public QRCodeCriteria() {}

    public QRCodeCriteria(QRCodeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.valeur = other.optionalValeur().map(StringFilter::copy).orElse(null);
        this.dateExpiration = other.optionalDateExpiration().map(InstantFilter::copy).orElse(null);
        this.transactionId = other.optionalTransactionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QRCodeCriteria copy() {
        return new QRCodeCriteria(this);
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

    public StringFilter getValeur() {
        return valeur;
    }

    public Optional<StringFilter> optionalValeur() {
        return Optional.ofNullable(valeur);
    }

    public StringFilter valeur() {
        if (valeur == null) {
            setValeur(new StringFilter());
        }
        return valeur;
    }

    public void setValeur(StringFilter valeur) {
        this.valeur = valeur;
    }

    public InstantFilter getDateExpiration() {
        return dateExpiration;
    }

    public Optional<InstantFilter> optionalDateExpiration() {
        return Optional.ofNullable(dateExpiration);
    }

    public InstantFilter dateExpiration() {
        if (dateExpiration == null) {
            setDateExpiration(new InstantFilter());
        }
        return dateExpiration;
    }

    public void setDateExpiration(InstantFilter dateExpiration) {
        this.dateExpiration = dateExpiration;
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
        final QRCodeCriteria that = (QRCodeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(valeur, that.valeur) &&
            Objects.equals(dateExpiration, that.dateExpiration) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valeur, dateExpiration, transactionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QRCodeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalValeur().map(f -> "valeur=" + f + ", ").orElse("") +
            optionalDateExpiration().map(f -> "dateExpiration=" + f + ", ").orElse("") +
            optionalTransactionId().map(f -> "transactionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
