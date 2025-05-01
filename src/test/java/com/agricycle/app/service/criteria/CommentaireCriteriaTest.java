package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CommentaireCriteriaTest {

    @Test
    void newCommentaireCriteriaHasAllFiltersNullTest() {
        var commentaireCriteria = new CommentaireCriteria();
        assertThat(commentaireCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void commentaireCriteriaFluentMethodsCreatesFiltersTest() {
        var commentaireCriteria = new CommentaireCriteria();

        setAllFilters(commentaireCriteria);

        assertThat(commentaireCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void commentaireCriteriaCopyCreatesNullFilterTest() {
        var commentaireCriteria = new CommentaireCriteria();
        var copy = commentaireCriteria.copy();

        assertThat(commentaireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(commentaireCriteria)
        );
    }

    @Test
    void commentaireCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var commentaireCriteria = new CommentaireCriteria();
        setAllFilters(commentaireCriteria);

        var copy = commentaireCriteria.copy();

        assertThat(commentaireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(commentaireCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var commentaireCriteria = new CommentaireCriteria();

        assertThat(commentaireCriteria).hasToString("CommentaireCriteria{}");
    }

    private static void setAllFilters(CommentaireCriteria commentaireCriteria) {
        commentaireCriteria.id();
        commentaireCriteria.contenu();
        commentaireCriteria.date();
        commentaireCriteria.postId();
        commentaireCriteria.auteurId();
        commentaireCriteria.distinct();
    }

    private static Condition<CommentaireCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getContenu()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getPostId()) &&
                condition.apply(criteria.getAuteurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CommentaireCriteria> copyFiltersAre(CommentaireCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getContenu(), copy.getContenu()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getPostId(), copy.getPostId()) &&
                condition.apply(criteria.getAuteurId(), copy.getAuteurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
