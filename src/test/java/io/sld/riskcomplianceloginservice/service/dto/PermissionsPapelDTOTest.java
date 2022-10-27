package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.PermissionsPapelDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermissionsPapelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermissionsPapelDTO.class);
        PermissionsPapelDTO permissionsPapelDTO1 = new PermissionsPapelDTO();
        permissionsPapelDTO1.setId(1L);
        PermissionsPapelDTO permissionsPapelDTO2 = new PermissionsPapelDTO();
        assertThat(permissionsPapelDTO1).isNotEqualTo(permissionsPapelDTO2);
        permissionsPapelDTO2.setId(permissionsPapelDTO1.getId());
        assertThat(permissionsPapelDTO1).isEqualTo(permissionsPapelDTO2);
        permissionsPapelDTO2.setId(2L);
        assertThat(permissionsPapelDTO1).isNotEqualTo(permissionsPapelDTO2);
        permissionsPapelDTO1.setId(null);
        assertThat(permissionsPapelDTO1).isNotEqualTo(permissionsPapelDTO2);
    }
}
