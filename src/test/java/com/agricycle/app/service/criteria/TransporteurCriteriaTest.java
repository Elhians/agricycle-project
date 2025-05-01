package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransporteurCriteriaTest {

    @Test
    void newTransporteurCriteriaHasAllFiltersNullTest() {
        var transporteurCriteria = new TransporteurCriteria();
        assertThat(transporteurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transporteurCriteriaFluentMethodsCreatesFiltersTest() {
        var transporteurCriteria = new TransporteurCriteria();

        setAllFilters(transporteurCriteria);

        assertThat(transporteurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transporteurCriteriaCopyCreatesNullFilterTest() {
        var transporteurCriteria = new TransporteurCriteria();
        var copy = transporteurCriteria.copy();

        assertThat(transporteurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transporteurCriteria)
        );
    }

    @Test
    void transporteurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transporteurCriteria = new TransporteurCriteria();
        setAllFilters(transporteurCriteria);

        var copy = transporteurCriteria.copy();

        assertThat(transporteurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transporteurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transporteurCriteria = new TransporteurCriteria();

        assertThat(transporteurCriteria).hasToString("TransporteurCriteria{}");
    }

    private static void setAllFilters(TransporteurCriteria transporteurCriteria) {
        transporteurCriteria.id();
        transporteurCriteria.zoneCouverture();
        transporteurCriteria.typeVehicule();
        transporteurCriteria.capacite();
        transporteurCriteria.utilisateurId();
        transporteurCriteria.distinct();
    }

    private static Condition<TransporteurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getZoneCouverture()) &&
                condition.apply(criteria.getTypeVehicule()) &&
                condition.apply(criteria.getCapacite()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransporteurCriteria> copyFiltersAre(
        TransporteurCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getZoneCouverture(), copy.getZoneCouverture()) &&
                condition.apply(criteria.getTypeVehicule(), copy.getTypeVehicule()) &&
                condition.apply(criteria.getCapacite(), copy.getCapacite()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
