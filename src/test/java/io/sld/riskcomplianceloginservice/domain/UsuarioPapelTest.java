package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioPapelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioPapel.class);
        UsuarioPapel usuarioPapel1 = new UsuarioPapel();
        usuarioPapel1.setId(1L);
        UsuarioPapel usuarioPapel2 = new UsuarioPapel();
        usuarioPapel2.setId(usuarioPapel1.getId());
        assertThat(usuarioPapel1).isEqualTo(usuarioPapel2);
        usuarioPapel2.setId(2L);
        assertThat(usuarioPapel1).isNotEqualTo(usuarioPapel2);
        usuarioPapel1.setId(null);
        assertThat(usuarioPapel1).isNotEqualTo(usuarioPapel2);
    }
}
