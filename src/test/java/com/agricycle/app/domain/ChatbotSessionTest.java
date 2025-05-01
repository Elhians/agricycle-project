package com.agricycle.app.domain;

import static com.agricycle.app.domain.ChatbotSessionTestSamples.*;
import static com.agricycle.app.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatbotSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatbotSession.class);
        ChatbotSession chatbotSession1 = getChatbotSessionSample1();
        ChatbotSession chatbotSession2 = new ChatbotSession();
        assertThat(chatbotSession1).isNotEqualTo(chatbotSession2);

        chatbotSession2.setId(chatbotSession1.getId());
        assertThat(chatbotSession1).isEqualTo(chatbotSession2);

        chatbotSession2 = getChatbotSessionSample2();
        assertThat(chatbotSession1).isNotEqualTo(chatbotSession2);
    }

    @Test
    void utilisateurTest() {
        ChatbotSession chatbotSession = getChatbotSessionRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        chatbotSession.setUtilisateur(utilisateurBack);
        assertThat(chatbotSession.getUtilisateur()).isEqualTo(utilisateurBack);

        chatbotSession.utilisateur(null);
        assertThat(chatbotSession.getUtilisateur()).isNull();
    }
}
