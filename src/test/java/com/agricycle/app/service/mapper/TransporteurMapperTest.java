package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.TransporteurAsserts.*;
import static com.agricycle.app.domain.TransporteurTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransporteurMapperTest {

    private TransporteurMapper transporteurMapper;

    @BeforeEach
    void setUp() {
        transporteurMapper = new TransporteurMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransporteurSample1();
        var actual = transporteurMapper.toEntity(transporteurMapper.toDto(expected));
        assertTransporteurAllPropertiesEquals(expected, actual);
    }
}
