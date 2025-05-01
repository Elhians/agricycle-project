package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PaiementCriteriaTest {

    @Test
    void newPaiementCriteriaHasAllFiltersNullTest() {
        var paiementCriteria = new PaiementCriteria();
        assertThat(paiementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void paiementCriteriaFluentMethodsCreatesFiltersTest() {
        var paiementCriteria = new PaiementCriteria();

        setAllFilters(paiementCriteria);

        assertThat(paiementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void paiementCriteriaCopyCreatesNullFilterTest() {
        var paiementCriteria = new PaiementCriteria();
        var copy = paiementCriteria.copy();

        assertThat(paiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(paiementCriteria)
        );
    }

    @Test
    void paiementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var paiementCriteria = new PaiementCriteria();
        setAllFilters(paiementCriteria);

        var copy = paiementCriteria.copy();

        assertThat(paiementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(paiementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var paiementCriteria = new PaiementCriteria();

        assertThat(paiementCriteria).hasToString("PaiementCriteria{}");
    }

    private static void setAllFilters(PaiementCriteria paiementCriteria) {
        paiementCriteria.id();
        paiementCriteria.moyenPaiement();
        paiementCriteria.preuve();
        paiementCriteria.date();
        paiementCriteria.transactionId();
        paiementCriteria.acheteurId();
        paiementCriteria.distinct();
    }

    private static Condition<PaiementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMoyenPaiement()) &&
                condition.apply(criteria.getPreuve()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getTransactionId()) &&
                condition.apply(criteria.getAcheteurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PaiementCriteria> copyFiltersAre(PaiementCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMoyenPaiement(), copy.getMoyenPaiement()) &&
                condition.apply(criteria.getPreuve(), copy.getPreuve()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getTransactionId(), copy.getTransactionId()) &&
                condition.apply(criteria.getAcheteurId(), copy.getAcheteurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
