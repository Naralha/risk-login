package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.entity.GrupoPapel;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoPapelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GrupoPapel.class);
        GrupoPapel grupoPapel1 = new GrupoPapel();
        grupoPapel1.setId(1L);
        GrupoPapel grupoPapel2 = new GrupoPapel();
        grupoPapel2.setId(grupoPapel1.getId());
        assertThat(grupoPapel1).isEqualTo(grupoPapel2);
        grupoPapel2.setId(2L);
        assertThat(grupoPapel1).isNotEqualTo(grupoPapel2);
        grupoPapel1.setId(null);
        assertThat(grupoPapel1).isNotEqualTo(grupoPapel2);
    }
}
