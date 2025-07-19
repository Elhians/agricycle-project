package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UtilisateurCriteriaTest {

    @Test
    void newUtilisateurCriteriaHasAllFiltersNullTest() {
        var utilisateurCriteria = new UtilisateurCriteria();
        assertThat(utilisateurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void utilisateurCriteriaFluentMethodsCreatesFiltersTest() {
        var utilisateurCriteria = new UtilisateurCriteria();

        setAllFilters(utilisateurCriteria);

        assertThat(utilisateurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void utilisateurCriteriaCopyCreatesNullFilterTest() {
        var utilisateurCriteria = new UtilisateurCriteria();
        var copy = utilisateurCriteria.copy();

        assertThat(utilisateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(utilisateurCriteria)
        );
    }

    @Test
    void utilisateurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var utilisateurCriteria = new UtilisateurCriteria();
        setAllFilters(utilisateurCriteria);

        var copy = utilisateurCriteria.copy();

        assertThat(utilisateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(utilisateurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var utilisateurCriteria = new UtilisateurCriteria();

        assertThat(utilisateurCriteria).hasToString("UtilisateurCriteria{}");
    }

    private static void setAllFilters(UtilisateurCriteria utilisateurCriteria) {
        utilisateurCriteria.id();
        utilisateurCriteria.phone();
        utilisateurCriteria.role();
        utilisateurCriteria.dateInscription();
        utilisateurCriteria.userId();
        utilisateurCriteria.agriculteurId();
        utilisateurCriteria.commercantId();
        utilisateurCriteria.transporteurId();
        utilisateurCriteria.consommateurId();
        utilisateurCriteria.organisationId();
        utilisateurCriteria.entrepriseId();
        utilisateurCriteria.distinct();
    }

    private static Condition<UtilisateurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getRole()) &&
                condition.apply(criteria.getDateInscription()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getAgriculteurId()) &&
                condition.apply(criteria.getCommercantId()) &&
                condition.apply(criteria.getTransporteurId()) &&
                condition.apply(criteria.getConsommateurId()) &&
                condition.apply(criteria.getOrganisationId()) &&
                condition.apply(criteria.getEntrepriseId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UtilisateurCriteria> copyFiltersAre(UtilisateurCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getRole(), copy.getRole()) &&
                condition.apply(criteria.getDateInscription(), copy.getDateInscription()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getAgriculteurId(), copy.getAgriculteurId()) &&
                condition.apply(criteria.getCommercantId(), copy.getCommercantId()) &&
                condition.apply(criteria.getTransporteurId(), copy.getTransporteurId()) &&
                condition.apply(criteria.getConsommateurId(), copy.getConsommateurId()) &&
                condition.apply(criteria.getOrganisationId(), copy.getOrganisationId()) &&
                condition.apply(criteria.getEntrepriseId(), copy.getEntrepriseId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
