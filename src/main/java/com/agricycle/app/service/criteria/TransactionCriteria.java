package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.StatutPaiement;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Transaction} entity. This class is used
 * in {@link com.agricycle.app.web.rest.TransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutPaiement
     */
    public static class StatutPaiementFilter extends Filter<StatutPaiement> {

        public StatutPaiementFilter() {}

        public StatutPaiementFilter(StatutPaiementFilter filter) {
            super(filter);
        }

        @Override
        public StatutPaiementFilter copy() {
            return new StatutPaiementFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter date;

    private StatutPaiementFilter statut;

    private DoubleFilter montant;

    private LongFilter produitId;

    private LongFilter acheteurId;

    private Boolean distinct;

    public TransactionCriteria() {}

    public TransactionCriteria(TransactionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutPaiementFilter::copy).orElse(null);
        this.montant = other.optionalMontant().map(DoubleFilter::copy).orElse(null);
        this.produitId = other.optionalProduitId().map(LongFilter::copy).orElse(null);
        this.acheteurId = other.optionalAcheteurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransactionCriteria copy() {
        return new TransactionCriteria(this);
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

    public StatutPaiementFilter getStatut() {
        return statut;
    }

    public Optional<StatutPaiementFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutPaiementFilter statut() {
        if (statut == null) {
            setStatut(new StatutPaiementFilter());
        }
        return statut;
    }

    public void setStatut(StatutPaiementFilter statut) {
        this.statut = statut;
    }

    public DoubleFilter getMontant() {
        return montant;
    }

    public Optional<DoubleFilter> optionalMontant() {
        return Optional.ofNullable(montant);
    }

    public DoubleFilter montant() {
        if (montant == null) {
            setMontant(new DoubleFilter());
        }
        return montant;
    }

    public void setMontant(DoubleFilter montant) {
        this.montant = montant;
    }

    public LongFilter getProduitId() {
        return produitId;
    }

    public Optional<LongFilter> optionalProduitId() {
        return Optional.ofNullable(produitId);
    }

    public LongFilter produitId() {
        if (produitId == null) {
            setProduitId(new LongFilter());
        }
        return produitId;
    }

    public void setProduitId(LongFilter produitId) {
        this.produitId = produitId;
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
        final TransactionCriteria that = (TransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(produitId, that.produitId) &&
            Objects.equals(acheteurId, that.acheteurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, statut, montant, produitId, acheteurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalMontant().map(f -> "montant=" + f + ", ").orElse("") +
            optionalProduitId().map(f -> "produitId=" + f + ", ").orElse("") +
            optionalAcheteurId().map(f -> "acheteurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
