package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioGrupoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioGrupo.class);
        UsuarioGrupo usuarioGrupo1 = new UsuarioGrupo();
        usuarioGrupo1.setId(1L);
        UsuarioGrupo usuarioGrupo2 = new UsuarioGrupo();
        usuarioGrupo2.setId(usuarioGrupo1.getId());
        assertThat(usuarioGrupo1).isEqualTo(usuarioGrupo2);
        usuarioGrupo2.setId(2L);
        assertThat(usuarioGrupo1).isNotEqualTo(usuarioGrupo2);
        usuarioGrupo1.setId(null);
        assertThat(usuarioGrupo1).isNotEqualTo(usuarioGrupo2);
    }
}
