package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.PapelDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PapelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PapelDTO.class);
        PapelDTO papelDTO1 = new PapelDTO();
        papelDTO1.setId(1L);
        PapelDTO papelDTO2 = new PapelDTO();
        assertThat(papelDTO1).isNotEqualTo(papelDTO2);
        papelDTO2.setId(papelDTO1.getId());
        assertThat(papelDTO1).isEqualTo(papelDTO2);
        papelDTO2.setId(2L);
        assertThat(papelDTO1).isNotEqualTo(papelDTO2);
        papelDTO1.setId(null);
        assertThat(papelDTO1).isNotEqualTo(papelDTO2);
    }
}
