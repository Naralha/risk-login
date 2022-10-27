package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioGrupoMapper;
import org.junit.jupiter.api.BeforeEach;

class UsuarioGrupoMapperTest {

    private UsuarioGrupoMapper usuarioGrupoMapper;

    @BeforeEach
    public void setUp() {
        usuarioGrupoMapper = new UsuarioGrupoMapperImpl();
    }
}
