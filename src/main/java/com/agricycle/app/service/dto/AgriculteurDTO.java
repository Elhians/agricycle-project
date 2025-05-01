package com.agricycle.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Agriculteur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgriculteurDTO implements Serializable {

    private Long id;

    private String typeProduction;

    private Integer anneeExperience;

    private String localisation;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeProduction() {
        return typeProduction;
    }

    public void setTypeProduction(String typeProduction) {
        this.typeProduction = typeProduction;
    }

    public Integer getAnneeExperience() {
        return anneeExperience;
    }

    public void setAnneeExperience(Integer anneeExperience) {
        this.anneeExperience = anneeExperience;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgriculteurDTO)) {
            return false;
        }

        AgriculteurDTO agriculteurDTO = (AgriculteurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agriculteurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgriculteurDTO{" +
            "id=" + getId() +
            ", typeProduction='" + getTypeProduction() + "'" +
            ", anneeExperience=" + getAnneeExperience() +
            ", localisation='" + getLocalisation() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
