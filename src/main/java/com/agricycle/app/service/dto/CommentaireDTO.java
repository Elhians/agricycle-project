package com.agricycle.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Commentaire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentaireDTO implements Serializable {

    private Long id;

    @NotNull
    private String contenu;

    @NotNull
    private Instant date;

    private PostDTO post;

    private UtilisateurDTO auteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public UtilisateurDTO getAuteur() {
        return auteur;
    }

    public void setAuteur(UtilisateurDTO auteur) {
        this.auteur = auteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentaireDTO)) {
            return false;
        }

        CommentaireDTO commentaireDTO = (CommentaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentaireDTO{" +
            "id=" + getId() +
            ", contenu='" + getContenu() + "'" +
            ", date='" + getDate() + "'" +
            ", post=" + getPost() +
            ", auteur=" + getAuteur() +
            "}";
    }
}
