package com.agricycle.app.service.mapper;

import static com.agricycle.app.domain.ChatbotSessionAsserts.*;
import static com.agricycle.app.domain.ChatbotSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatbotSessionMapperTest {

    private ChatbotSessionMapper chatbotSessionMapper;

    @BeforeEach
    void setUp() {
        chatbotSessionMapper = new ChatbotSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChatbotSessionSample1();
        var actual = chatbotSessionMapper.toEntity(chatbotSessionMapper.toDto(expected));
        assertChatbotSessionAllPropertiesEquals(expected, actual);
    }
}
