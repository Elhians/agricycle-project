package com.agricycle.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Entreprise} entity. This class is used
 * in {@link com.agricycle.app.web.rest.EntrepriseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /entreprises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntrepriseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomEntreprise;

    private StringFilter typeActivite;

    private StringFilter licence;

    private StringFilter adressePhysique;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public EntrepriseCriteria() {}

    public EntrepriseCriteria(EntrepriseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nomEntreprise = other.optionalNomEntreprise().map(StringFilter::copy).orElse(null);
        this.typeActivite = other.optionalTypeActivite().map(StringFilter::copy).orElse(null);
        this.licence = other.optionalLicence().map(StringFilter::copy).orElse(null);
        this.adressePhysique = other.optionalAdressePhysique().map(StringFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EntrepriseCriteria copy() {
        return new EntrepriseCriteria(this);
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

    public StringFilter getNomEntreprise() {
        return nomEntreprise;
    }

    public Optional<StringFilter> optionalNomEntreprise() {
        return Optional.ofNullable(nomEntreprise);
    }

    public StringFilter nomEntreprise() {
        if (nomEntreprise == null) {
            setNomEntreprise(new StringFilter());
        }
        return nomEntreprise;
    }

    public void setNomEntreprise(StringFilter nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public StringFilter getTypeActivite() {
        return typeActivite;
    }

    public Optional<StringFilter> optionalTypeActivite() {
        return Optional.ofNullable(typeActivite);
    }

    public StringFilter typeActivite() {
        if (typeActivite == null) {
            setTypeActivite(new StringFilter());
        }
        return typeActivite;
    }

    public void setTypeActivite(StringFilter typeActivite) {
        this.typeActivite = typeActivite;
    }

    public StringFilter getLicence() {
        return licence;
    }

    public Optional<StringFilter> optionalLicence() {
        return Optional.ofNullable(licence);
    }

    public StringFilter licence() {
        if (licence == null) {
            setLicence(new StringFilter());
        }
        return licence;
    }

    public void setLicence(StringFilter licence) {
        this.licence = licence;
    }

    public StringFilter getAdressePhysique() {
        return adressePhysique;
    }

    public Optional<StringFilter> optionalAdressePhysique() {
        return Optional.ofNullable(adressePhysique);
    }

    public StringFilter adressePhysique() {
        if (adressePhysique == null) {
            setAdressePhysique(new StringFilter());
        }
        return adressePhysique;
    }

    public void setAdressePhysique(StringFilter adressePhysique) {
        this.adressePhysique = adressePhysique;
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
        final EntrepriseCriteria that = (EntrepriseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomEntreprise, that.nomEntreprise) &&
            Objects.equals(typeActivite, that.typeActivite) &&
            Objects.equals(licence, that.licence) &&
            Objects.equals(adressePhysique, that.adressePhysique) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomEntreprise, typeActivite, licence, adressePhysique, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntrepriseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNomEntreprise().map(f -> "nomEntreprise=" + f + ", ").orElse("") +
            optionalTypeActivite().map(f -> "typeActivite=" + f + ", ").orElse("") +
            optionalLicence().map(f -> "licence=" + f + ", ").orElse("") +
            optionalAdressePhysique().map(f -> "adressePhysique=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
