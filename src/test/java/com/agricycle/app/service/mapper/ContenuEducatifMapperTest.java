package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.ContenuEducatifAsserts.*;
import static com.agricycle.app.domain.ContenuEducatifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContenuEducatifMapperTest {

    private ContenuEducatifMapper contenuEducatifMapper;

    @BeforeEach
    void setUp() {
        contenuEducatifMapper = new ContenuEducatifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContenuEducatifSample1();
        var actual = contenuEducatifMapper.toEntity(contenuEducatifMapper.toDto(expected));
        assertContenuEducatifAllPropertiesEquals(expected, actual);
    }
}
