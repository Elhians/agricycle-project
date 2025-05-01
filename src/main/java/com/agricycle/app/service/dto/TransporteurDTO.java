package com.agricycle.app.service.dto;

import com.agricycle.app.domain.enumeration.TypeVehicule;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Transporteur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransporteurDTO implements Serializable {

    private Long id;

    private String zoneCouverture;

    private TypeVehicule typeVehicule;

    private Float capacite;

    private UtilisateurDTO utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZoneCouverture() {
        return zoneCouverture;
    }

    public void setZoneCouverture(String zoneCouverture) {
        this.zoneCouverture = zoneCouverture;
    }

    public TypeVehicule getTypeVehicule() {
        return typeVehicule;
    }

    public void setTypeVehicule(TypeVehicule typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public Float getCapacite() {
        return capacite;
    }

    public void setCapacite(Float capacite) {
        this.capacite = capacite;
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
        if (!(o instanceof TransporteurDTO)) {
            return false;
        }

        TransporteurDTO transporteurDTO = (TransporteurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transporteurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransporteurDTO{" +
            "id=" + getId() +
            ", zoneCouverture='" + getZoneCouverture() + "'" +
            ", typeVehicule='" + getTypeVehicule() + "'" +
            ", capacite=" + getCapacite() +
            ", utilisateur=" + getUtilisateur() +
            "}";
    }
}
