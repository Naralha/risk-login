package io.sld.riskcomplianceloginservice.domain.repository;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UsuarioGrupo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsuarioGrupoRepository extends JpaRepository<UsuarioGrupo, Long>, JpaSpecificationExecutor<UsuarioGrupo> {}
