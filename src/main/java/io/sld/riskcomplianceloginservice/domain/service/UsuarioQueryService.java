package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.UsuarioCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for executing complex queries for {@link Usuario} entities in the database.
 * The main input is a {@link UsuarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsuarioDTO} or a {@link Page} of {@link UsuarioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioQueryService extends QueryService<Usuario> {

    private final Logger log = LoggerFactory.getLogger(UsuarioQueryService.class);

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    public UsuarioQueryService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    /**
     * Return a {@link List} of {@link UsuarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> findByCriteria(UsuarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioMapper.toDto(usuarioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsuarioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> findByCriteria(UsuarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.findAll(specification, page).map(usuarioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Usuario> createSpecification(UsuarioCriteria criteria) {
        Specification<Usuario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Usuario_.id));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), Usuario_.idnVarUsuario));
            }
            if (criteria.getnVarNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getnVarNome(), Usuario_.nVarNome));
            }
            if (criteria.getIdnVarEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarEmpresa(), Usuario_.idnVarEmpresa));
            }
            if (criteria.getIdnVarUsuarioCadastro() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getIdnVarUsuarioCadastro(), Usuario_.idnVarUsuarioCadastro));
            }
            if (criteria.getnVarSenha() != null) {
                specification = specification.and(buildStringSpecification(criteria.getnVarSenha(), Usuario_.nVarSenha));
            }
            if (criteria.getAppId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppId(), root -> root.join(Usuario_.apps, JoinType.LEFT).get(App_.id))
                    );
            }
            if (criteria.getAppEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppEmpresaId(),
                            root -> root.join(Usuario_.appEmpresas, JoinType.LEFT).get(AppEmpresa_.id)
                        )
                    );
            }
            if (criteria.getFeaturesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFeaturesId(), root -> root.join(Usuario_.features, JoinType.LEFT).get(Features_.id))
                    );
            }
            if (criteria.getGrupoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGrupoId(), root -> root.join(Usuario_.grupos, JoinType.LEFT).get(Grupo_.id))
                    );
            }
            if (criteria.getGrupoPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGrupoPapelId(),
                            root -> root.join(Usuario_.grupoPapels, JoinType.LEFT).get(GrupoPapel_.id)
                        )
                    );
            }
            if (criteria.getPermissionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPermissionsId(),
                            root -> root.join(Usuario_.permissions, JoinType.LEFT).get(Permissions_.id)
                        )
                    );
            }
            if (criteria.getPermissionsPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPermissionsPapelId(),
                            root -> root.join(Usuario_.permissionsPapels, JoinType.LEFT).get(PermissionsPapel_.id)
                        )
                    );
            }
            if (criteria.getPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPapelId(), root -> root.join(Usuario_.papels, JoinType.LEFT).get(Papel_.id))
                    );
            }
            if (criteria.getUsuarioGrupoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioGrupoId(),
                            root -> root.join(Usuario_.usuarioGrupos, JoinType.LEFT).get(UsuarioGrupo_.id)
                        )
                    );
            }
            if (criteria.getUsuarioPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioPapelId(),
                            root -> root.join(Usuario_.usuarioPapels, JoinType.LEFT).get(UsuarioPapel_.id)
                        )
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(Usuario_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
        }
        return specification;
    }
}
