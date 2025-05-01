package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConsommateurCriteriaTest {

    @Test
    void newConsommateurCriteriaHasAllFiltersNullTest() {
        var consommateurCriteria = new ConsommateurCriteria();
        assertThat(consommateurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void consommateurCriteriaFluentMethodsCreatesFiltersTest() {
        var consommateurCriteria = new ConsommateurCriteria();

        setAllFilters(consommateurCriteria);

        assertThat(consommateurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void consommateurCriteriaCopyCreatesNullFilterTest() {
        var consommateurCriteria = new ConsommateurCriteria();
        var copy = consommateurCriteria.copy();

        assertThat(consommateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(consommateurCriteria)
        );
    }

    @Test
    void consommateurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var consommateurCriteria = new ConsommateurCriteria();
        setAllFilters(consommateurCriteria);

        var copy = consommateurCriteria.copy();

        assertThat(consommateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(consommateurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var consommateurCriteria = new ConsommateurCriteria();

        assertThat(consommateurCriteria).hasToString("ConsommateurCriteria{}");
    }

    private static void setAllFilters(ConsommateurCriteria consommateurCriteria) {
        consommateurCriteria.id();
        consommateurCriteria.preferences();
        consommateurCriteria.utilisateurId();
        consommateurCriteria.distinct();
    }

    private static Condition<ConsommateurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPreferences()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConsommateurCriteria> copyFiltersAre(
        ConsommateurCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPreferences(), copy.getPreferences()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
