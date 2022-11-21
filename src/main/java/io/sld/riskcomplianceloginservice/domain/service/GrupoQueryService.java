package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.GrupoCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoMapper;
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
 * Service for executing complex queries for {@link Grupo} entities in the database.
 * The main input is a {@link GrupoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GrupoDTO} or a {@link Page} of {@link GrupoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GrupoQueryService extends QueryService<Grupo> {

    private final Logger log = LoggerFactory.getLogger(GrupoQueryService.class);

    private final GrupoRepository grupoRepository;

    private final GrupoMapper grupoMapper;

    public GrupoQueryService(GrupoRepository grupoRepository, GrupoMapper grupoMapper) {
        this.grupoRepository = grupoRepository;
        this.grupoMapper = grupoMapper;
    }

    /**
     * Return a {@link List} of {@link GrupoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GrupoDTO> findByCriteria(GrupoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Grupo> specification = createSpecification(criteria);
        return grupoMapper.toDto(grupoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GrupoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GrupoDTO> findByCriteria(GrupoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Grupo> specification = createSpecification(criteria);
        return grupoRepository.findAll(specification, page).map(grupoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GrupoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Grupo> specification = createSpecification(criteria);
        return grupoRepository.count(specification);
    }

    /**
     * Function to convert {@link GrupoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Grupo> createSpecification(GrupoCriteria criteria) {
        Specification<Grupo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Grupo_.id));
            }
            if (criteria.getIdnVarGrupo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarGrupo(), Grupo_.idnVarGrupo));
            }
            if (criteria.getnVarNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getnVarNome(), Grupo_.nVarNome));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), Grupo_.idnVarUsuario));
            }
            if (criteria.getIdnVarEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarEmpresa(), Grupo_.idnVarEmpresa));
            }
            if (criteria.getGrupoPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGrupoPapelId(),
                            root -> root.join(Grupo_.grupoPapels, JoinType.LEFT).get(GrupoPapel_.id)
                        )
                    );
            }
            if (criteria.getUsuarioGrupoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioGrupoId(),
                            root -> root.join(Grupo_.usuarioGrupos, JoinType.LEFT).get(UsuarioGrupo_.id)
                        )
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(Grupo_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Grupo_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
