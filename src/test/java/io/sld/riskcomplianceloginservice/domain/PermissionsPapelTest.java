package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissionsPapelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionsPapel.class);
        PermissionsPapel permissionsPapel1 = new PermissionsPapel();
        permissionsPapel1.setId(1L);
        PermissionsPapel permissionsPapel2 = new PermissionsPapel();
        permissionsPapel2.setId(permissionsPapel1.getId());
        assertThat(permissionsPapel1).isEqualTo(permissionsPapel2);
        permissionsPapel2.setId(2L);
        assertThat(permissionsPapel1).isNotEqualTo(permissionsPapel2);
        permissionsPapel1.setId(null);
        assertThat(permissionsPapel1).isNotEqualTo(permissionsPapel2);
    }
}
