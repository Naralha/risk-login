package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.entity.Grupo;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Grupo.class);
        Grupo grupo1 = new Grupo();
        grupo1.setId(1L);
        Grupo grupo2 = new Grupo();
        grupo2.setId(grupo1.getId());
        assertThat(grupo1).isEqualTo(grupo2);
        grupo2.setId(2L);
        assertThat(grupo1).isNotEqualTo(grupo2);
        grupo1.setId(null);
        assertThat(grupo1).isNotEqualTo(grupo2);
    }
}
