package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioGrupoDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioGrupoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioGrupoDTO.class);
        UsuarioGrupoDTO usuarioGrupoDTO1 = new UsuarioGrupoDTO();
        usuarioGrupoDTO1.setId(1L);
        UsuarioGrupoDTO usuarioGrupoDTO2 = new UsuarioGrupoDTO();
        assertThat(usuarioGrupoDTO1).isNotEqualTo(usuarioGrupoDTO2);
        usuarioGrupoDTO2.setId(usuarioGrupoDTO1.getId());
        assertThat(usuarioGrupoDTO1).isEqualTo(usuarioGrupoDTO2);
        usuarioGrupoDTO2.setId(2L);
        assertThat(usuarioGrupoDTO1).isNotEqualTo(usuarioGrupoDTO2);
        usuarioGrupoDTO1.setId(null);
        assertThat(usuarioGrupoDTO1).isNotEqualTo(usuarioGrupoDTO2);
    }
}
