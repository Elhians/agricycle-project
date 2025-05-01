package com.agricycle.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QRCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QRCodeDTO.class);
        QRCodeDTO qRCodeDTO1 = new QRCodeDTO();
        qRCodeDTO1.setId(1L);
        QRCodeDTO qRCodeDTO2 = new QRCodeDTO();
        assertThat(qRCodeDTO1).isNotEqualTo(qRCodeDTO2);
        qRCodeDTO2.setId(qRCodeDTO1.getId());
        assertThat(qRCodeDTO1).isEqualTo(qRCodeDTO2);
        qRCodeDTO2.setId(2L);
        assertThat(qRCodeDTO1).isNotEqualTo(qRCodeDTO2);
        qRCodeDTO1.setId(null);
        assertThat(qRCodeDTO1).isNotEqualTo(qRCodeDTO2);
    }
}
