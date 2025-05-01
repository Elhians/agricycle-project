package com.agricycle.app.service.dto;

import com.agricycle.app.domain.enumeration.StatutAnnonce;
import com.agricycle.app.domain.enumeration.TypeProduit;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.agricycle.app.domain.Produit} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProduitDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private String description;

    @NotNull
    private Double prix;

    @NotNull
    private Integer quantite;

    @NotNull
    private TypeProduit type;

    @NotNull
    private StatutAnnonce statut;

    private String imageUrl;

    private Instant dateAjout;

    private UtilisateurDTO vendeur;

    private UtilisateurDTO acheteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public TypeProduit getType() {
        return type;
    }

    public void setType(TypeProduit type) {
        this.type = type;
    }

    public StatutAnnonce getStatut() {
        return statut;
    }

    public void setStatut(StatutAnnonce statut) {
        this.statut = statut;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Instant dateAjout) {
        this.dateAjout = dateAjout;
    }

    public UtilisateurDTO getVendeur() {
        return vendeur;
    }

    public void setVendeur(UtilisateurDTO vendeur) {
        this.vendeur = vendeur;
    }

    public UtilisateurDTO getAcheteur() {
        return acheteur;
    }

    public void setAcheteur(UtilisateurDTO acheteur) {
        this.acheteur = acheteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProduitDTO)) {
            return false;
        }

        ProduitDTO produitDTO = (ProduitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, produitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProduitDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", prix=" + getPrix() +
            ", quantite=" + getQuantite() +
            ", type='" + getType() + "'" +
            ", statut='" + getStatut() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", dateAjout='" + getDateAjout() + "'" +
            ", vendeur=" + getVendeur() +
            ", acheteur=" + getAcheteur() +
            "}";
    }
}
