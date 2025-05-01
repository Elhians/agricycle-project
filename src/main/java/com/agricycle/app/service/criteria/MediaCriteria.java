package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.TypeMedia;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Media} entity. This class is used
 * in {@link com.agricycle.app.web.rest.MediaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /media?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MediaCriteria implements Serializable, Criteria {

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

    private StringFilter url;

    private TypeMediaFilter typeMedia;

    private LongFilter postId;

    private Boolean distinct;

    public MediaCriteria() {}

    public MediaCriteria(MediaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.typeMedia = other.optionalTypeMedia().map(TypeMediaFilter::copy).orElse(null);
        this.postId = other.optionalPostId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MediaCriteria copy() {
        return new MediaCriteria(this);
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
        final MediaCriteria that = (MediaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(url, that.url) &&
            Objects.equals(typeMedia, that.typeMedia) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, typeMedia, postId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalTypeMedia().map(f -> "typeMedia=" + f + ", ").orElse("") +
            optionalPostId().map(f -> "postId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
