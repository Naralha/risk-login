package io.sld.riskcomplianceloginservice.domain.repository;

import io.sld.riskcomplianceloginservice.domain.entity.PermissionsPapel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PermissionsPapel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionsPapelRepository extends JpaRepository<PermissionsPapel, Long>, JpaSpecificationExecutor<PermissionsPapel> {}
