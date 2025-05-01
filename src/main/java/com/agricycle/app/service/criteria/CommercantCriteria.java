package com.agricycle.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Commercant} entity. This class is used
 * in {@link com.agricycle.app.web.rest.CommercantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commercants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommercantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter adresseCommerce;

    private StringFilter moyenPaiement;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public CommercantCriteria() {}

    public CommercantCriteria(CommercantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.adresseCommerce = other.optionalAdresseCommerce().map(StringFilter::copy).orElse(null);
        this.moyenPaiement = other.optionalMoyenPaiement().map(StringFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CommercantCriteria copy() {
        return new CommercantCriteria(this);
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

    public StringFilter getAdresseCommerce() {
        return adresseCommerce;
    }

    public Optional<StringFilter> optionalAdresseCommerce() {
        return Optional.ofNullable(adresseCommerce);
    }

    public StringFilter adresseCommerce() {
        if (adresseCommerce == null) {
            setAdresseCommerce(new StringFilter());
        }
        return adresseCommerce;
    }

    public void setAdresseCommerce(StringFilter adresseCommerce) {
        this.adresseCommerce = adresseCommerce;
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

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public Optional<LongFilter> optionalUtilisateurId() {
        return Optional.ofNullable(utilisateurId);
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            setUtilisateurId(new LongFilter());
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
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
        final CommercantCriteria that = (CommercantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(adresseCommerce, that.adresseCommerce) &&
            Objects.equals(moyenPaiement, that.moyenPaiement) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, adresseCommerce, moyenPaiement, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommercantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAdresseCommerce().map(f -> "adresseCommerce=" + f + ", ").orElse("") +
            optionalMoyenPaiement().map(f -> "moyenPaiement=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
