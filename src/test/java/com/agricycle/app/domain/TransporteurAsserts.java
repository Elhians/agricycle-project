package com.agricycle.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TransporteurAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransporteurAllPropertiesEquals(Transporteur expected, Transporteur actual) {
        assertTransporteurAutoGeneratedPropertiesEquals(expected, actual);
        assertTransporteurAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransporteurAllUpdatablePropertiesEquals(Transporteur expected, Transporteur actual) {
        assertTransporteurUpdatableFieldsEquals(expected, actual);
        assertTransporteurUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransporteurAutoGeneratedPropertiesEquals(Transporteur expected, Transporteur actual) {
        assertThat(actual)
            .as("Verify Transporteur auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransporteurUpdatableFieldsEquals(Transporteur expected, Transporteur actual) {
        assertThat(actual)
            .as("Verify Transporteur relevant properties")
            .satisfies(a -> assertThat(a.getZoneCouverture()).as("check zoneCouverture").isEqualTo(expected.getZoneCouverture()))
            .satisfies(a -> assertThat(a.getTypeVehicule()).as("check typeVehicule").isEqualTo(expected.getTypeVehicule()))
            .satisfies(a -> assertThat(a.getCapacite()).as("check capacite").isEqualTo(expected.getCapacite()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransporteurUpdatableRelationshipsEquals(Transporteur expected, Transporteur actual) {
        assertThat(actual)
            .as("Verify Transporteur relationships")
            .satisfies(a -> assertThat(a.getUtilisateur()).as("check utilisateur").isEqualTo(expected.getUtilisateur()));
    }
}
