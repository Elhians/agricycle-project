package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.UserRole;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Utilisateur} entity. This class is used
 * in {@link com.agricycle.app.web.rest.UtilisateurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /utilisateurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UserRole
     */
    public static class UserRoleFilter extends Filter<UserRole> {

        public UserRoleFilter() {}

        public UserRoleFilter(UserRoleFilter filter) {
            super(filter);
        }

        @Override
        public UserRoleFilter copy() {
            return new UserRoleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter phone;

    private UserRoleFilter role;

    private InstantFilter dateInscription;

    private LongFilter userId;

    private LongFilter agriculteurId;

    private LongFilter commercantId;

    private LongFilter transporteurId;

    private LongFilter consommateurId;

    private LongFilter organisationId;

    private LongFilter entrepriseId;

    private Boolean distinct;

    public UtilisateurCriteria() {}

    public UtilisateurCriteria(UtilisateurCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.role = other.optionalRole().map(UserRoleFilter::copy).orElse(null);
        this.dateInscription = other.optionalDateInscription().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.agriculteurId = other.optionalAgriculteurId().map(LongFilter::copy).orElse(null);
        this.commercantId = other.optionalCommercantId().map(LongFilter::copy).orElse(null);
        this.transporteurId = other.optionalTransporteurId().map(LongFilter::copy).orElse(null);
        this.consommateurId = other.optionalConsommateurId().map(LongFilter::copy).orElse(null);
        this.organisationId = other.optionalOrganisationId().map(LongFilter::copy).orElse(null);
        this.entrepriseId = other.optionalEntrepriseId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UtilisateurCriteria copy() {
        return new UtilisateurCriteria(this);
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

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public UserRoleFilter getRole() {
        return role;
    }

    public Optional<UserRoleFilter> optionalRole() {
        return Optional.ofNullable(role);
    }

    public UserRoleFilter role() {
        if (role == null) {
            setRole(new UserRoleFilter());
        }
        return role;
    }

    public void setRole(UserRoleFilter role) {
        this.role = role;
    }

    public InstantFilter getDateInscription() {
        return dateInscription;
    }

    public Optional<InstantFilter> optionalDateInscription() {
        return Optional.ofNullable(dateInscription);
    }

    public InstantFilter dateInscription() {
        if (dateInscription == null) {
            setDateInscription(new InstantFilter());
        }
        return dateInscription;
    }

    public void setDateInscription(InstantFilter dateInscription) {
        this.dateInscription = dateInscription;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getAgriculteurId() {
        return agriculteurId;
    }

    public Optional<LongFilter> optionalAgriculteurId() {
        return Optional.ofNullable(agriculteurId);
    }

    public LongFilter agriculteurId() {
        if (agriculteurId == null) {
            setAgriculteurId(new LongFilter());
        }
        return agriculteurId;
    }

    public void setAgriculteurId(LongFilter agriculteurId) {
        this.agriculteurId = agriculteurId;
    }

    public LongFilter getCommercantId() {
        return commercantId;
    }

    public Optional<LongFilter> optionalCommercantId() {
        return Optional.ofNullable(commercantId);
    }

    public LongFilter commercantId() {
        if (commercantId == null) {
            setCommercantId(new LongFilter());
        }
        return commercantId;
    }

    public void setCommercantId(LongFilter commercantId) {
        this.commercantId = commercantId;
    }

    public LongFilter getTransporteurId() {
        return transporteurId;
    }

    public Optional<LongFilter> optionalTransporteurId() {
        return Optional.ofNullable(transporteurId);
    }

    public LongFilter transporteurId() {
        if (transporteurId == null) {
            setTransporteurId(new LongFilter());
        }
        return transporteurId;
    }

    public void setTransporteurId(LongFilter transporteurId) {
        this.transporteurId = transporteurId;
    }

    public LongFilter getConsommateurId() {
        return consommateurId;
    }

    public Optional<LongFilter> optionalConsommateurId() {
        return Optional.ofNullable(consommateurId);
    }

    public LongFilter consommateurId() {
        if (consommateurId == null) {
            setConsommateurId(new LongFilter());
        }
        return consommateurId;
    }

    public void setConsommateurId(LongFilter consommateurId) {
        this.consommateurId = consommateurId;
    }

    public LongFilter getOrganisationId() {
        return organisationId;
    }

    public Optional<LongFilter> optionalOrganisationId() {
        return Optional.ofNullable(organisationId);
    }

    public LongFilter organisationId() {
        if (organisationId == null) {
            setOrganisationId(new LongFilter());
        }
        return organisationId;
    }

    public void setOrganisationId(LongFilter organisationId) {
        this.organisationId = organisationId;
    }

    public LongFilter getEntrepriseId() {
        return entrepriseId;
    }

    public Optional<LongFilter> optionalEntrepriseId() {
        return Optional.ofNullable(entrepriseId);
    }

    public LongFilter entrepriseId() {
        if (entrepriseId == null) {
            setEntrepriseId(new LongFilter());
        }
        return entrepriseId;
    }

    public void setEntrepriseId(LongFilter entrepriseId) {
        this.entrepriseId = entrepriseId;
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
        final UtilisateurCriteria that = (UtilisateurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(role, that.role) &&
            Objects.equals(dateInscription, that.dateInscription) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(agriculteurId, that.agriculteurId) &&
            Objects.equals(commercantId, that.commercantId) &&
            Objects.equals(transporteurId, that.transporteurId) &&
            Objects.equals(consommateurId, that.consommateurId) &&
            Objects.equals(organisationId, that.organisationId) &&
            Objects.equals(entrepriseId, that.entrepriseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            phone,
            role,
            dateInscription,
            userId,
            agriculteurId,
            commercantId,
            transporteurId,
            consommateurId,
            organisationId,
            entrepriseId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalRole().map(f -> "role=" + f + ", ").orElse("") +
            optionalDateInscription().map(f -> "dateInscription=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalAgriculteurId().map(f -> "agriculteurId=" + f + ", ").orElse("") +
            optionalCommercantId().map(f -> "commercantId=" + f + ", ").orElse("") +
            optionalTransporteurId().map(f -> "transporteurId=" + f + ", ").orElse("") +
            optionalConsommateurId().map(f -> "consommateurId=" + f + ", ").orElse("") +
            optionalOrganisationId().map(f -> "organisationId=" + f + ", ").orElse("") +
            optionalEntrepriseId().map(f -> "entrepriseId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
