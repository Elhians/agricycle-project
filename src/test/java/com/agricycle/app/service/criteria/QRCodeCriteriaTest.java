package com.agricycle.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QRCodeCriteriaTest {

    @Test
    void newQRCodeCriteriaHasAllFiltersNullTest() {
        var qRCodeCriteria = new QRCodeCriteria();
        assertThat(qRCodeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void qRCodeCriteriaFluentMethodsCreatesFiltersTest() {
        var qRCodeCriteria = new QRCodeCriteria();

        setAllFilters(qRCodeCriteria);

        assertThat(qRCodeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void qRCodeCriteriaCopyCreatesNullFilterTest() {
        var qRCodeCriteria = new QRCodeCriteria();
        var copy = qRCodeCriteria.copy();

        assertThat(qRCodeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(qRCodeCriteria)
        );
    }

    @Test
    void qRCodeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var qRCodeCriteria = new QRCodeCriteria();
        setAllFilters(qRCodeCriteria);

        var copy = qRCodeCriteria.copy();

        assertThat(qRCodeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(qRCodeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var qRCodeCriteria = new QRCodeCriteria();

        assertThat(qRCodeCriteria).hasToString("QRCodeCriteria{}");
    }

    private static void setAllFilters(QRCodeCriteria qRCodeCriteria) {
        qRCodeCriteria.id();
        qRCodeCriteria.valeur();
        qRCodeCriteria.dateExpiration();
        qRCodeCriteria.transactionId();
        qRCodeCriteria.distinct();
    }

    private static Condition<QRCodeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getValeur()) &&
                condition.apply(criteria.getDateExpiration()) &&
                condition.apply(criteria.getTransactionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QRCodeCriteria> copyFiltersAre(QRCodeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getValeur(), copy.getValeur()) &&
                condition.apply(criteria.getDateExpiration(), copy.getDateExpiration()) &&
                condition.apply(criteria.getTransactionId(), copy.getTransactionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
