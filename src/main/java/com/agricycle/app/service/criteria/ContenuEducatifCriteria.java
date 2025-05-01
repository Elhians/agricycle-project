package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.TypeMedia;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.ContenuEducatif} entity. This class is used
 * in {@link com.agricycle.app.web.rest.ContenuEducatifResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contenu-educatifs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContenuEducatifCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeMedia
     */
    public static class TypeMediaFilter extends Filter<TypeMedia> {

        public TypeMediaFilter() {}

        public TypeMediaFilter(TypeMediaFilter filter) {
            super(filter);
        }

        @Override
        public TypeMediaFilter copy() {
            return new TypeMediaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titre;

    private StringFilter description;

    private TypeMediaFilter typeMedia;

    private StringFilter url;

    private InstantFilter datePublication;

    private LongFilter auteurId;

    private Boolean distinct;

    public ContenuEducatifCriteria() {}

    public ContenuEducatifCriteria(ContenuEducatifCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.typeMedia = other.optionalTypeMedia().map(TypeMediaFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.datePublication = other.optionalDatePublication().map(InstantFilter::copy).orElse(null);
        this.auteurId = other.optionalAuteurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContenuEducatifCriteria copy() {
        return new ContenuEducatifCriteria(this);
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

    public StringFilter getTitre() {
        return titre;
    }

    public Optional<StringFilter> optionalTitre() {
        return Optional.ofNullable(titre);
    }

    public StringFilter titre() {
        if (titre == null) {
            setTitre(new StringFilter());
        }
        return titre;
    }

    public void setTitre(StringFilter titre) {
        this.titre = titre;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public TypeMediaFilter getTypeMedia() {
        return typeMedia;
    }

    public Optional<TypeMediaFilter> optionalTypeMedia() {
        return Optional.ofNullable(typeMedia);
    }

    public TypeMediaFilter typeMedia() {
        if (typeMedia == null) {
            setTypeMedia(new TypeMediaFilter());
        }
        return typeMedia;
    }

    public void setTypeMedia(TypeMediaFilter typeMedia) {
        this.typeMedia = typeMedia;
    }

    public StringFilter getUrl() {
        return url;
    }

    public Optional<StringFilter> optionalUrl() {
        return Optional.ofNullable(url);
    }

    public StringFilter url() {
        if (url == null) {
            setUrl(new StringFilter());
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public InstantFilter getDatePublication() {
        return datePublication;
    }

    public Optional<InstantFilter> optionalDatePublication() {
        return Optional.ofNullable(datePublication);
    }

    public InstantFilter datePublication() {
        if (datePublication == null) {
            setDatePublication(new InstantFilter());
        }
        return datePublication;
    }

    public void setDatePublication(InstantFilter datePublication) {
        this.datePublication = datePublication;
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
        final ContenuEducatifCriteria that = (ContenuEducatifCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(description, that.description) &&
            Objects.equals(typeMedia, that.typeMedia) &&
            Objects.equals(url, that.url) &&
            Objects.equals(datePublication, that.datePublication) &&
            Objects.equals(auteurId, that.auteurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, description, typeMedia, url, datePublication, auteurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContenuEducatifCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalTypeMedia().map(f -> "typeMedia=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalDatePublication().map(f -> "datePublication=" + f + ", ").orElse("") +
            optionalAuteurId().map(f -> "auteurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
