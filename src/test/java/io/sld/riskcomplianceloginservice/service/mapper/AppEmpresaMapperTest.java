package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.AppEmpresaMapper;
import org.junit.jupiter.api.BeforeEach;

class AppEmpresaMapperTest {

    private AppEmpresaMapper appEmpresaMapper;

    @BeforeEach
    public void setUp() {
        appEmpresaMapper = new AppEmpresaMapperImpl();
    }
}
