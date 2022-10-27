package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioPapelMapper;
import org.junit.jupiter.api.BeforeEach;

class UsuarioPapelMapperTest {

    private UsuarioPapelMapper usuarioPapelMapper;

    @BeforeEach
    public void setUp() {
        usuarioPapelMapper = new UsuarioPapelMapperImpl();
    }
}
