package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.EntrepriseAsserts.*;
import static com.agricycle.app.domain.EntrepriseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntrepriseMapperTest {

    private EntrepriseMapper entrepriseMapper;

    @BeforeEach
    void setUp() {
        entrepriseMapper = new EntrepriseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntrepriseSample1();
        var actual = entrepriseMapper.toEntity(entrepriseMapper.toDto(expected));
        assertEntrepriseAllPropertiesEquals(expected, actual);
    }
}
