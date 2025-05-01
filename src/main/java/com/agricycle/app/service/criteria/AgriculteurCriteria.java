package com.agricycle.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Agriculteur} entity. This class is used
 * in {@link com.agricycle.app.web.rest.AgriculteurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agriculteurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculteurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter typeProduction;

    private IntegerFilter anneeExperience;

    private StringFilter localisation;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public AgriculteurCriteria() {}

    public AgriculteurCriteria(AgriculteurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.typeProduction = other.optionalTypeProduction().map(StringFilter::copy).orElse(null);
        this.anneeExperience = other.optionalAnneeExperience().map(IntegerFilter::copy).orElse(null);
        this.localisation = other.optionalLocalisation().map(StringFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgriculteurCriteria copy() {
        return new AgriculteurCriteria(this);
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

    public StringFilter getTypeProduction() {
        return typeProduction;
    }

    public Optional<StringFilter> optionalTypeProduction() {
        return Optional.ofNullable(typeProduction);
    }

    public StringFilter typeProduction() {
        if (typeProduction == null) {
            setTypeProduction(new StringFilter());
        }
        return typeProduction;
    }

    public void setTypeProduction(StringFilter typeProduction) {
        this.typeProduction = typeProduction;
    }

    public IntegerFilter getAnneeExperience() {
        return anneeExperience;
    }

    public Optional<IntegerFilter> optionalAnneeExperience() {
        return Optional.ofNullable(anneeExperience);
    }

    public IntegerFilter anneeExperience() {
        if (anneeExperience == null) {
            setAnneeExperience(new IntegerFilter());
        }
        return anneeExperience;
    }

    public void setAnneeExperience(IntegerFilter anneeExperience) {
        this.anneeExperience = anneeExperience;
    }

    public StringFilter getLocalisation() {
        return localisation;
    }

    public Optional<StringFilter> optionalLocalisation() {
        return Optional.ofNullable(localisation);
    }

    public StringFilter localisation() {
        if (localisation == null) {
            setLocalisation(new StringFilter());
        }
        return localisation;
    }

    public void setLocalisation(StringFilter localisation) {
        this.localisation = localisation;
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
        final AgriculteurCriteria that = (AgriculteurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeProduction, that.typeProduction) &&
            Objects.equals(anneeExperience, that.anneeExperience) &&
            Objects.equals(localisation, that.localisation) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeProduction, anneeExperience, localisation, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculteurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTypeProduction().map(f -> "typeProduction=" + f + ", ").orElse("") +
            optionalAnneeExperience().map(f -> "anneeExperience=" + f + ", ").orElse("") +
            optionalLocalisation().map(f -> "localisation=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
