package io.sld.riskcomplianceloginservice.domain.repository;

import io.sld.riskcomplianceloginservice.domain.entity.Permissions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Permissions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionsRepository extends JpaRepository<Permissions, Long>, JpaSpecificationExecutor<Permissions> {}
