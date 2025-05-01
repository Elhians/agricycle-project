package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ChatbotSessionCriteriaTest {

    @Test
    void newChatbotSessionCriteriaHasAllFiltersNullTest() {
        var chatbotSessionCriteria = new ChatbotSessionCriteria();
        assertThat(chatbotSessionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void chatbotSessionCriteriaFluentMethodsCreatesFiltersTest() {
        var chatbotSessionCriteria = new ChatbotSessionCriteria();

        setAllFilters(chatbotSessionCriteria);

        assertThat(chatbotSessionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void chatbotSessionCriteriaCopyCreatesNullFilterTest() {
        var chatbotSessionCriteria = new ChatbotSessionCriteria();
        var copy = chatbotSessionCriteria.copy();

        assertThat(chatbotSessionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(chatbotSessionCriteria)
        );
    }

    @Test
    void chatbotSessionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var chatbotSessionCriteria = new ChatbotSessionCriteria();
        setAllFilters(chatbotSessionCriteria);

        var copy = chatbotSessionCriteria.copy();

        assertThat(chatbotSessionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(chatbotSessionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var chatbotSessionCriteria = new ChatbotSessionCriteria();

        assertThat(chatbotSessionCriteria).hasToString("ChatbotSessionCriteria{}");
    }

    private static void setAllFilters(ChatbotSessionCriteria chatbotSessionCriteria) {
        chatbotSessionCriteria.id();
        chatbotSessionCriteria.dateDebut();
        chatbotSessionCriteria.dateFin();
        chatbotSessionCriteria.utilisateurId();
        chatbotSessionCriteria.distinct();
    }

    private static Condition<ChatbotSessionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ChatbotSessionCriteria> copyFiltersAre(
        ChatbotSessionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
