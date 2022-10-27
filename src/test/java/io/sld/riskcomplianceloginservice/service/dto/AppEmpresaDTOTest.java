package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.AppEmpresaDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppEmpresaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppEmpresaDTO.class);
        AppEmpresaDTO appEmpresaDTO1 = new AppEmpresaDTO();
        appEmpresaDTO1.setId(1L);
        AppEmpresaDTO appEmpresaDTO2 = new AppEmpresaDTO();
        assertThat(appEmpresaDTO1).isNotEqualTo(appEmpresaDTO2);
        appEmpresaDTO2.setId(appEmpresaDTO1.getId());
        assertThat(appEmpresaDTO1).isEqualTo(appEmpresaDTO2);
        appEmpresaDTO2.setId(2L);
        assertThat(appEmpresaDTO1).isNotEqualTo(appEmpresaDTO2);
        appEmpresaDTO1.setId(null);
        assertThat(appEmpresaDTO1).isNotEqualTo(appEmpresaDTO2);
    }
}
