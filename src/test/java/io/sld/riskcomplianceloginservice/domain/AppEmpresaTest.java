package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppEmpresaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppEmpresa.class);
        AppEmpresa appEmpresa1 = new AppEmpresa();
        appEmpresa1.setId(1L);
        AppEmpresa appEmpresa2 = new AppEmpresa();
        appEmpresa2.setId(appEmpresa1.getId());
        assertThat(appEmpresa1).isEqualTo(appEmpresa2);
        appEmpresa2.setId(2L);
        assertThat(appEmpresa1).isNotEqualTo(appEmpresa2);
        appEmpresa1.setId(null);
        assertThat(appEmpresa1).isNotEqualTo(appEmpresa2);
    }
}
