package io.sld.riskcomplianceloginservice.domain.repository;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioPapel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UsuarioPapel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioPapelRepository extends JpaRepository<UsuarioPapel, Long>, JpaSpecificationExecutor<UsuarioPapel> {}
