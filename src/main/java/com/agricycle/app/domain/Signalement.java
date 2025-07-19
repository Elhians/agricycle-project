package com.agricycle.app.domain;

import com.agricycle.app.domain.enumeration.Cible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Signalement.
 */
@Entity
@Table(name = "signalement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Signalement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "raison", nullable = false)
    private String raison;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cible", nullable = false)
    private Cible cible;

    @Column(name = "date")
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "agriculteur", "commercant", "transporteur", "consommateur", "organisation", "entreprise" },
        allowSetters = true
    )
    private Utilisateur auteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "auteur" }, allowSetters = true)
    private Post ciblePost;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Signalement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaison() {
        return this.raison;
    }

    public Signalement raison(String raison) {
        this.setRaison(raison);
        return this;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public Cible getCible() {
        return this.cible;
    }

    public Signalement cible(Cible cible) {
        this.setCible(cible);
        return this;
    }

    public void setCible(Cible cible) {
        this.cible = cible;
    }

    public Instant getDate() {
        return this.date;
    }

    public Signalement date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Utilisateur getAuteur() {
        return this.auteur;
    }

    public void setAuteur(Utilisateur utilisateur) {
        this.auteur = utilisateur;
    }

    public Signalement auteur(Utilisateur utilisateur) {
        this.setAuteur(utilisateur);
        return this;
    }

    public Post getCiblePost() {
        return this.ciblePost;
    }

    public void setCiblePost(Post post) {
        this.ciblePost = post;
    }

    public Signalement ciblePost(Post post) {
        this.setCiblePost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Signalement)) {
            return false;
        }
        return getId() != null && getId().equals(((Signalement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Signalement{" +
            "id=" + getId() +
            ", raison='" + getRaison() + "'" +
            ", cible='" + getCible() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
