package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioPapelDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UsuarioPapelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsuarioPapelDTO.class);
        UsuarioPapelDTO usuarioPapelDTO1 = new UsuarioPapelDTO();
        usuarioPapelDTO1.setId(1L);
        UsuarioPapelDTO usuarioPapelDTO2 = new UsuarioPapelDTO();
        assertThat(usuarioPapelDTO1).isNotEqualTo(usuarioPapelDTO2);
        usuarioPapelDTO2.setId(usuarioPapelDTO1.getId());
        assertThat(usuarioPapelDTO1).isEqualTo(usuarioPapelDTO2);
        usuarioPapelDTO2.setId(2L);
        assertThat(usuarioPapelDTO1).isNotEqualTo(usuarioPapelDTO2);
        usuarioPapelDTO1.setId(null);
        assertThat(usuarioPapelDTO1).isNotEqualTo(usuarioPapelDTO2);
    }
}
