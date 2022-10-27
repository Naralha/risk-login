package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.PermissionsMapper;
import org.junit.jupiter.api.BeforeEach;

class PermissionsMapperTest {

    private PermissionsMapper permissionsMapper;

    @BeforeEach
    public void setUp() {
        permissionsMapper = new PermissionsMapperImpl();
    }
}
