package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.EmpresaMapper;
import org.junit.jupiter.api.BeforeEach;

class EmpresaMapperTest {

    private EmpresaMapper empresaMapper;

    @BeforeEach
    public void setUp() {
        empresaMapper = new EmpresaMapperImpl();
    }
}
