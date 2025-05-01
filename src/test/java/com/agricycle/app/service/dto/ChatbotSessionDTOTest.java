package com.agricycle.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatbotSessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatbotSessionDTO.class);
        ChatbotSessionDTO chatbotSessionDTO1 = new ChatbotSessionDTO();
        chatbotSessionDTO1.setId(1L);
        ChatbotSessionDTO chatbotSessionDTO2 = new ChatbotSessionDTO();
        assertThat(chatbotSessionDTO1).isNotEqualTo(chatbotSessionDTO2);
        chatbotSessionDTO2.setId(chatbotSessionDTO1.getId());
        assertThat(chatbotSessionDTO1).isEqualTo(chatbotSessionDTO2);
        chatbotSessionDTO2.setId(2L);
        assertThat(chatbotSessionDTO1).isNotEqualTo(chatbotSessionDTO2);
        chatbotSessionDTO1.setId(null);
        assertThat(chatbotSessionDTO1).isNotEqualTo(chatbotSessionDTO2);
    }
}
