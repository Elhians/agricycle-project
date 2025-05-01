package com.agricycle.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentaireDTO.class);
        CommentaireDTO commentaireDTO1 = new CommentaireDTO();
        commentaireDTO1.setId(1L);
        CommentaireDTO commentaireDTO2 = new CommentaireDTO();
        assertThat(commentaireDTO1).isNotEqualTo(commentaireDTO2);
        commentaireDTO2.setId(commentaireDTO1.getId());
        assertThat(commentaireDTO1).isEqualTo(commentaireDTO2);
        commentaireDTO2.setId(2L);
        assertThat(commentaireDTO1).isNotEqualTo(commentaireDTO2);
        commentaireDTO1.setId(null);
        assertThat(commentaireDTO1).isNotEqualTo(commentaireDTO2);
    }
}
