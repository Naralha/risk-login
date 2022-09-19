package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PapelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Papel.class);
        Papel papel1 = new Papel();
        papel1.setId(1L);
        Papel papel2 = new Papel();
        papel2.setId(papel1.getId());
        assertThat(papel1).isEqualTo(papel2);
        papel2.setId(2L);
        assertThat(papel1).isNotEqualTo(papel2);
        papel1.setId(null);
        assertThat(papel1).isNotEqualTo(papel2);
    }
}
