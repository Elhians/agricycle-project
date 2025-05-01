package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.Cible;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Signalement} entity. This class is used
 * in {@link com.agricycle.app.web.rest.SignalementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /signalements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignalementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Cible
     */
    public static class CibleFilter extends Filter<Cible> {

        public CibleFilter() {}

        public CibleFilter(CibleFilter filter) {
            super(filter);
        }

        @Override
        public CibleFilter copy() {
            return new CibleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter raison;

    private CibleFilter cible;

    private InstantFilter date;

    private LongFilter auteurId;

    private LongFilter ciblePostId;

    private Boolean distinct;

    public SignalementCriteria() {}

    public SignalementCriteria(SignalementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.raison = other.optionalRaison().map(StringFilter::copy).orElse(null);
        this.cible = other.optionalCible().map(CibleFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.auteurId = other.optionalAuteurId().map(LongFilter::copy).orElse(null);
        this.ciblePostId = other.optionalCiblePostId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SignalementCriteria copy() {
        return new SignalementCriteria(this);
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

    public StringFilter getRaison() {
        return raison;
    }

    public Optional<StringFilter> optionalRaison() {
        return Optional.ofNullable(raison);
    }

    public StringFilter raison() {
        if (raison == null) {
            setRaison(new StringFilter());
        }
        return raison;
    }

    public void setRaison(StringFilter raison) {
        this.raison = raison;
    }

    public CibleFilter getCible() {
        return cible;
    }

    public Optional<CibleFilter> optionalCible() {
        return Optional.ofNullable(cible);
    }

    public CibleFilter cible() {
        if (cible == null) {
            setCible(new CibleFilter());
        }
        return cible;
    }

    public void setCible(CibleFilter cible) {
        this.cible = cible;
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

    public LongFilter getAuteurId() {
        return auteurId;
    }

    public Optional<LongFilter> optionalAuteurId() {
        return Optional.ofNullable(auteurId);
    }

    public LongFilter auteurId() {
        if (auteurId == null) {
            setAuteurId(new LongFilter());
        }
        return auteurId;
    }

    public void setAuteurId(LongFilter auteurId) {
        this.auteurId = auteurId;
    }

    public LongFilter getCiblePostId() {
        return ciblePostId;
    }

    public Optional<LongFilter> optionalCiblePostId() {
        return Optional.ofNullable(ciblePostId);
    }

    public LongFilter ciblePostId() {
        if (ciblePostId == null) {
            setCiblePostId(new LongFilter());
        }
        return ciblePostId;
    }

    public void setCiblePostId(LongFilter ciblePostId) {
        this.ciblePostId = ciblePostId;
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
        final SignalementCriteria that = (SignalementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(raison, that.raison) &&
            Objects.equals(cible, that.cible) &&
            Objects.equals(date, that.date) &&
            Objects.equals(auteurId, that.auteurId) &&
            Objects.equals(ciblePostId, that.ciblePostId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, raison, cible, date, auteurId, ciblePostId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignalementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRaison().map(f -> "raison=" + f + ", ").orElse("") +
            optionalCible().map(f -> "cible=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalAuteurId().map(f -> "auteurId=" + f + ", ").orElse("") +
            optionalCiblePostId().map(f -> "ciblePostId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
