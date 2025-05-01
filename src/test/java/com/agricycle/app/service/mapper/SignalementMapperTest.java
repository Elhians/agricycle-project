package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.SignalementAsserts.*;
import static com.agricycle.app.domain.SignalementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignalementMapperTest {

    private SignalementMapper signalementMapper;

    @BeforeEach
    void setUp() {
        signalementMapper = new SignalementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSignalementSample1();
        var actual = signalementMapper.toEntity(signalementMapper.toDto(expected));
        assertSignalementAllPropertiesEquals(expected, actual);
    }
}
