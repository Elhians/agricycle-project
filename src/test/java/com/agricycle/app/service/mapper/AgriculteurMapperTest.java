package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.AgriculteurAsserts.*;
import static com.agricycle.app.domain.AgriculteurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgriculteurMapperTest {

    private AgriculteurMapper agriculteurMapper;

    @BeforeEach
    void setUp() {
        agriculteurMapper = new AgriculteurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAgriculteurSample1();
        var actual = agriculteurMapper.toEntity(agriculteurMapper.toDto(expected));
        assertAgriculteurAllPropertiesEquals(expected, actual);
    }
}
