package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.QRCodeAsserts.*;
import static com.agricycle.app.domain.QRCodeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QRCodeMapperTest {

    private QRCodeMapper qRCodeMapper;

    @BeforeEach
    void setUp() {
        qRCodeMapper = new QRCodeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQRCodeSample1();
        var actual = qRCodeMapper.toEntity(qRCodeMapper.toDto(expected));
        assertQRCodeAllPropertiesEquals(expected, actual);
    }
}
