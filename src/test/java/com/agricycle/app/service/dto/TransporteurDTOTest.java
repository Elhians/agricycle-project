package com.agricycle.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransporteurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransporteurDTO.class);
        TransporteurDTO transporteurDTO1 = new TransporteurDTO();
        transporteurDTO1.setId(1L);
        TransporteurDTO transporteurDTO2 = new TransporteurDTO();
        assertThat(transporteurDTO1).isNotEqualTo(transporteurDTO2);
        transporteurDTO2.setId(transporteurDTO1.getId());
        assertThat(transporteurDTO1).isEqualTo(transporteurDTO2);
        transporteurDTO2.setId(2L);
        assertThat(transporteurDTO1).isNotEqualTo(transporteurDTO2);
        transporteurDTO1.setId(null);
        assertThat(transporteurDTO1).isNotEqualTo(transporteurDTO2);
    }
}
