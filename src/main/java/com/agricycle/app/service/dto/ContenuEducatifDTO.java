package com.agricycle.app.service.dto;

import com.agricycle.app.domain.enumeration.TypeMedia;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.ContenuEducatif} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContenuEducatifDTO implements Serializable {

    private Long id;

    @NotNull
    private String titre;

    private String description;

    @NotNull
    private TypeMedia typeMedia;

    @NotNull
    private String url;

    private Instant datePublication;

    private UtilisateurDTO auteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeMedia getTypeMedia() {
        return typeMedia;
    }

    public void setTypeMedia(TypeMedia typeMedia) {
        this.typeMedia = typeMedia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Instant getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Instant datePublication) {
        this.datePublication = datePublication;
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
        if (!(o instanceof ContenuEducatifDTO)) {
            return false;
        }

        ContenuEducatifDTO contenuEducatifDTO = (ContenuEducatifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contenuEducatifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContenuEducatifDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", typeMedia='" + getTypeMedia() + "'" +
            ", url='" + getUrl() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            ", auteur=" + getAuteur() +
            "}";
    }
}
