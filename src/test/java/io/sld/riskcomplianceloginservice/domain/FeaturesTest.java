package io.sld.riskcomplianceloginservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.entity.Features;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeaturesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Features.class);
        Features features1 = new Features();
        features1.setId(1L);
        Features features2 = new Features();
        features2.setId(features1.getId());
        assertThat(features1).isEqualTo(features2);
        features2.setId(2L);
        assertThat(features1).isNotEqualTo(features2);
        features1.setId(null);
        assertThat(features1).isNotEqualTo(features2);
    }
}
