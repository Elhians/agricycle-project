package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrganisationCriteriaTest {

    @Test
    void newOrganisationCriteriaHasAllFiltersNullTest() {
        var organisationCriteria = new OrganisationCriteria();
        assertThat(organisationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void organisationCriteriaFluentMethodsCreatesFiltersTest() {
        var organisationCriteria = new OrganisationCriteria();

        setAllFilters(organisationCriteria);

        assertThat(organisationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void organisationCriteriaCopyCreatesNullFilterTest() {
        var organisationCriteria = new OrganisationCriteria();
        var copy = organisationCriteria.copy();

        assertThat(organisationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(organisationCriteria)
        );
    }

    @Test
    void organisationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var organisationCriteria = new OrganisationCriteria();
        setAllFilters(organisationCriteria);

        var copy = organisationCriteria.copy();

        assertThat(organisationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(organisationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var organisationCriteria = new OrganisationCriteria();

        assertThat(organisationCriteria).hasToString("OrganisationCriteria{}");
    }

    private static void setAllFilters(OrganisationCriteria organisationCriteria) {
        organisationCriteria.id();
        organisationCriteria.nomOrganisation();
        organisationCriteria.siteWeb();
        organisationCriteria.utilisateurId();
        organisationCriteria.distinct();
    }

    private static Condition<OrganisationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNomOrganisation()) &&
                condition.apply(criteria.getSiteWeb()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrganisationCriteria> copyFiltersAre(
        OrganisationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNomOrganisation(), copy.getNomOrganisation()) &&
                condition.apply(criteria.getSiteWeb(), copy.getSiteWeb()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
