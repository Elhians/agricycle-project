package com.agricycle.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Consommateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsommateurDTO implements Serializable {

    private Long id;

    private String preferences;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
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
        if (!(o instanceof ConsommateurDTO)) {
            return false;
        }

        ConsommateurDTO consommateurDTO = (ConsommateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consommateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsommateurDTO{" +
            "id=" + getId() +
            ", preferences='" + getPreferences() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
