package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EntrepriseCriteriaTest {

    @Test
    void newEntrepriseCriteriaHasAllFiltersNullTest() {
        var entrepriseCriteria = new EntrepriseCriteria();
        assertThat(entrepriseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void entrepriseCriteriaFluentMethodsCreatesFiltersTest() {
        var entrepriseCriteria = new EntrepriseCriteria();

        setAllFilters(entrepriseCriteria);

        assertThat(entrepriseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void entrepriseCriteriaCopyCreatesNullFilterTest() {
        var entrepriseCriteria = new EntrepriseCriteria();
        var copy = entrepriseCriteria.copy();

        assertThat(entrepriseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(entrepriseCriteria)
        );
    }

    @Test
    void entrepriseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var entrepriseCriteria = new EntrepriseCriteria();
        setAllFilters(entrepriseCriteria);

        var copy = entrepriseCriteria.copy();

        assertThat(entrepriseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(entrepriseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var entrepriseCriteria = new EntrepriseCriteria();

        assertThat(entrepriseCriteria).hasToString("EntrepriseCriteria{}");
    }

    private static void setAllFilters(EntrepriseCriteria entrepriseCriteria) {
        entrepriseCriteria.id();
        entrepriseCriteria.nomEntreprise();
        entrepriseCriteria.typeActivite();
        entrepriseCriteria.licence();
        entrepriseCriteria.adressePhysique();
        entrepriseCriteria.utilisateurId();
        entrepriseCriteria.distinct();
    }

    private static Condition<EntrepriseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNomEntreprise()) &&
                condition.apply(criteria.getTypeActivite()) &&
                condition.apply(criteria.getLicence()) &&
                condition.apply(criteria.getAdressePhysique()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EntrepriseCriteria> copyFiltersAre(EntrepriseCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNomEntreprise(), copy.getNomEntreprise()) &&
                condition.apply(criteria.getTypeActivite(), copy.getTypeActivite()) &&
                condition.apply(criteria.getLicence(), copy.getLicence()) &&
                condition.apply(criteria.getAdressePhysique(), copy.getAdressePhysique()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
