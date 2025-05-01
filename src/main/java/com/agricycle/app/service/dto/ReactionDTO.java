package com.agricycle.app.service.dto;

import com.agricycle.app.domain.enumeration.Cible;
import com.agricycle.app.domain.enumeration.TypeReaction;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Reaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactionDTO implements Serializable {

    private Long id;

    @NotNull
    private TypeReaction type;

    private Instant date;

    @NotNull
    private Cible cible;

    private UtilisateurDTO utilisateur;

    private PostDTO post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeReaction getType() {
        return type;
    }

    public void setType(TypeReaction type) {
        this.type = type;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Cible getCible() {
        return cible;
    }

    public void setCible(Cible cible) {
        this.cible = cible;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReactionDTO)) {
            return false;
        }

        ReactionDTO reactionDTO = (ReactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", date='" + getDate() + "'" +
            ", cible='" + getCible() + "'" +
            ", utilisateur=" + getUtilisateur() +
            ", post=" + getPost() +
            "}";
    }
}
