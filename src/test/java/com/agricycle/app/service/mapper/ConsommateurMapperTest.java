package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.ConsommateurAsserts.*;
import static com.agricycle.app.domain.ConsommateurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsommateurMapperTest {

    private ConsommateurMapper consommateurMapper;

    @BeforeEach
    void setUp() {
        consommateurMapper = new ConsommateurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConsommateurSample1();
        var actual = consommateurMapper.toEntity(consommateurMapper.toDto(expected));
        assertConsommateurAllPropertiesEquals(expected, actual);
    }
}
