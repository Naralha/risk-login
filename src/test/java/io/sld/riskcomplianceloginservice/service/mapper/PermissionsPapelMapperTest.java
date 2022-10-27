package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.PermissionsPapelMapper;
import org.junit.jupiter.api.BeforeEach;

class PermissionsPapelMapperTest {

    private PermissionsPapelMapper permissionsPapelMapper;

    @BeforeEach
    public void setUp() {
        permissionsPapelMapper = new PermissionsPapelMapperImpl();
    }
}
