package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.PapelMapper;
import org.junit.jupiter.api.BeforeEach;

class PapelMapperTest {

    private PapelMapper papelMapper;

    @BeforeEach
    public void setUp() {
        papelMapper = new PapelMapperImpl();
    }
}
