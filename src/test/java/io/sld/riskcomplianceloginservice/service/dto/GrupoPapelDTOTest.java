package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoPapelDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoPapelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GrupoPapelDTO.class);
        GrupoPapelDTO grupoPapelDTO1 = new GrupoPapelDTO();
        grupoPapelDTO1.setId(1L);
        GrupoPapelDTO grupoPapelDTO2 = new GrupoPapelDTO();
        assertThat(grupoPapelDTO1).isNotEqualTo(grupoPapelDTO2);
        grupoPapelDTO2.setId(grupoPapelDTO1.getId());
        assertThat(grupoPapelDTO1).isEqualTo(grupoPapelDTO2);
        grupoPapelDTO2.setId(2L);
        assertThat(grupoPapelDTO1).isNotEqualTo(grupoPapelDTO2);
        grupoPapelDTO1.setId(null);
        assertThat(grupoPapelDTO1).isNotEqualTo(grupoPapelDTO2);
    }
}
