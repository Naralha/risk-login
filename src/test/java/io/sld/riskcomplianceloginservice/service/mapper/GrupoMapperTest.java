package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoMapper;
import org.junit.jupiter.api.BeforeEach;

class GrupoMapperTest {

    private GrupoMapper grupoMapper;

    @BeforeEach
    public void setUp() {
        grupoMapper = new GrupoMapperImpl();
    }
}
