package com.agricycle.app.service.dto;

import com.agricycle.app.domain.enumeration.Cible;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Signalement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignalementDTO implements Serializable {

    private Long id;

    @NotNull
    private String raison;

    @NotNull
    private Cible cible;

    private Instant date;

    private UtilisateurDTO auteur;

    private PostDTO ciblePost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public Cible getCible() {
        return cible;
    }

    public void setCible(Cible cible) {
        this.cible = cible;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public UtilisateurDTO getAuteur() {
        return auteur;
    }

    public void setAuteur(UtilisateurDTO auteur) {
        this.auteur = auteur;
    }

    public PostDTO getCiblePost() {
        return ciblePost;
    }

    public void setCiblePost(PostDTO ciblePost) {
        this.ciblePost = ciblePost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignalementDTO)) {
            return false;
        }

        SignalementDTO signalementDTO = (SignalementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, signalementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignalementDTO{" +
            "id=" + getId() +
            ", raison='" + getRaison() + "'" +
            ", cible='" + getCible() + "'" +
            ", date='" + getDate() + "'" +
            ", auteur=" + getAuteur() +
            ", ciblePost=" + getCiblePost() +
            "}";
    }
}
