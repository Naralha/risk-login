package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioDTO.class);
        UsuarioDTO usuarioDTO1 = new UsuarioDTO();
        usuarioDTO1.setId(1L);
        UsuarioDTO usuarioDTO2 = new UsuarioDTO();
        assertThat(usuarioDTO1).isNotEqualTo(usuarioDTO2);
        usuarioDTO2.setId(usuarioDTO1.getId());
        assertThat(usuarioDTO1).isEqualTo(usuarioDTO2);
        usuarioDTO2.setId(2L);
        assertThat(usuarioDTO1).isNotEqualTo(usuarioDTO2);
        usuarioDTO1.setId(null);
        assertThat(usuarioDTO1).isNotEqualTo(usuarioDTO2);
    }
}
