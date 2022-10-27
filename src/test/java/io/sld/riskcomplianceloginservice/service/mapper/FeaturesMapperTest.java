package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.FeaturesMapper;
import org.junit.jupiter.api.BeforeEach;

class FeaturesMapperTest {

    private FeaturesMapper featuresMapper;

    @BeforeEach
    public void setUp() {
        featuresMapper = new FeaturesMapperImpl();
    }
}
