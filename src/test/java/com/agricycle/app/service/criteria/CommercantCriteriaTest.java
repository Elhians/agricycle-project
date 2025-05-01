package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CommercantCriteriaTest {

    @Test
    void newCommercantCriteriaHasAllFiltersNullTest() {
        var commercantCriteria = new CommercantCriteria();
        assertThat(commercantCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void commercantCriteriaFluentMethodsCreatesFiltersTest() {
        var commercantCriteria = new CommercantCriteria();

        setAllFilters(commercantCriteria);

        assertThat(commercantCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void commercantCriteriaCopyCreatesNullFilterTest() {
        var commercantCriteria = new CommercantCriteria();
        var copy = commercantCriteria.copy();

        assertThat(commercantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(commercantCriteria)
        );
    }

    @Test
    void commercantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var commercantCriteria = new CommercantCriteria();
        setAllFilters(commercantCriteria);

        var copy = commercantCriteria.copy();

        assertThat(commercantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(commercantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var commercantCriteria = new CommercantCriteria();

        assertThat(commercantCriteria).hasToString("CommercantCriteria{}");
    }

    private static void setAllFilters(CommercantCriteria commercantCriteria) {
        commercantCriteria.id();
        commercantCriteria.adresseCommerce();
        commercantCriteria.moyenPaiement();
        commercantCriteria.utilisateurId();
        commercantCriteria.distinct();
    }

    private static Condition<CommercantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAdresseCommerce()) &&
                condition.apply(criteria.getMoyenPaiement()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CommercantCriteria> copyFiltersAre(CommercantCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAdresseCommerce(), copy.getAdresseCommerce()) &&
                condition.apply(criteria.getMoyenPaiement(), copy.getMoyenPaiement()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
