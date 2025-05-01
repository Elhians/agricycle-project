package com.agricycle.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Commercant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommercantDTO implements Serializable {

    private Long id;

    private String adresseCommerce;

    private String moyenPaiement;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresseCommerce() {
        return adresseCommerce;
    }

    public void setAdresseCommerce(String adresseCommerce) {
        this.adresseCommerce = adresseCommerce;
    }

    public String getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
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
        if (!(o instanceof CommercantDTO)) {
            return false;
        }

        CommercantDTO commercantDTO = (CommercantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commercantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommercantDTO{" +
            "id=" + getId() +
            ", adresseCommerce='" + getAdresseCommerce() + "'" +
            ", moyenPaiement='" + getMoyenPaiement() + "'" +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
