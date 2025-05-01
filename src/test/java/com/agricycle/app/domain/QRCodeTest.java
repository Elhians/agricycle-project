package com.agricycle.app.domain;

import static com.agricycle.app.domain.QRCodeTestSamples.*;
import static com.agricycle.app.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agricycle.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QRCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QRCode.class);
        QRCode qRCode1 = getQRCodeSample1();
        QRCode qRCode2 = new QRCode();
        assertThat(qRCode1).isNotEqualTo(qRCode2);

        qRCode2.setId(qRCode1.getId());
        assertThat(qRCode1).isEqualTo(qRCode2);

        qRCode2 = getQRCodeSample2();
        assertThat(qRCode1).isNotEqualTo(qRCode2);
    }

    @Test
    void transactionTest() {
        QRCode qRCode = getQRCodeRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        qRCode.setTransaction(transactionBack);
        assertThat(qRCode.getTransaction()).isEqualTo(transactionBack);

        qRCode.transaction(null);
        assertThat(qRCode.getTransaction()).isNull();
    }
}
