package com.agricycle.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Commentaire} entity. This class is used
 * in {@link com.agricycle.app.web.rest.CommentaireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commentaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentaireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter contenu;

    private InstantFilter date;

    private LongFilter postId;

    private LongFilter auteurId;

    private Boolean distinct;

    public CommentaireCriteria() {}

    public CommentaireCriteria(CommentaireCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.contenu = other.optionalContenu().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.postId = other.optionalPostId().map(LongFilter::copy).orElse(null);
        this.auteurId = other.optionalAuteurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CommentaireCriteria copy() {
        return new CommentaireCriteria(this);
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

    public StringFilter getContenu() {
        return contenu;
    }

    public Optional<StringFilter> optionalContenu() {
        return Optional.ofNullable(contenu);
    }

    public StringFilter contenu() {
        if (contenu == null) {
            setContenu(new StringFilter());
        }
        return contenu;
    }

    public void setContenu(StringFilter contenu) {
        this.contenu = contenu;
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
        final CommentaireCriteria that = (CommentaireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contenu, that.contenu) &&
            Objects.equals(date, that.date) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(auteurId, that.auteurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contenu, date, postId, auteurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentaireCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalContenu().map(f -> "contenu=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalPostId().map(f -> "postId=" + f + ", ").orElse("") +
            optionalAuteurId().map(f -> "auteurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
