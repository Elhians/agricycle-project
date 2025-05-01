package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.CommercantAsserts.*;
import static com.agricycle.app.domain.CommercantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommercantMapperTest {

    private CommercantMapper commercantMapper;

    @BeforeEach
    void setUp() {
        commercantMapper = new CommercantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommercantSample1();
        var actual = commercantMapper.toEntity(commercantMapper.toDto(expected));
        assertCommercantAllPropertiesEquals(expected, actual);
    }
}
