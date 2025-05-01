package com.agricycle.app.domain;

import com.agricycle.app.domain.enumeration.TypeVehicule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transporteur.
 */
@Entity
@Table(name = "transporteur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transporteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "zone_couverture")
    private String zoneCouverture;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_vehicule")
    private TypeVehicule typeVehicule;

    @Column(name = "capacite")
    private Float capacite;

    @JsonIgnoreProperties(
        value = { "agriculteur", "commercant", "transporteur", "consommateur", "organisation", "entreprise" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transporteur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZoneCouverture() {
        return this.zoneCouverture;
    }

    public Transporteur zoneCouverture(String zoneCouverture) {
        this.setZoneCouverture(zoneCouverture);
        return this;
    }

    public void setZoneCouverture(String zoneCouverture) {
        this.zoneCouverture = zoneCouverture;
    }

    public TypeVehicule getTypeVehicule() {
        return this.typeVehicule;
    }

    public Transporteur typeVehicule(TypeVehicule typeVehicule) {
        this.setTypeVehicule(typeVehicule);
        return this;
    }

    public void setTypeVehicule(TypeVehicule typeVehicule) {
        this.typeVehicule = typeVehicule;
    }

    public Float getCapacite() {
        return this.capacite;
    }

    public Transporteur capacite(Float capacite) {
        this.setCapacite(capacite);
        return this;
    }

    public void setCapacite(Float capacite) {
        this.capacite = capacite;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Transporteur utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transporteur)) {
            return false;
        }
        return getId() != null && getId().equals(((Transporteur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transporteur{" +
            "id=" + getId() +
            ", zoneCouverture='" + getZoneCouverture() + "'" +
            ", typeVehicule='" + getTypeVehicule() + "'" +
            ", capacite=" + getCapacite() +
            "}";
    }
}
