package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgriculteurCriteriaTest {

    @Test
    void newAgriculteurCriteriaHasAllFiltersNullTest() {
        var agriculteurCriteria = new AgriculteurCriteria();
        assertThat(agriculteurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void agriculteurCriteriaFluentMethodsCreatesFiltersTest() {
        var agriculteurCriteria = new AgriculteurCriteria();

        setAllFilters(agriculteurCriteria);

        assertThat(agriculteurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void agriculteurCriteriaCopyCreatesNullFilterTest() {
        var agriculteurCriteria = new AgriculteurCriteria();
        var copy = agriculteurCriteria.copy();

        assertThat(agriculteurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(agriculteurCriteria)
        );
    }

    @Test
    void agriculteurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agriculteurCriteria = new AgriculteurCriteria();
        setAllFilters(agriculteurCriteria);

        var copy = agriculteurCriteria.copy();

        assertThat(agriculteurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(agriculteurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agriculteurCriteria = new AgriculteurCriteria();

        assertThat(agriculteurCriteria).hasToString("AgriculteurCriteria{}");
    }

    private static void setAllFilters(AgriculteurCriteria agriculteurCriteria) {
        agriculteurCriteria.id();
        agriculteurCriteria.typeProduction();
        agriculteurCriteria.anneeExperience();
        agriculteurCriteria.localisation();
        agriculteurCriteria.utilisateurId();
        agriculteurCriteria.distinct();
    }

    private static Condition<AgriculteurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTypeProduction()) &&
                condition.apply(criteria.getAnneeExperience()) &&
                condition.apply(criteria.getLocalisation()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgriculteurCriteria> copyFiltersAre(AgriculteurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTypeProduction(), copy.getTypeProduction()) &&
                condition.apply(criteria.getAnneeExperience(), copy.getAnneeExperience()) &&
                condition.apply(criteria.getLocalisation(), copy.getLocalisation()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
