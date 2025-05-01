package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.Cible;
import com.agricycle.app.domain.enumeration.TypeReaction;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Reaction} entity. This class is used
 * in {@link com.agricycle.app.web.rest.ReactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeReaction
     */
    public static class TypeReactionFilter extends Filter<TypeReaction> {

        public TypeReactionFilter() {}

        public TypeReactionFilter(TypeReactionFilter filter) {
            super(filter);
        }

        @Override
        public TypeReactionFilter copy() {
            return new TypeReactionFilter(this);
        }
    }

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

    private TypeReactionFilter type;

    private InstantFilter date;

    private CibleFilter cible;

    private LongFilter utilisateurId;

    private LongFilter postId;

    private Boolean distinct;

    public ReactionCriteria() {}

    public ReactionCriteria(ReactionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeReactionFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.cible = other.optionalCible().map(CibleFilter::copy).orElse(null);
        this.utilisateurId = other.optionalUtilisateurId().map(LongFilter::copy).orElse(null);
        this.postId = other.optionalPostId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReactionCriteria copy() {
        return new ReactionCriteria(this);
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

    public TypeReactionFilter getType() {
        return type;
    }

    public Optional<TypeReactionFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TypeReactionFilter type() {
        if (type == null) {
            setType(new TypeReactionFilter());
        }
        return type;
    }

    public void setType(TypeReactionFilter type) {
        this.type = type;
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

    public LongFilter getPostId() {
        return postId;
    }

    public Optional<LongFilter> optionalPostId() {
        return Optional.ofNullable(postId);
    }

    public LongFilter postId() {
        if (postId == null) {
            setPostId(new LongFilter());
        }
        return postId;
    }

    public void setPostId(LongFilter postId) {
        this.postId = postId;
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
        final ReactionCriteria that = (ReactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(date, that.date) &&
            Objects.equals(cible, that.cible) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, date, cible, utilisateurId, postId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalCible().map(f -> "cible=" + f + ", ").orElse("") +
            optionalUtilisateurId().map(f -> "utilisateurId=" + f + ", ").orElse("") +
            optionalPostId().map(f -> "postId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
