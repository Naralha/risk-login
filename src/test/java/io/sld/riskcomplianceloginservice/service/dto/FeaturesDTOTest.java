package io.sld.riskcomplianceloginservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.sld.riskcomplianceloginservice.domain.service.dto.FeaturesDTO;
import io.sld.riskcomplianceloginservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeaturesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeaturesDTO.class);
        FeaturesDTO featuresDTO1 = new FeaturesDTO();
        featuresDTO1.setId(1L);
        FeaturesDTO featuresDTO2 = new FeaturesDTO();
        assertThat(featuresDTO1).isNotEqualTo(featuresDTO2);
        featuresDTO2.setId(featuresDTO1.getId());
        assertThat(featuresDTO1).isEqualTo(featuresDTO2);
        featuresDTO2.setId(2L);
        assertThat(featuresDTO1).isNotEqualTo(featuresDTO2);
        featuresDTO1.setId(null);
        assertThat(featuresDTO1).isNotEqualTo(featuresDTO2);
    }
}
