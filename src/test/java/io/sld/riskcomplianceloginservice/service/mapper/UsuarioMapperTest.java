package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;

class UsuarioMapperTest {

    private UsuarioMapper usuarioMapper;

    @BeforeEach
    public void setUp() {
        usuarioMapper = new UsuarioMapperImpl();
    }
}
