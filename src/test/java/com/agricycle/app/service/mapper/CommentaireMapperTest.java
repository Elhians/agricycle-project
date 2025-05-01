package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.CommentaireAsserts.*;
import static com.agricycle.app.domain.CommentaireTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentaireMapperTest {

    private CommentaireMapper commentaireMapper;

    @BeforeEach
    void setUp() {
        commentaireMapper = new CommentaireMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommentaireSample1();
        var actual = commentaireMapper.toEntity(commentaireMapper.toDto(expected));
        assertCommentaireAllPropertiesEquals(expected, actual);
    }
}
