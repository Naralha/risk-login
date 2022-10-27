package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoPapelMapper;
import org.junit.jupiter.api.BeforeEach;

class GrupoPapelMapperTest {

    private GrupoPapelMapper grupoPapelMapper;

    @BeforeEach
    public void setUp() {
        grupoPapelMapper = new GrupoPapelMapperImpl();
    }
}
