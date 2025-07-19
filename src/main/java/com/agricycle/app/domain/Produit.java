package com.agricycle.app.domain;

import com.agricycle.app.domain.enumeration.StatutAnnonce;
import com.agricycle.app.domain.enumeration.TypeProduit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "prix", nullable = false)
    private Double prix;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeProduit type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutAnnonce statut;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "date_ajout")
    private Instant dateAjout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "agriculteur", "commercant", "transporteur", "consommateur", "organisation", "entreprise" },
        allowSetters = true
    )
    private Utilisateur vendeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "agriculteur", "commercant", "transporteur", "consommateur", "organisation", "entreprise" },
        allowSetters = true
    )
    private Utilisateur acheteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Produit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Produit nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Produit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Produit prix(Double prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public Produit quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public TypeProduit getType() {
        return this.type;
    }

    public Produit type(TypeProduit type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeProduit type) {
        this.type = type;
    }

    public StatutAnnonce getStatut() {
        return this.statut;
    }

    public Produit statut(StatutAnnonce statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutAnnonce statut) {
        this.statut = statut;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Produit imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getDateAjout() {
        return this.dateAjout;
    }

    public Produit dateAjout(Instant dateAjout) {
        this.setDateAjout(dateAjout);
        return this;
    }

    public void setDateAjout(Instant dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Utilisateur getVendeur() {
        return this.vendeur;
    }

    public void setVendeur(Utilisateur utilisateur) {
        this.vendeur = utilisateur;
    }

    public Produit vendeur(Utilisateur utilisateur) {
        this.setVendeur(utilisateur);
        return this;
    }

    public Utilisateur getAcheteur() {
        return this.acheteur;
    }

    public void setAcheteur(Utilisateur utilisateur) {
        this.acheteur = utilisateur;
    }

    public Produit acheteur(Utilisateur utilisateur) {
        this.setAcheteur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return getId() != null && getId().equals(((Produit) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", prix=" + getPrix() +
            ", quantite=" + getQuantite() +
            ", type='" + getType() + "'" +
            ", statut='" + getStatut() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", dateAjout='" + getDateAjout() + "'" +
            "}";
    }
}
