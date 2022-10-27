package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GrupoDTO.class);
        GrupoDTO grupoDTO1 = new GrupoDTO();
        grupoDTO1.setId(1L);
        GrupoDTO grupoDTO2 = new GrupoDTO();
        assertThat(grupoDTO1).isNotEqualTo(grupoDTO2);
        grupoDTO2.setId(grupoDTO1.getId());
        assertThat(grupoDTO1).isEqualTo(grupoDTO2);
        grupoDTO2.setId(2L);
        assertThat(grupoDTO1).isNotEqualTo(grupoDTO2);
        grupoDTO1.setId(null);
        assertThat(grupoDTO1).isNotEqualTo(grupoDTO2);
    }
}
