package io.sld.riskcomplianceloginservice.domain.repository;

import io.sld.riskcomplianceloginservice.domain.entity.AppEmpresa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppEmpresa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppEmpresaRepository extends JpaRepository<AppEmpresa, Long>, JpaSpecificationExecutor<AppEmpresa> {}
