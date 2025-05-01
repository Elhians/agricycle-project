package com.agricycle.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Organisation} entity. This class is used
 * in {@link com.agricycle.app.web.rest.OrganisationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organisations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganisationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomOrganisation;

    private StringFilter siteWeb;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public OrganisationCriteria() {}

    public OrganisationCriteria(OrganisationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nomOrganisation = other.optionalNomOrganisation().map(StringFilter::copy).orElse(null);
        this.siteWeb = other.optionalSiteWeb().map(StringFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OrganisationCriteria copy() {
        return new OrganisationCriteria(this);
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

    public StringFilter getNomOrganisation() {
        return nomOrganisation;
    }

    public Optional<StringFilter> optionalNomOrganisation() {
        return Optional.ofNullable(nomOrganisation);
    }

    public StringFilter nomOrganisation() {
        if (nomOrganisation == null) {
            setNomOrganisation(new StringFilter());
        }
        return nomOrganisation;
    }

    public void setNomOrganisation(StringFilter nomOrganisation) {
        this.nomOrganisation = nomOrganisation;
    }

    public StringFilter getSiteWeb() {
        return siteWeb;
    }

    public Optional<StringFilter> optionalSiteWeb() {
        return Optional.ofNullable(siteWeb);
    }

    public StringFilter siteWeb() {
        if (siteWeb == null) {
            setSiteWeb(new StringFilter());
        }
        return siteWeb;
    }

    public void setSiteWeb(StringFilter siteWeb) {
        this.siteWeb = siteWeb;
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
        final OrganisationCriteria that = (OrganisationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomOrganisation, that.nomOrganisation) &&
            Objects.equals(siteWeb, that.siteWeb) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomOrganisation, siteWeb, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganisationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNomOrganisation().map(f -> "nomOrganisation=" + f + ", ").orElse("") +
            optionalSiteWeb().map(f -> "siteWeb=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
