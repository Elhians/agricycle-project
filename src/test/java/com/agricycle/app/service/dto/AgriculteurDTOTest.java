package com.agricycle.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgriculteurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgriculteurDTO.class);
        AgriculteurDTO agriculteurDTO1 = new AgriculteurDTO();
        agriculteurDTO1.setId(1L);
        AgriculteurDTO agriculteurDTO2 = new AgriculteurDTO();
        assertThat(agriculteurDTO1).isNotEqualTo(agriculteurDTO2);
        agriculteurDTO2.setId(agriculteurDTO1.getId());
        assertThat(agriculteurDTO1).isEqualTo(agriculteurDTO2);
        agriculteurDTO2.setId(2L);
        assertThat(agriculteurDTO1).isNotEqualTo(agriculteurDTO2);
        agriculteurDTO1.setId(null);
        assertThat(agriculteurDTO1).isNotEqualTo(agriculteurDTO2);
    }
}
