package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContenuEducatifCriteriaTest {

    @Test
    void newContenuEducatifCriteriaHasAllFiltersNullTest() {
        var contenuEducatifCriteria = new ContenuEducatifCriteria();
        assertThat(contenuEducatifCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contenuEducatifCriteriaFluentMethodsCreatesFiltersTest() {
        var contenuEducatifCriteria = new ContenuEducatifCriteria();

        setAllFilters(contenuEducatifCriteria);

        assertThat(contenuEducatifCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contenuEducatifCriteriaCopyCreatesNullFilterTest() {
        var contenuEducatifCriteria = new ContenuEducatifCriteria();
        var copy = contenuEducatifCriteria.copy();

        assertThat(contenuEducatifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contenuEducatifCriteria)
        );
    }

    @Test
    void contenuEducatifCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contenuEducatifCriteria = new ContenuEducatifCriteria();
        setAllFilters(contenuEducatifCriteria);

        var copy = contenuEducatifCriteria.copy();

        assertThat(contenuEducatifCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contenuEducatifCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contenuEducatifCriteria = new ContenuEducatifCriteria();

        assertThat(contenuEducatifCriteria).hasToString("ContenuEducatifCriteria{}");
    }

    private static void setAllFilters(ContenuEducatifCriteria contenuEducatifCriteria) {
        contenuEducatifCriteria.id();
        contenuEducatifCriteria.titre();
        contenuEducatifCriteria.description();
        contenuEducatifCriteria.typeMedia();
        contenuEducatifCriteria.url();
        contenuEducatifCriteria.datePublication();
        contenuEducatifCriteria.auteurId();
        contenuEducatifCriteria.distinct();
    }

    private static Condition<ContenuEducatifCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitre()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTypeMedia()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getDatePublication()) &&
                condition.apply(criteria.getAuteurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContenuEducatifCriteria> copyFiltersAre(
        ContenuEducatifCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitre(), copy.getTitre()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTypeMedia(), copy.getTypeMedia()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getDatePublication(), copy.getDatePublication()) &&
                condition.apply(criteria.getAuteurId(), copy.getAuteurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
