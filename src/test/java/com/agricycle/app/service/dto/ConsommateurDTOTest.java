package com.agricycle.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsommateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsommateurDTO.class);
        ConsommateurDTO consommateurDTO1 = new ConsommateurDTO();
        consommateurDTO1.setId(1L);
        ConsommateurDTO consommateurDTO2 = new ConsommateurDTO();
        assertThat(consommateurDTO1).isNotEqualTo(consommateurDTO2);
        consommateurDTO2.setId(consommateurDTO1.getId());
        assertThat(consommateurDTO1).isEqualTo(consommateurDTO2);
        consommateurDTO2.setId(2L);
        assertThat(consommateurDTO1).isNotEqualTo(consommateurDTO2);
        consommateurDTO1.setId(null);
        assertThat(consommateurDTO1).isNotEqualTo(consommateurDTO2);
    }
}
