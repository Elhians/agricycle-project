package com.agricycle.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.ChatbotSession} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatbotSessionDTO implements Serializable {

    private Long id;

    private Instant dateDebut;

    private Instant dateFin;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
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
        if (!(o instanceof ChatbotSessionDTO)) {
            return false;
        }

        ChatbotSessionDTO chatbotSessionDTO = (ChatbotSessionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatbotSessionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatbotSessionDTO{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
