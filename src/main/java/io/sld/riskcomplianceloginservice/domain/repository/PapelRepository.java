package io.sld.riskcomplianceloginservice.domain.repository;

import io.sld.riskcomplianceloginservice.domain.entity.Papel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Papel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PapelRepository extends JpaRepository<Papel, Long>, JpaSpecificationExecutor<Papel> {}
