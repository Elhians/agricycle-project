package com.agricycle.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContenuEducatifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContenuEducatifDTO.class);
        ContenuEducatifDTO contenuEducatifDTO1 = new ContenuEducatifDTO();
        contenuEducatifDTO1.setId(1L);
        ContenuEducatifDTO contenuEducatifDTO2 = new ContenuEducatifDTO();
        assertThat(contenuEducatifDTO1).isNotEqualTo(contenuEducatifDTO2);
        contenuEducatifDTO2.setId(contenuEducatifDTO1.getId());
        assertThat(contenuEducatifDTO1).isEqualTo(contenuEducatifDTO2);
        contenuEducatifDTO2.setId(2L);
        assertThat(contenuEducatifDTO1).isNotEqualTo(contenuEducatifDTO2);
        contenuEducatifDTO1.setId(null);
        assertThat(contenuEducatifDTO1).isNotEqualTo(contenuEducatifDTO2);
    }
}
