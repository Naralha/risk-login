package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.UsuarioGrupo;
import io.sld.riskcomplianceloginservice.domain.repository.UsuarioGrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.UsuarioGrupoCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.UsuarioGrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.UsuarioGrupoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UsuarioGrupo} entities in the database.
 * The main input is a {@link UsuarioGrupoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsuarioGrupoDTO} or a {@link Page} of {@link UsuarioGrupoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioGrupoQueryService extends QueryService<UsuarioGrupo> {

    private final Logger log = LoggerFactory.getLogger(UsuarioGrupoQueryService.class);

    private final UsuarioGrupoRepository usuarioGrupoRepository;

    private final UsuarioGrupoMapper usuarioGrupoMapper;

    public UsuarioGrupoQueryService(UsuarioGrupoRepository usuarioGrupoRepository, UsuarioGrupoMapper usuarioGrupoMapper) {
        this.usuarioGrupoRepository = usuarioGrupoRepository;
        this.usuarioGrupoMapper = usuarioGrupoMapper;
    }

    /**
     * Return a {@link List} of {@link UsuarioGrupoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioGrupoDTO> findByCriteria(UsuarioGrupoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UsuarioGrupo> specification = createSpecification(criteria);
        return usuarioGrupoMapper.toDto(usuarioGrupoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsuarioGrupoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioGrupoDTO> findByCriteria(UsuarioGrupoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UsuarioGrupo> specification = createSpecification(criteria);
        return usuarioGrupoRepository.findAll(specification, page).map(usuarioGrupoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioGrupoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UsuarioGrupo> specification = createSpecification(criteria);
        return usuarioGrupoRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioGrupoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UsuarioGrupo> createSpecification(UsuarioGrupoCriteria criteria) {
        Specification<UsuarioGrupo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UsuarioGrupo_.id));
            }
            if (criteria.getIdnVarUsuarioCadastrado() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getIdnVarUsuarioCadastrado(), UsuarioGrupo_.idnVarUsuarioCadastrado)
                    );
            }
            if (criteria.getIdnVarGrupo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarGrupo(), UsuarioGrupo_.idnVarGrupo));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), UsuarioGrupo_.idnVarUsuario));
            }
            if (criteria.getGrupoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGrupoId(), root -> root.join(UsuarioGrupo_.grupo, JoinType.LEFT).get(Grupo_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioId(),
                            root -> root.join(UsuarioGrupo_.usuario, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
