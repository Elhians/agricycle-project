package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.TypeVehicule;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Transporteur} entity. This class is used
 * in {@link com.agricycle.app.web.rest.TransporteurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transporteurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransporteurCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeVehicule
     */
    public static class TypeVehiculeFilter extends Filter<TypeVehicule> {

        public TypeVehiculeFilter() {}

        public TypeVehiculeFilter(TypeVehiculeFilter filter) {
            super(filter);
        }

        @Override
        public TypeVehiculeFilter copy() {
            return new TypeVehiculeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter zoneCouverture;

    private TypeVehiculeFilter typeVehicule;

    private FloatFilter capacite;

    private LongFilter utilisateurId;

    private Boolean distinct;

    public TransporteurCriteria() {}

    public TransporteurCriteria(TransporteurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.zoneCouverture = other.optionalZoneCouverture().map(StringFilter::copy).orElse(null);
        this.typeVehicule = other.optionalTypeVehicule().map(TypeVehiculeFilter::copy).orElse(null);
        this.capacite = other.optionalCapacite().map(FloatFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransporteurCriteria copy() {
        return new TransporteurCriteria(this);
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

    public StringFilter getZoneCouverture() {
        return zoneCouverture;
    }

    public Optional<StringFilter> optionalZoneCouverture() {
        return Optional.ofNullable(zoneCouverture);
    }

    public StringFilter zoneCouverture() {
        if (zoneCouverture == null) {
            setZoneCouverture(new StringFilter());
        }
        return zoneCouverture;
    }

    public void setZoneCouverture(StringFilter zoneCouverture) {
        this.zoneCouverture = zoneCouverture;
    }

    public TypeVehiculeFilter getTypeVehicule() {
        return typeVehicule;
    }

    public Optional<TypeVehiculeFilter> optionalTypeVehicule() {
        return Optional.ofNullable(typeVehicule);
    }

    public TypeVehiculeFilter typeVehicule() {
        if (typeVehicule == null) {
            setTypeVehicule(new TypeVehiculeFilter());
        }
        return typeVehicule;
    }

    public void setTypeVehicule(TypeVehiculeFilter typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public FloatFilter getCapacite() {
        return capacite;
    }

    public Optional<FloatFilter> optionalCapacite() {
        return Optional.ofNullable(capacite);
    }

    public FloatFilter capacite() {
        if (capacite == null) {
            setCapacite(new FloatFilter());
        }
        return capacite;
    }

    public void setCapacite(FloatFilter capacite) {
        this.capacite = capacite;
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
        final TransporteurCriteria that = (TransporteurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(zoneCouverture, that.zoneCouverture) &&
            Objects.equals(typeVehicule, that.typeVehicule) &&
            Objects.equals(capacite, that.capacite) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zoneCouverture, typeVehicule, capacite, utilisateurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransporteurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalZoneCouverture().map(f -> "zoneCouverture=" + f + ", ").orElse("") +
            optionalTypeVehicule().map(f -> "typeVehicule=" + f + ", ").orElse("") +
            optionalCapacite().map(f -> "capacite=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
