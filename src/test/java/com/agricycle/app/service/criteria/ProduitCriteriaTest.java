package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProduitCriteriaTest {

    @Test
    void newProduitCriteriaHasAllFiltersNullTest() {
        var produitCriteria = new ProduitCriteria();
        assertThat(produitCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void produitCriteriaFluentMethodsCreatesFiltersTest() {
        var produitCriteria = new ProduitCriteria();

        setAllFilters(produitCriteria);

        assertThat(produitCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void produitCriteriaCopyCreatesNullFilterTest() {
        var produitCriteria = new ProduitCriteria();
        var copy = produitCriteria.copy();

        assertThat(produitCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(produitCriteria)
        );
    }

    @Test
    void produitCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var produitCriteria = new ProduitCriteria();
        setAllFilters(produitCriteria);

        var copy = produitCriteria.copy();

        assertThat(produitCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(produitCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var produitCriteria = new ProduitCriteria();

        assertThat(produitCriteria).hasToString("ProduitCriteria{}");
    }

    private static void setAllFilters(ProduitCriteria produitCriteria) {
        produitCriteria.id();
        produitCriteria.nom();
        produitCriteria.description();
        produitCriteria.prix();
        produitCriteria.quantite();
        produitCriteria.type();
        produitCriteria.statut();
        produitCriteria.imageUrl();
        produitCriteria.dateAjout();
        produitCriteria.vendeurId();
        produitCriteria.acheteurId();
        produitCriteria.distinct();
    }

    private static Condition<ProduitCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPrix()) &&
                condition.apply(criteria.getQuantite()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getDateAjout()) &&
                condition.apply(criteria.getVendeurId()) &&
                condition.apply(criteria.getAcheteurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProduitCriteria> copyFiltersAre(ProduitCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPrix(), copy.getPrix()) &&
                condition.apply(criteria.getQuantite(), copy.getQuantite()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getDateAjout(), copy.getDateAjout()) &&
                condition.apply(criteria.getVendeurId(), copy.getVendeurId()) &&
                condition.apply(criteria.getAcheteurId(), copy.getAcheteurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
