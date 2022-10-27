package io.sld.riskcomplianceloginservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.mapper.AppMapper;
import org.junit.jupiter.api.BeforeEach;

class AppMapperTest {

    private AppMapper appMapper;

    @BeforeEach
    public void setUp() {
        appMapper = new AppMapperImpl();
    }
}
