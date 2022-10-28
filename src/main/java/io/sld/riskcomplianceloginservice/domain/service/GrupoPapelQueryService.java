package io.sld.riskcomplianceloginservice.domain.service;

import io.sld.riskcomplianceloginservice.domain.entity.*;
import io.sld.riskcomplianceloginservice.domain.repository.GrupoPapelRepository;
import io.sld.riskcomplianceloginservice.domain.service.criteria.GrupoPapelCriteria;
import io.sld.riskcomplianceloginservice.domain.service.dto.GrupoPapelDTO;
import io.sld.riskcomplianceloginservice.domain.service.mapper.GrupoPapelMapper;
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
 * Service for executing complex queries for {@link GrupoPapel} entities in the database.
 * The main input is a {@link GrupoPapelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GrupoPapelDTO} or a {@link Page} of {@link GrupoPapelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GrupoPapelQueryService extends QueryService<GrupoPapel> {

    private final Logger log = LoggerFactory.getLogger(GrupoPapelQueryService.class);

    private final GrupoPapelRepository grupoPapelRepository;

    private final GrupoPapelMapper grupoPapelMapper;

    public GrupoPapelQueryService(GrupoPapelRepository grupoPapelRepository, GrupoPapelMapper grupoPapelMapper) {
        this.grupoPapelRepository = grupoPapelRepository;
        this.grupoPapelMapper = grupoPapelMapper;
    }

    /**
     * Return a {@link List} of {@link GrupoPapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GrupoPapelDTO> findByCriteria(GrupoPapelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GrupoPapel> specification = createSpecification(criteria);
        return grupoPapelMapper.toDto(grupoPapelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GrupoPapelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GrupoPapelDTO> findByCriteria(GrupoPapelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GrupoPapel> specification = createSpecification(criteria);
        return grupoPapelRepository.findAll(specification, page).map(grupoPapelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GrupoPapelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GrupoPapel> specification = createSpecification(criteria);
        return grupoPapelRepository.count(specification);
    }

    /**
     * Function to convert {@link GrupoPapelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GrupoPapel> createSpecification(GrupoPapelCriteria criteria) {
        Specification<GrupoPapel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GrupoPapel_.id));
            }
            if (criteria.getIdnVarGrupo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarGrupo(), GrupoPapel_.idnVarGrupo));
            }
            if (criteria.getIdnVarPapel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarPapel(), GrupoPapel_.idnVarPapel));
            }
            if (criteria.getIdnVarUsuario() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarUsuario(), GrupoPapel_.idnVarUsuario));
            }
            if (criteria.getIdnVarEmpresa() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdnVarEmpresa(), GrupoPapel_.idnVarEmpresa));
            }
            if (criteria.getGrupoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getGrupoId(), root -> root.join(GrupoPapel_.grupo, JoinType.LEFT).get(Grupo_.id))
                    );
            }
            if (criteria.getPapelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPapelId(), root -> root.join(GrupoPapel_.papel, JoinType.LEFT).get(Papel_.id))
                    );
            }
            if (criteria.getEmpresaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmpresaId(), root -> root.join(GrupoPapel_.empresa, JoinType.LEFT).get(Empresa_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(GrupoPapel_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
