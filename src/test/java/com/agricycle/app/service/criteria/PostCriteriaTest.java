package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PostCriteriaTest {

    @Test
    void newPostCriteriaHasAllFiltersNullTest() {
        var postCriteria = new PostCriteria();
        assertThat(postCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void postCriteriaFluentMethodsCreatesFiltersTest() {
        var postCriteria = new PostCriteria();

        setAllFilters(postCriteria);

        assertThat(postCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void postCriteriaCopyCreatesNullFilterTest() {
        var postCriteria = new PostCriteria();
        var copy = postCriteria.copy();

        assertThat(postCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(postCriteria)
        );
    }

    @Test
    void postCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var postCriteria = new PostCriteria();
        setAllFilters(postCriteria);

        var copy = postCriteria.copy();

        assertThat(postCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(postCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var postCriteria = new PostCriteria();

        assertThat(postCriteria).hasToString("PostCriteria{}");
    }

    private static void setAllFilters(PostCriteria postCriteria) {
        postCriteria.id();
        postCriteria.contenu();
        postCriteria.dateCreation();
        postCriteria.auteurId();
        postCriteria.distinct();
    }

    private static Condition<PostCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getContenu()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getAuteurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PostCriteria> copyFiltersAre(PostCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getContenu(), copy.getContenu()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getAuteurId(), copy.getAuteurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
