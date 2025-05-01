package com.agricycle.app.service.criteria;

import com.agricycle.app.domain.enumeration.StatutAnnonce;
import com.agricycle.app.domain.enumeration.TypeProduit;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agricycle.app.domain.Produit} entity. This class is used
 * in {@link com.agricycle.app.web.rest.ProduitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /produits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProduitCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeProduit
     */
    public static class TypeProduitFilter extends Filter<TypeProduit> {

        public TypeProduitFilter() {}

        public TypeProduitFilter(TypeProduitFilter filter) {
            super(filter);
        }

        @Override
        public TypeProduitFilter copy() {
            return new TypeProduitFilter(this);
        }
    }

    /**
     * Class for filtering StatutAnnonce
     */
    public static class StatutAnnonceFilter extends Filter<StatutAnnonce> {

        public StatutAnnonceFilter() {}

        public StatutAnnonceFilter(StatutAnnonceFilter filter) {
            super(filter);
        }

        @Override
        public StatutAnnonceFilter copy() {
            return new StatutAnnonceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter description;

    private DoubleFilter prix;

    private IntegerFilter quantite;

    private TypeProduitFilter type;

    private StatutAnnonceFilter statut;

    private StringFilter imageUrl;

    private InstantFilter dateAjout;

    private LongFilter vendeurId;

    private LongFilter acheteurId;

    private Boolean distinct;

    public ProduitCriteria() {}

    public ProduitCriteria(ProduitCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.prix = other.optionalPrix().map(DoubleFilter::copy).orElse(null);
        this.quantite = other.optionalQuantite().map(IntegerFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeProduitFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutAnnonceFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.dateAjout = other.optionalDateAjout().map(InstantFilter::copy).orElse(null);
        this.vendeurId = other.optionalVendeurId().map(LongFilter::copy).orElse(null);
        this.acheteurId = other.optionalAcheteurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProduitCriteria copy() {
        return new ProduitCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getPrix() {
        return prix;
    }

    public Optional<DoubleFilter> optionalPrix() {
        return Optional.ofNullable(prix);
    }

    public DoubleFilter prix() {
        if (prix == null) {
            setPrix(new DoubleFilter());
        }
        return prix;
    }

    public void setPrix(DoubleFilter prix) {
        this.prix = prix;
    }

    public IntegerFilter getQuantite() {
        return quantite;
    }

    public Optional<IntegerFilter> optionalQuantite() {
        return Optional.ofNullable(quantite);
    }

    public IntegerFilter quantite() {
        if (quantite == null) {
            setQuantite(new IntegerFilter());
        }
        return quantite;
    }

    public void setQuantite(IntegerFilter quantite) {
        this.quantite = quantite;
    }

    public TypeProduitFilter getType() {
        return type;
    }

    public Optional<TypeProduitFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TypeProduitFilter type() {
        if (type == null) {
            setType(new TypeProduitFilter());
        }
        return type;
    }

    public void setType(TypeProduitFilter type) {
        this.type = type;
    }

    public StatutAnnonceFilter getStatut() {
        return statut;
    }

    public Optional<StatutAnnonceFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutAnnonceFilter statut() {
        if (statut == null) {
            setStatut(new StatutAnnonceFilter());
        }
        return statut;
    }

    public void setStatut(StatutAnnonceFilter statut) {
        this.statut = statut;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public InstantFilter getDateAjout() {
        return dateAjout;
    }

    public Optional<InstantFilter> optionalDateAjout() {
        return Optional.ofNullable(dateAjout);
    }

    public InstantFilter dateAjout() {
        if (dateAjout == null) {
            setDateAjout(new InstantFilter());
        }
        return dateAjout;
    }

    public void setDateAjout(InstantFilter dateAjout) {
        this.dateAjout = dateAjout;
    }

    public LongFilter getVendeurId() {
        return vendeurId;
    }

    public Optional<LongFilter> optionalVendeurId() {
        return Optional.ofNullable(vendeurId);
    }

    public LongFilter vendeurId() {
        if (vendeurId == null) {
            setVendeurId(new LongFilter());
        }
        return vendeurId;
    }

    public void setVendeurId(LongFilter vendeurId) {
        this.vendeurId = vendeurId;
    }

    public LongFilter getAcheteurId() {
        return acheteurId;
    }

    public Optional<LongFilter> optionalAcheteurId() {
        return Optional.ofNullable(acheteurId);
    }

    public LongFilter acheteurId() {
        if (acheteurId == null) {
            setAcheteurId(new LongFilter());
        }
        return acheteurId;
    }

    public void setAcheteurId(LongFilter acheteurId) {
        this.acheteurId = acheteurId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProduitCriteria that = (ProduitCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(prix, that.prix) &&
            Objects.equals(quantite, that.quantite) &&
            Objects.equals(type, that.type) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(dateAjout, that.dateAjout) &&
            Objects.equals(vendeurId, that.vendeurId) &&
            Objects.equals(acheteurId, that.acheteurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, prix, quantite, type, statut, imageUrl, dateAjout, vendeurId, acheteurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProduitCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalPrix().map(f -> "prix=" + f + ", ").orElse("") +
            optionalQuantite().map(f -> "quantite=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalDateAjout().map(f -> "dateAjout=" + f + ", ").orElse("") +
            optionalVendeurId().map(f -> "vendeurId=" + f + ", ").orElse("") +
            optionalAcheteurId().map(f -> "acheteurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
