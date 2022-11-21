package io.sld.riskcomplianceloginservice.domain.repository;

import io.sld.riskcomplianceloginservice.domain.entity.Features;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Features entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeaturesRepository extends JpaRepository<Features, Long>, JpaSpecificationExecutor<Features> {}
